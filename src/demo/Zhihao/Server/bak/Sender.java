package demo.Zhihao.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//��Ϣ������
public class Sender {
	private ArrayList<Socket> clients;
	private Socket client;
	public Sender(Socket client){
		this.client = client;
	}
	//���Ͷ˿ں�
	public void send(int port) throws IOException{
		PrintWriter writer = new PrintWriter(client.getOutputStream());
		//���Ͷ˿ںţ���ִ��writer.flush()֮����������ͣ���Ҫ���Ǽ����
		writer.println(port);
		writer.flush();
		
		System.out.println(port);
	}
	public Sender(ArrayList<Socket> clients){
		this.clients = clients;
	}
	//���ͽ��յ�����Ϣ
	public void send(byte [] message) throws IOException{
		Iterator<Socket> it = (Iterator<Socket>) clients.iterator();
		while(it.hasNext()){
			Socket client = (Socket)it.next();
			
			PrintWriter writer = new PrintWriter(client.getOutputStream());
			writer.println(new String(message,0,message.length));
			writer.flush();
		}
	}
}
