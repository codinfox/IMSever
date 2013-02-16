package demo.Zhihao.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receiver {
	private BufferedReader reader = null;
	public Receiver(Socket socket) {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String receive() throws IOException{
		return reader.readLine();
	}
}
