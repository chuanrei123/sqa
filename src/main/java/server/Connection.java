package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {
	
	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;
	
	private volatile boolean running;
	private int messageCount;
	private int state;
	private Socket client;
	private Server serverReference;
	private BufferedReader in;
	private PrintWriter out;
	private String username;
	
	Connection (Socket client, Server serverReference) {
		this.serverReference = serverReference;
		this.client = client;
		this.state = STATE_UNREGISTERED;
		messageCount = 0;
	}
	
	public void run(){
		String line;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		running = true;
		this.sendOverConnection("OK Welcome to the chat server, there are currelty " + serverReference.getNumberOfUsers() + " user(s) online");
		while(running) {
			try {
				line = in.readLine();
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}
	}


	public boolean isRunning(){
		return running;
	}
	
	private synchronized void sendOverConnection (String message){
		out.println(message);
	}
	
	public void messageForConnection (String message){
		sendOverConnection(message);
	}
	
	public int getState() {
		return state;
	}
	
	public String getUserName() {
		return username;
	}
	
}

	