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

	//敞开大门，使客户端能够连接进来
	public void open() throws IOException{
		while(!tcpServer.isClosed()){
			try{
				/*当socket已关闭时，仍执行tcpServer.accept，会有异常
					我这里直接用异常处理应付过去了，你们做的时候要处理一下细节
					，判断关闭没有，不能直接抛出异常
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
	
	//信息处理
	public void messageProcess() throws IOException{
		while(!tcpServer.isClosed()){
			relayer.messageProcess();
		}
	}
	//关闭连接
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