package demo.Zhihao.Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
//信息转发者
public class Relayer {
	private DatagramSocket udpServer;
	private ArrayList<Socket> clients;
	private Receiver receiver;
	private Sender sender;
	public Relayer(DatagramSocket udpServer,ArrayList<Socket> clients){
		this.udpServer = udpServer;
		this.clients = clients;
		receiver = new Receiver(udpServer);
		sender = new Sender(clients);
	}
	//转发信息（首先接收，再发送）
	public void messageProcess() throws IOException{
		byte [] message = receiver.receive();
		sender.send(message);
	}
}