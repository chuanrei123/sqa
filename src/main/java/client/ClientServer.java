package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientServer {

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream output = null;
    public ObjectOutputStream oos;
    public Read readThread;
    public String connectionID;
//    private ChatServer server = null;

    public ClientServer (String address, int port, String loginUsername, Controller controller) {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server at port: " + port);

            writeUsernameToServer(loginUsername);
            // Thread to listen to incoming messages
            readThread = new Read(socket, this, controller);
            Thread t = new Thread(readThread);
            t.start();
        } catch (Exception u) {
            System.out.println("Unable to connect to server");
        }
    }

    // Function to write username to the server
    public void writeUsernameToServer(String username) {
        String usernameHeader = "usernameHeader:=>";
        String encodedUsernameString = usernameHeader + username;

        try {
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(encodedUsernameString);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Function to write message to the server
    public void writeMessage(String msgString) {
        if(msgString != "") {
            try {
                output = new DataOutputStream(socket.getOutputStream());
                output.writeUTF(msgString);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void setConnectionID(String connectionID) {
        this.connectionID = connectionID;
    }

    public String getConnectionID() {
        return this.connectionID;
    }
}
