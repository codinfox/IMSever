package demo.Zhihao.Server;

import java.io.IOException;
import java.net.*;

/**
 * The class used to answer the connection request via TCP and allocate UDP port number.<br>
 * The first class used when connecting the server.
 * @author ben
 *
 */
class Welcome implements Runnable{
	private Jabber jabber = null;
	private ServerSocket welcomeSocket = null;
	private boolean active = false;
	private Clients clients = null;
	
	/**
	 * Creates and init the Welcome class.
	 * @param clients the specified clients.(unique in the program)
	 * @param jabber the Jabber class specified. 
	 * @param port the local port specified.
	 * @see Jabber
	 */
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
	
	/**
	 * Closes connections.
	 */
	public void close() {
		try {
			active = false;
			jabber.close();
			welcomeSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if the thread is still alive.
	 * @return true if alive, and vice versa.
	 */
	public boolean isActive() {
		return active;
	}
}

/**
 * an auxiliary class used to represent members.
 * @author ben
 *
 */
class Member {
	public Socket socket = null;
	public String name = null;
	
	/**
	 * Creates and init the member class.
	 * @param socket the member's socket.
	 * @param name the mebmer's name.
	 */
	public Member(Socket socket, String name) {
		this.socket = socket;
		this.name = name;
	}
}
