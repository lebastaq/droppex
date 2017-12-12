package com.fcup;

import java.io.*;
import java.net.Socket;

public class ChunkSeeder extends Thread{
    private final Socket socket;
    private final Object STORAGE_FOLDER;
    String chunkName;
    File file;

    public ChunkSeeder(Socket socket, String STORAGE_FOLDER) {
        this.socket = socket;
        this.STORAGE_FOLDER = STORAGE_FOLDER;
    }

    public void run() {
        try {
            openFile();
            readFileName();
            writeFileIntoBuffer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void readFileName() throws IOException {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        chunkName = in.readLine();
        System.out.println("Requested chunkName" + chunkName);
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
    }

    private void openFile() throws FileNotFoundException {
        file = new File(STORAGE_FOLDER + chunkName);
        if(!file.exists()) {
            System.err.println("File not found: storage/" + chunkName);
            throw new FileNotFoundException();
        }
    }
}
