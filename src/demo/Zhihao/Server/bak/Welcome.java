package demo.Zhihao.Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Welcome {
	private ServerSocket tcpServer;
	private DatagramSocket udpServer;
	private ArrayList<Socket> clients;
	private Relayer relayer;
	public Welcome(int port) throws IOException{
		tcpServer = new ServerSocket(port);
		udpServer = new DatagramSocket();
		clients = new ArrayList<Socket>();
		relayer = new Relayer(udpServer, clients);
	}

	//�������ţ�ʹ�ͻ����ܹ����ӽ���
	public void open() throws IOException{
		while(!tcpServer.isClosed()){
			try{
				/*��socket�ѹر�ʱ����ִ��tcpServer.accept�������쳣
					������ֱ�����쳣����Ӧ����ȥ�ˣ���������ʱ��Ҫ����һ��ϸ��
					���жϹر�û�У�����ֱ���׳��쳣
				*/
				Socket client = tcpServer.accept();
				clients.add(client);
				Sender sender = new Sender(client);
				sender.send(udpServer.getLocalPort());
			}catch(Exception e){
				System.out.println("socket has been closed");
			}
		}
		
	}
	
	//��Ϣ����
	public void messageProcess() throws IOException{
		while(!tcpServer.isClosed()){
			relayer.messageProcess();
		}
	}
	//�ر�����
	public void quit() throws IOException{
		Scanner in = new Scanner(System.in);
		if(in.next().equals("QUIT")){
			Sender sender = new Sender(clients);
			sender.send("QUIT".getBytes());
			tcpServer.close();
			udpServer.close();
			System.out.println("QUIT");
		}
	}
	
	public boolean isActive(){
		return !tcpServer.isClosed();
	}
}