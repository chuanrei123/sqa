package client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Read implements Runnable  {

    private Socket socket;
    public BufferedReader reader;

    public Read(Socket socket) {
        this.socket = socket;

        try {
            InputStream inputStream = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
