package demo.Zhihao.Server;

import java.io.IOException;
import java.net.*;

public class Welcome implements Runnable{
	private Jabber jabber = null;
	private ServerSocket welcomeSocket = null;
	private boolean active = false;
	private Clients clients = null;
	
	public Welcome(Clients clients, Jabber jabber, int port) {
		this.jabber = jabber;
		this.clients = clients;
		try {
			welcomeSocket = new ServerSocket(port);
			active = true;
		} catch (IOException e) {
			System.err.println("Server cannot be launched.");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		active = true;
		while (active)
			try {
				Socket client = welcomeSocket.accept();
				System.out.println("client");
				Receiver receiver = new Receiver(client);
				String name = receiver.receive();
				Sender.send(client, "WELCOME "+Integer.toString(jabber.getLocalPort())+"\n");
				System.out.println(name);
				name = name.substring("HELO ".length());
				if (clients.isNameExist(name)) {
					int i = 2;
					int length = name.length();
					name += i;
					while (clients.isNameExist(name)) {
						name = name.substring(0, length);
						name += i;
						i++;
					}
				}
				System.out.println("welcome username: " + name);
				Sender.send(client, "WELCOME USERNAME: " + name + "\n");
				clients.add(new Member(client, name));
				System.out.println(jabber.getLocalPort());
				clients.initInfo(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void close() {
		try {
			active = false;
			jabber.close();
			welcomeSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isActive() {
		return active;
	}
}

class Member {
	public Socket socket = null;
	public String name = null;
	
	public Member(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
}
