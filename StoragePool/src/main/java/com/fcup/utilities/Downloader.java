package com.fcup.utilities;

import java.io.*;
import java.net.Socket;

public class Downloader extends Thread {

    private Download info;
    private final String STORAGE_FOLDER;


    public Downloader(String STORAGE_FOLDER, Download seeder) {
        // TODO
        this.STORAGE_FOLDER = STORAGE_FOLDER;
        this.info = seeder;
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
        System.out.println("Downloader: Downloading file " + info.chunkID + " - connecting to seeder...");
        Socket clientSocket = info.connectToSocket();

        PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
        FileOutputStream fos = new FileOutputStream(STORAGE_FOLDER + "/" + info.chunkID);

        out.println(info.chunkID);

        byte[] contents = new byte[1024]; // 1MB
        int bytesRead = 0;
        while ((bytesRead = dis.read(contents)) > 0) {
            fos.write(contents, 0, bytesRead);
        }

        fos.flush();
        fos.close();
        dis.close();
    }
}
