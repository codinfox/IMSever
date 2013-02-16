package demo.Zhihao.Server;

import java.io.IOException;


public class Main {
	public static void main(String [] args) throws IOException{
		Welcome welcome = new Welcome(Integer.parseInt(args[0]));
		Wel wel = new Wel(welcome);
		wel.start();
		Jabber jab = new Jabber(welcome);
		jab.start();
		Quit quit = new Quit(welcome);
		quit.start();
	}
}

class Wel extends Thread{
	Welcome welcome;
	public Wel(Welcome welcome) throws IOException{
		this.welcome = welcome;
	}
	public void run(){
		try {
			welcome.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Jabber extends Thread{
	Welcome welcome;
	public Jabber(Welcome welcome) throws IOException{
		this.welcome = welcome;
	}
	public void run(){
		try {
			welcome.messageProcess();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class Quit extends Thread{
	Welcome welcome;
	public Quit(Welcome welcome) throws IOException{
		this.welcome = welcome;
	}
	public void run(){
		try {
			while(welcome.isActive()){
				welcome.quit();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}