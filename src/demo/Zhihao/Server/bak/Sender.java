package demo.Zhihao.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

//信息发送者
public class Sender {
	private ArrayList<Socket> clients;
	private Socket client;
	public Sender(Socket client){
		this.client = client;
	}
	//发送端口号
	public void send(int port) throws IOException{
		PrintWriter writer = new PrintWriter(client.getOutputStream());
		//发送端口号，当执行writer.flush()之后才真正发送，不要忘记加这个
		writer.println(port);
		writer.flush();
		
		System.out.println(port);
	}
	public Sender(ArrayList<Socket> clients){
		this.clients = clients;
	}
	//发送接收到的信息
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
