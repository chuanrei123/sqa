package server;

import java.io.*;
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
	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	
	Connection (Socket client, Server serverReference) {
		this.serverReference = serverReference;
		this.client = client;
		try {
			this.dataInputStream = new DataInputStream(client.getInputStream());
			this.dataOutputStream = new DataOutputStream(client.getOutputStream());
		} catch  (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void run(){
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			while(true) {
				String incomingMessage = dataInputStream.readUTF();
				System.out.println(incomingMessage);
			}
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}

	}
	
	public String getUserName() {
		return username;
	}

	public void sendMessages(String message) {
		out.println(message);
	}
	
}

	