package demo.Zhihao.Server;

import java.io.IOException;
import java.net.*;

/**
 * This is used to redirect the messages sent to the server to all the clients that connecting to the server.<br>
 * The protocol used to receive the messages is UDP, whereas the protocol used to send messages is TCP.<br>
 * This class also do some process to the messages received. 
 * @author ben
 *
 */
class Jabber extends Thread{
	private Clients clients = null;
	private DatagramSocket udpSocket = null;
	private boolean active = true;
	private UDPReceiver udpReceiver = null;
	
	/**
	 * Creates a new Jabber instance by specifying the Clients.
	 * @param clients the clients specified.
	 */
	public Jabber(Clients clients) {
		this.clients = clients;
		try {
			udpSocket = new DatagramSocket();
			udpReceiver = new UDPReceiver(udpSocket);
		} catch (SocketException e) {
			System.err.println("Jabber thread launch failure.");
			e.printStackTrace();
		}
	}
		
	/**
	 * Gets the local UDP port.
	 * @return the local UDP port.
	 */
	public int getLocalPort() {
		return udpSocket.getLocalPort();
	}
	
	/**
	 * Gets the local host address.
	 * @return the local host address.
	 */
	public String getHostAddress() {
		return udpSocket.getLocalAddress().getHostAddress();
	}
	
	@Override
	public void run() {
		active = true;
		while (active) {
			try {
				String msg = udpReceiver.receive();
				if (msg.startsWith("QUIT: ")) { // client quits.
					clientQuit(msg);
					msg = msg.substring("QUIT: ".length(), msg.length()-1);
					msg += " QUIT";
					msg = "<sys> " + msg + "\n";
				} else if (msg.startsWith("<conn-info> ")) { // direct connect requests.
					msg = msg.substring(0, msg.length()-1); //remove the trailing '\n'
					String[] param = msg.split(" ");
					for (String s : param)
						System.out.println("param: " + s);
					clients.directConnReq(param[1], param[2], param[3], param[4]);
					continue;
				}
				System.out.println("2 received: " + msg);
				Sender.send(clients, msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Handle the quit.
	 * @param msg the quit message.
	 */
	private void clientQuit(String msg) {
		String name = msg.substring("QUIT: ".length(), msg.length()-1);
		System.out.println("quiting: " + name);
		clients.remove(name);
	}
	
	/**
	 * Closes the connections to all the clients.
	 * @throws IOException if IOException occurs while sending messages to the clients.
	 */
	public void close() throws IOException {
		active = false;
		udpSocket.close();
		Sender.send(clients, "QUIT SERVER\n");
	}
}