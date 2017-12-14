package com.fcup.Utilities;

import java.io.*;
import java.net.Socket;

public class Downloader extends Thread {

    private Client seeder;
    private final String chunkID;
    private final String STORAGE_FOLDER;


    // todo something about these 3 parameters in the constructor
    public Downloader(String STORAGE_FOLDER, Client client, String chunkID) {
        // TODO
        this.STORAGE_FOLDER = STORAGE_FOLDER;
        this.seeder = client;
        this.chunkID = chunkID;
    }

    @Override
    public void run() {
        try {
            downloadFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Downloader: finished downloading file");
    }

    private void downloadFile() throws Exception {
        System.out.println("Downloader: Downloading file " + chunkID + " - connecting to seeder...");
        Socket clientSocket = seeder.connectToSocket();

        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fos = new FileOutputStream(STORAGE_FOLDER + "/" + chunkID);

        out.println(chunkID);

        byte[] contents = new byte[1024 * 1024]; // 10MB
        int bytesRead = 0;
        while ((bytesRead = dis.read(contents)) > 0) {
            fos.write(contents, 0, bytesRead);
        }

        fos.flush();
        fos.close();
        dis.close();
    }
}
