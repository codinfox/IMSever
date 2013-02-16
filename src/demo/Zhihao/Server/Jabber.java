package demo.Zhihao.Server;

import java.io.IOException;
import java.net.*;

public class Jabber extends Thread{
	private Clients clients = null;
	private DatagramSocket udpSocket = null;
	private boolean active = true;
	private UDPReceiver udpReceiver = null;
	
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
		
	public int getLocalPort() {
		return udpSocket.getLocalPort();
	}
	
	public String getHostAddress() {
		return udpSocket.getLocalAddress().getHostAddress();
	}
	
	@Override
	public void run() {
		active = true;
		while (active) {
			try {
				String msg = udpReceiver.receive();
				if (msg.startsWith("QUIT: ")) {
					clientQuit(msg);
					msg = msg.substring("QUIT: ".length(), msg.length()-1);
					msg += " QUIT";
					msg = "<sys> " + msg + "\n";
				} else if (msg.startsWith("<conn-info> ")) {
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
	
	private void clientQuit(String msg) {
		String name = msg.substring("QUIT: ".length(), msg.length()-1);
		System.out.println("quiting: " + name);
		clients.remove(name);
	}
	
	public void close() throws IOException {
		active = false;
		udpSocket.close();
		Sender.send(clients, "QUIT SERVER\n");
	}
}