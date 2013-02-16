package demo.Zhihao.Server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.net.*;

/**
 * This class is used to send messages using TCP protocol.<br>
 * This class should <strong>never</strong> be instantiated.
 * @author ben
 */
public class Sender {
	
	/**
	 * Use this method to send message to a member using TCP.
	 * @param member to which the message is sent.
	 * @param msg the content of the message.
	 * @throws IOException if an IOException occurs while sending the data. 
	 */
	public static void send(Member member, String msg) throws IOException {
		send(member.socket, msg);
		//writer.close();
	}
	
	/**
	 * Use this method to send message to a bunch of sockets using TCP.
	 * @param clients to which the message is sent.
	 * @param msg the content of the message.
	 * @throws IOException if an IOException occurs while sending the data. 
	 */
	public static void send(Clients clients, String msg) throws IOException {
		Iterator<Member> iterator = clients.iterator();
		while (iterator.hasNext()) {
			Socket socket = iterator.next().socket;
			send(socket, msg);
			System.out.println("Sent: " + msg);
		}
	}
	
	/**
	 * Use this method to send message to a socket using TCP.
	 * @param socket to which the message is sent.
	 * @param msg the content of the message.
	 * @throws IOException if an IOException occurs while sending the data. 
	 */
	public static void send(Socket socket, String msg) throws IOException {
		OutputStream stream = socket.getOutputStream();
		PrintWriter writer = new PrintWriter(stream, true);
		writer.print(msg);
		writer.flush();
		//writer.close();
	}
}
