package demo.Zhihao.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Used to receive messages from the specified socket.
 * @author ben
 *
 */
class Receiver {
	private BufferedReader reader = null;
	
	/**
	 * Creates and init the Receiver by specifying a socket.
	 * @param socket the socket from which to receive data.
	 */
	public Receiver(Socket socket) {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Receives data.
	 * @return the data received.
	 * @throws IOException if IOException occurs while retrieving data.
	 */
	public String receive() throws IOException{
		return reader.readLine();
	}
}
