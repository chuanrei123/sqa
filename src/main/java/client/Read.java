package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Read implements Runnable  {

    private Socket socket;
    private ClientServer clientServer;
    public BufferedReader reader;
    public Controller controller;

    public Read(Socket socket, ClientServer clientServer, Controller controller) {
        this.socket = socket;
        this.clientServer = clientServer;
        this.controller = controller;

        try {
            InputStream inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String incomingMsg = reader.readLine();
                decodeMsg(incomingMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decodeMsg(String incomingMsg) {
        if(incomingMsg.startsWith("connectionHeader:=>")) {
            String[] removeHeader = incomingMsg.split("(connectionHeader:=>)");
            clientServer.setConnectionID(removeHeader[1]);
        } else if(incomingMsg.startsWith("usersOnline:=>")){
            String[] removeHeader = incomingMsg.split("(usersOnline:=>)");
            removeHeader = removeHeader[1].split("=>concat<=");
            List<String> allUserOnline = Arrays.asList(removeHeader);
            controller.updateOnlineUser(allUserOnline);
        } else {

        }
    }
}
