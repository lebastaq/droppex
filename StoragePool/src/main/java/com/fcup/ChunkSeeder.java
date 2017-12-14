package com.fcup;

import java.io.*;
import java.net.Socket;

public class ChunkSeeder extends Thread{
    private final Socket socket;
    private final Object STORAGE_FOLDER;
    String chunkID;
    File file;

    public ChunkSeeder(Socket socket, String STORAGE_FOLDER) {
        this.socket = socket;
        this.STORAGE_FOLDER = STORAGE_FOLDER;
    }

    public void run() {
        try {
            readChunkId();
            openChunkFile();
            writeFileIntoBuffer();
            closeSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void readChunkId() throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        chunkID = in.readLine();
        System.out.println("ChunkSeeder: Requested chunkID: " + chunkID);
    }

    private void openChunkFile() throws FileNotFoundException {
        file = new File(STORAGE_FOLDER + "/" + chunkID);
        if(!file.exists()) {
            System.err.println("File not found:" + STORAGE_FOLDER + "/" + chunkID);
            throw new FileNotFoundException();
        }
    }

    private void writeFileIntoBuffer() throws IOException {
        OutputStream os = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        long fileLength = file.length();

        byte[] contents = new byte[(int)fileLength];
        int bytesRead;

        while((bytesRead = bis.read(contents)) > 0){
            os.write(contents, 0, bytesRead);
        }
        os.flush();
        fis.close();
        bis.close();
        System.out.println("ChunkSeeder: finished seeding file");
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
