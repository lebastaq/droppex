package com.fcup.utilities;

import java.io.*;
import java.net.Socket;


// TODO create super class for chunk seeder/downloader ?
public class ChunkDownloader extends Thread{
    private final Socket socket;
    private final String STORAGE_FOLDER;
    String chunkID;

    public ChunkDownloader(Socket socket, String STORAGE_FOLDER) {
        this.socket = socket;
        this.STORAGE_FOLDER = STORAGE_FOLDER;
    }

    public void run() {
        try {
            createDownloadsDirIfNotExists();
            readFileFromBuffer();
            closeSocket();
            System.out.println("Finished downloading " + chunkID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO move this to main class StoragePool
    private void createDownloadsDirIfNotExists() throws FileNotFoundException {
        File theDir = new File(STORAGE_FOLDER);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
    }

    private void readFileFromBuffer() throws IOException {
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            chunkID = dis.readLine();
            System.out.println("ChunkDownloading: Downloading chunkID: " + chunkID);

            FileOutputStream fos = new FileOutputStream(STORAGE_FOLDER + "/" + chunkID);

            byte[] contents = new byte[1024*1024]; // TODO change this ?
            int bytesRead = 0;
            while ((bytesRead = dis.read(contents)) > 0) {
                fos.write(contents, 0, bytesRead);
            }

            fos.flush();
            fos.close();
            dis.close();

        } catch(Exception e) {
            System.out.println("Unexpected exception: ");
            e.printStackTrace();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
