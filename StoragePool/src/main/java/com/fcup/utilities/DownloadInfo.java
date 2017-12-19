package com.fcup.utilities;

import java.io.IOException;
import java.net.Socket;

public class DownloadInfo {
    String IP;
    int port;
    String chunkID;

    public DownloadInfo(String IP, int port, String chunkID) {
        this.IP = IP;
        this.port = port;
        this.chunkID = chunkID;
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
