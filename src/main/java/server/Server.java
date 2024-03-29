package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;


public class Server {

	private ServerSocket server;
	private ArrayList<Connection> list;
	private Connection c = null;

	public Server (int port) {
		try {
			server = new ServerSocket(port);
			System.out.println("Server has been initialised on port " + port);
		}
		catch (IOException e) {
			System.err.println("error initialising server");
			e.printStackTrace();
		}
		list = new ArrayList<Connection>();
		while(true) {

				try {
					c = new Connection(server.accept(), this);
				}
				catch (IOException e) {
					System.err.println("error setting up new client connection");
					e.printStackTrace();
				}
				Thread t = new Thread(c);
				t.start();
				list.add(c);
		}
	}

	//Function to get connection id
	public String getConnectionid() {
		return c.toString();
	}

	// Function to broadcast message to all the connected users
	public void broadcastMessage(String theMessage){
		for( Connection clientThread: list){
			clientThread.sendMessages(theMessage);
		}
	}

	// Function to get the total of users online
	public int getNumberOfUsers() {
		return list.size();
	}
}
