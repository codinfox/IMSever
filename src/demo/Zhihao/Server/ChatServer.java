package demo.Zhihao.Server;

import javax.swing.DefaultListModel;

/**
 * The main class and model of the IMServer.
 * @author ben
 *
 */
public class ChatServer {

	private Clients clients = new Clients();
	private Welcome welcome = null;
	
	/**
	 * Creates a ChatServer by specifying the local port number.
	 * @param port the local port numbe allocated to the server.
	 */
	public ChatServer(int port) {
		Jabber jabber = new Jabber(clients);
		Thread welThread = new Thread(welcome = new Welcome(clients, jabber, port));
		if (!welcome.isActive())
			System.exit(-1); // in case the port is occupied.
		welThread.start();
		jabber.start();
		new UIServer(this, jabber.getHostAddress(), port, jabber.getLocalPort());
		System.out.println("Server on!");
	}
	
	/**
	 * Forces a client to get offline.
	 * @param name the client's name.
	 */
	public void forceQuit(String name) {
		clients.forceQuit(name);
	}
	
	/**
	 * Updates the room information to all the clients.
	 * @param info the new information.
	 */
	public void updateInfo(String info) {
		clients.updateInfo(info);
	}
	
	/**
	 * Sets the member model.
	 * @param model the new member model.
	 * @see Clients#setMemberModel(DefaultListModel)
	 */
	public void setMemberModel(DefaultListModel<String> model) {
		clients.setMemberModel(model);
	}
	
	/**
	 * Closes connections.
	 */
	public void close() {
		welcome.close();
	}
	
	/**
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("param error");
			return;
		}
		
		new ChatServer(Integer.parseInt(args[0]));
	}

}