package demo.Zhihao.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//��Ϣ������
public class Receiver {
	private DatagramSocket udpServer;
	public Receiver(DatagramSocket udpServer){
		this.udpServer = udpServer;
	}
	public byte [] receive() throws IOException{
		byte [] message = new byte[100];
		//�յ�����Ϣ�ŵ�packet�������
		DatagramPacket packet = new DatagramPacket(message, message.length);
		try{
			udpServer.receive(packet);
		}catch(Exception e){
			System.out.println("socket has been closed");
		}
		
		//�½�һ��String���󣬲�����byte[]��ʽ������Ϊ�յ�����Ϣ��0��packet.getLenthΪȡ��Ϣ�ĳ���
		return new String(message, 0, packet.getLength()).getBytes();
	}
}
