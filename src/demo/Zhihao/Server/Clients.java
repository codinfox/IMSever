package demo.Zhihao.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;

class Clients{
	private ArrayList<Member> clients = new ArrayList<>();
	private DefaultListModel<String> memberModel = null;
	private String tmpInfo = "Chatroom|This is a chatroom|nothing\n";
	
	public Clients() {
		new update().start();
	}

	public void add(Member client) {
		clients.add(client);
		memberModel.addElement(client.name);
		updateMember();
	}

	public void setMemberModel(DefaultListModel<String> model) {
		memberModel = model;
	}

	public int count() {
		return clients.size();
	}

	public Iterator<Member> iterator() {
		return clients.iterator();
	}

	public void remove(String name) {
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).name.equals(name)) {
				clients.remove(i);
				memberModel.removeElement(name);
				break;
			}
		}
		updateMember();
	}

	public void forceQuit(String name) {
		Member client = getClientByName(name);
		clients.remove(client);
		try {
			Sender.send(client, "QUIT SERVER\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Member m : clients) {
			try {
				Sender.send(m, "<sys> "+name+" is forced to quit by admin\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		updateMember();
	}

	public void updateInfo(String info) {
		System.out.println("info: " + info);
		tmpInfo = info;
		for (Member m : clients) {
			try {
				Sender.send(m, "<sysinfo> " + info);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateMember() {
		System.out.println("update member");
		String memberInfo = new String();
		for (Member m : clients) {
			System.out.println("in for " + m.name);
			memberInfo += "|"+m.name;
		}
		System.out.println(memberInfo);
		for (Member m : clients) {
			try {
				Sender.send(m, "<member-info> " + memberInfo + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void initInfo(Socket client) {
		try {
			Sender.send(client, "<sysinfo> " + tmpInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public boolean isNameExist(String name) {
		Iterator<Member> iter = clients.iterator();
		while (iter.hasNext()) {
			if (iter.next().name.equals(name))
				return true;
		}
		return false;
	}

	private Member getClientByName(String name) {
		for (Member member : clients) {
			if (member.name.equals(name)) 
				return member;
		}
		return null;
	}

	public void directConnReq(String name, String otherClient, String ip, String port) {
		Member member = getClientByName(name);
		try {
			Sender.send(member,	"<conn-info> "+otherClient + " " + ip+" "+port+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class update extends Thread {
		public void run() {
			while (true) {
				updateMember();
				try {
					sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}