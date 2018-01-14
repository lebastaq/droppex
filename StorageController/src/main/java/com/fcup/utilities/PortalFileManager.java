package com.fcup.utilities;

import com.fcup.Shard;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;

public class PortalFileManager implements Runnable {
    private final String TEMP_FILE_DIR = "tmp/";
    private String POOL_IP = "10.132.0.8";

    private final int POOL_RECEIVING_PORT = 26001;
    private final int POOL_SENDING_PORT = 26002;

    private String filename;
    private String fileHash;
    private String token;

    private final Socket socket;

    public PortalFileManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("A connection was made");
        try(DataInputStream dis = new DataInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = dis.readLine();
            filename = dis.readLine();

            if (action.equals("upload")) {
                receivePortalUpload(dis);

                if (validUpload()) {
                    FileEncoder uh = new FileEncoder(TEMP_FILE_DIR, filename, token);
                    File shardDirectory = uh.run();

                    distributeShards(shardDirectory);

                    String output = (shardDirectory != null) ? "SUCCESS"
                                                             : "FAILED";

                    out.println(output);

                }

            } else if (action.equals("download")) {
                receiveShards();

                FileDecoder dh = new FileDecoder(filename, new File (TEMP_FILE_DIR));
                dh.run();

                System.out.println(filename + " decoded in StorageController");
                sendPortalDownload(os);

            }

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
    }

    private void receivePortalUpload(DataInputStream dis) throws IOException {
        fileHash = dis.readLine();
        token = dis.readLine();

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

    private void sendPortalDownload(OutputStream os) throws IOException {

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

        outgoingFile.delete();
    }

    private boolean validUpload() throws IOException, NoSuchAlgorithmException {
        String downloadedHash = FileHasher.hashFile(TEMP_FILE_DIR + filename);

        return downloadedHash.equals(fileHash);
    }

    private void distributeShards(File shardDirectory) {
        System.out.println("Uploading " + filename + " shards to pools.");

        // Loop through each shard and send it over
        for (File shard : shardDirectory.listFiles()) {

            // TODO: Remove hard-coded address
            try (Socket clientSocket = new Socket(POOL_IP, POOL_SENDING_PORT);
                 OutputStream os = clientSocket.getOutputStream();
                 PrintWriter out = new PrintWriter(os, true);
                 FileInputStream fis = new FileInputStream(shard);
                 FileChannel ch = fis.getChannel()) {

                // Send shardID to upload
                out.println(shard.getName());

                long fileLength = shard.length();
                int bytesRead;
                ByteBuffer buffer = ByteBuffer.allocate((int)fileLength);

                while ((bytesRead = ch.read(buffer)) > 0){
                    os.write(buffer.array(), 0, bytesRead);
                }

                os.flush();

            } catch(IOException  e) {
                e.printStackTrace();

            }
        }
    }

    private void receiveShards() {
        System.out.println("Receiving " + filename + " shards from pools.");

        // TODO: Query unique list of shards
        Shard[] shards = new Shard[];

        for (Shard shard : shards) {
            String shardID = shard.getId();

            // TODO: Remove hard-coded address
            try (Socket clientSocket = new Socket(POOL_IP, POOL_RECEIVING_PORT);
                 OutputStream os = clientSocket.getOutputStream();
                 PrintWriter out = new PrintWriter(os, true);
                 DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                 FileOutputStream fos = new FileOutputStream(TEMP_FILE_DIR + "/" + shardID)) {

                out.println(shardID);

                byte[] contents = new byte[1024*1024]; // 1MB
                int bytesRead;

                while ((bytesRead = dis.read(contents)) > 0) {
                    fos.write(contents, 0, bytesRead);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
