package demo.Zhihao.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//信息接收者
public class Receiver {
	private DatagramSocket udpServer;
	public Receiver(DatagramSocket udpServer){
		this.udpServer = udpServer;
	}
	public byte [] receive() throws IOException{
		byte [] message = new byte[100];
		//收到的信息放到packet这个包里
		DatagramPacket packet = new DatagramPacket(message, message.length);
		try{
			udpServer.receive(packet);
		}catch(Exception e){
			System.out.println("socket has been closed");
		}
		
		//新建一个String对象，并返回byte[]形式，内容为收到的信息，0，packet.getLenth为取信息的长度
		return new String(message, 0, packet.getLength()).getBytes();
	}
}
