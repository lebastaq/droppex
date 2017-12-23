package com.fcup.utilities;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class PortalFileHandler implements Runnable {
    private final String TEMP_FILE_DIR = "tmp/";
    private final Socket socket;

    public PortalFileHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(DataInputStream dis = new DataInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = dis.readLine();
            String filename = dis.readLine();

            if (action.equals("upload")) {
                receiveUpload(filename, dis);

            } else if (action.equals("download")) {
                sendDownload(os, filename);

            } else {
                out.println("Error: Operation not supported. Need 'upload' or 'download'");
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void receiveUpload(String filename, DataInputStream dis) throws IOException {

        try (FileOutputStream fos = new FileOutputStream(TEMP_FILE_DIR + filename)) {

            byte[] contents = new byte[1024];

            int bytesRead;
            while ((bytesRead = dis.read(contents)) > 0) {
                fos.write(contents, 0, bytesRead);

            }

        } catch (IOException e) {
            throw e;
        }
    }

    private void sendDownload(OutputStream os, String filename) throws IOException {
        /*
        TODO:
        Get the shards from the Storage Pools and encode them before continuing
        Save the encoded file to TEMP_FILE_DIR
         */

        File outgoingFile = new File(TEMP_FILE_DIR + filename);

        try(FileInputStream fis = new FileInputStream(outgoingFile);
            FileChannel ch = fis.getChannel()) {

            long fileLength = outgoingFile.length();
            ByteBuffer buffer = ByteBuffer.allocate((int)fileLength);

            int bytesRead;
            while ((bytesRead = ch.read(buffer)) > 0){
                os.write(buffer.array(), 0, bytesRead);

            }

            os.flush();

        } catch (IOException e) {
            throw e;

        }
    }

}
