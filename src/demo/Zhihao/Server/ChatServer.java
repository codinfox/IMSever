package demo.Zhihao.Server;

import javax.swing.DefaultListModel;

public class ChatServer {

	private Clients clients = new Clients();
	private Welcome welcome = null;
	
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
	
	public void forceQuit(String name) {
		clients.forceQuit(name);
	}
	
	public void updateInfo(String info) {
		clients.updateInfo(info);
	}
	
	
	public void setMemberModel(DefaultListModel<String> model) {
		clients.setMemberModel(model);
	}
	
	public void close() {
		welcome.close();
	}
	
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("param error");
			return;
		}
		
		// TODO: a second chance
		new ChatServer(Integer.parseInt(args[0]));
	}

}