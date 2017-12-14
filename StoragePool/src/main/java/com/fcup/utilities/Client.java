package com.fcup.utilities;

import java.io.IOException;
import java.net.Socket;

public class Client {
    String IP;
    int port;

    public Client(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    Socket connectToSocket() {
        boolean scanning = true;
        Socket socket = null;
        int numberOfTries = 0;

        while (scanning && numberOfTries < 5){
            numberOfTries++;
            try {
                socket = new Socket(IP, port);
                scanning = false;
            } catch (IOException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        return socket;
    }
}
