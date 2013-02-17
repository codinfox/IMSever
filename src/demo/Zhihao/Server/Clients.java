package demo.Zhihao.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;

/**
 * Handles operation with clients. <br>
 * including system info sending, updating and etc.
 * @author ben
 *
 */
class Clients{
	private ArrayList<Member> clients = new ArrayList<>();
	private DefaultListModel<String> memberModel = null;
	private String tmpInfo = "Chatroom|This is a chatroom|nothing\n";
	
	/**
	 * Creates a new Clients and init.
	 */
	public Clients() {
		new update().start();
	}

	/**
	 * Adds a member into the clients list.
	 * @param client the new member.
	 */
	public void add(Member client) {
		clients.add(client);
		memberModel.addElement(client.name);
		updateMember();
	}

	/**
	 * Designates the model of the member list of the view part.<br>
	 * Enables the updating operations.
	 * @param model the member model.
	 */
	public void setMemberModel(DefaultListModel<String> model) {
		memberModel = model;
	}

	/**
	 * Gets the number of clients in the list.
	 * @return the number of clients.
	 */
	public int count() {
		return clients.size();
	}

	/**
	 * Gets the iterator of the list.
	 * @return the iterator of the list.
	 */
	public Iterator<Member> iterator() {
		return clients.iterator();
	}

	/**
	 * Removes a client from the list.
	 * @param name the client to be removed.
	 */
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

	/**
	 * Notice the client that it has been forced to quit.
	 * @param name the client which is forced to quit.
	 */
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

	/**
	 * Updates system info.
	 * @param info the new info.
	 */
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

	/**
	 * Updates the member information to all the clients.
	 */
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

	/**
	 * First time init system info.
	 * @param client the default info.
	 */
	public void initInfo(Socket client) {
		try {
			Sender.send(client, "<sysinfo> " + tmpInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the client's name is already existent in the server.
	 * @param name the client's original name.
	 * @return true if the name is existent, vice versa.
	 */
	public boolean isNameExist(String name) {
		Iterator<Member> iter = clients.iterator();
		while (iter.hasNext()) {
			if (iter.next().name.equals(name))
				return true;
		}
		return false;
	}

	/**
	 * Gets a specified member by name.
	 * @param name the client's name.
	 * @return the member.
	 */
	private Member getClientByName(String name) {
		for (Member member : clients) {
			if (member.name.equals(name)) 
				return member;
		}
		return null;
	}

	/**
	 * Sends direct connect request to a specified client.
	 * @param name the client from whom the request is sent.
	 * @param otherClient the client to whom the request is sent.
	 * @param ip the otherClient's IP address.
	 * @param port the otherClient's UDP port.
	 */
	public void directConnReq(String name, String otherClient, String ip, String port) {
		Member member = getClientByName(name);
		try {
			Sender.send(member,	"<conn-info> "+otherClient + " " + ip+" "+port+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used to update member list every 10 sec.
	 * @author ben
	 *
	 */
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