package demo.Zhihao.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * This is used to retrieve the messages sent using UDP. <br>
 * @author ben
 *
 */
public class UDPReceiver {
	private DatagramSocket dataSocket = null;
	
	/**
	 * Creates a new UDPReceiver by specifying UDP socket.
	 * @param dataSocket the socket to connect.
	 */
	public UDPReceiver(DatagramSocket dataSocket) {
		this.dataSocket = dataSocket;		
	}
	
	/**
	 * Retrieve data.
	 * @return the data received.
	 * @throws IOException if IOException occurs while retrieving data.
	 */
	public String receive() throws IOException {
		byte[] buffer = new byte[1024];
		DatagramPacket dataPacket = new DatagramPacket(buffer, buffer.length);
		dataSocket.receive(dataPacket);
		System.out.println("Received: " + new String(buffer, 0, dataPacket.getLength()));
		return new String(buffer, 0, dataPacket.getLength());
	}
}
