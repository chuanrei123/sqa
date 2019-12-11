package server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private static ArrayList<String> usernameList = new ArrayList<>();
	private static ArrayList<String> connectionList = new ArrayList<>();
	private static String totalUsersHeader = "usersOnline:=>";
	
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

			// Function to broadcast connection id to newly connected client
			broadcastConnectionID();

			while(true) {
				String incomingMessage = dataInputStream.readUTF();
				decodeMessage(incomingMessage);
				System.out.println(incomingMessage);
			}
		} catch (IOException e) {
			removeUsers();
		}

	}

	public void decodeMessage(String incomingMsg) {
		String usernameHeader = "usernameHeader:=>";
		// If incoming message starts with "usernameHeader:=>" then means the
		// incoming message is username
		// else meaning that the incoming message is message from client
		if(incomingMsg.startsWith(usernameHeader)) {
			String[] removedHeader = incomingMsg.split("(usernameHeader:=>)");
			String username = removedHeader[1];
			usernameList.add(username);
			broadcastTotalUsers(username);
		} else if(incomingMsg.startsWith("msgHeader:=>")){
			String[] removeHeader = incomingMsg.split("(msgHeader:=>)");
			removeHeader = removeHeader[1].split("=>concat<=");
			String connectionId = removeHeader[0];
			String msgBody = removeHeader[1];
			String name = "";
			String encodedMsg = "";

			for (int i = 0; i< connectionList.size(); i++) {
				if(connectionList.get(i).matches(connectionId)) {
					name = usernameList.get(i);
					encodedMsg = "msgHeader:=>" + name + "=>concat<=" + msgBody;
				}
			}
			serverReference.broadcastMessage(encodedMsg);
		}
	}

	// send connection id to individual client
	public void broadcastConnectionID() {
		String connectionHeader = "connectionHeader:=>";
		connectionList.add(serverReference.getConnectionid());
		connectionHeader = connectionHeader + serverReference.getConnectionid();
		out.println(connectionHeader);
	}

	// Send all the online users to all the client
	public void broadcastTotalUsers(String username) {
		String concatHeader = "=>concat<=" + username;
		totalUsersHeader = totalUsersHeader + concatHeader;
		serverReference.broadcastMessage(totalUsersHeader);
	}

	public void sendMessages(String message) {
		out.println(message);
	}

	public void removeUsers() {
		Connection connection = this;
		String newOnlineUsers = "usersOnline:=>";
		int positionToRemove = 0;

		for (int i = 0; i< connectionList.size(); i++) {
			if(connectionList.get(i).matches(connection.toString())){
				connectionList.remove(i);
				positionToRemove = i;
				usernameList.remove(i);
			}
		}

		String[] removeHeader = totalUsersHeader.split("usersOnline:=>");
		removeHeader = removeHeader[1].split("=>concat<=");
		List<String> allUserOnline = Arrays.asList(removeHeader);

		for(int i = 1; i < allUserOnline.size(); i++) {
			if(i != positionToRemove + 1) {
				newOnlineUsers = newOnlineUsers + "=>concat<=" + allUserOnline.get(i);
			}
		}

		totalUsersHeader = newOnlineUsers;
		serverReference.broadcastMessage(totalUsersHeader);
	}
}

	