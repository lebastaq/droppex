package com.fcup.utilities;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;

public class PortalFileManager implements Runnable {
    private final String TEMP_FILE_DIR = "tmp/";

    private String filename;
    private String fileHash;
    private String token;

    private final Socket socket;

    public PortalFileManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(DataInputStream dis = new DataInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = dis.readLine();
            filename = dis.readLine();

            if (action.equals("upload")) {
                receiveUpload(dis);

                if (validUpload()) {
                    FileEncoder uh = new FileEncoder(TEMP_FILE_DIR, filename, token);
                    File shardDirectory = uh.run();

                    // TODO: Handle this output on the App server side
                    String output = (shardDirectory != null) ? "SUCCESS"
                                                             : "FAILED";

                    out.println(output);

                }

            } else if (action.equals("download")) {
                // TODO: This needs a real directory path rather than "." (Where we receive shards from pools)
                FileDecoder dh = new FileDecoder(filename, new File ("."));
                dh.run();

                sendDownload(os);

            }

        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
    }

    private void receiveUpload(DataInputStream dis) throws IOException {
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

    private void sendDownload(OutputStream os) throws IOException {
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

    private boolean validUpload() throws IOException, NoSuchAlgorithmException {
        String downloadedHash = FileHasher.hashFile(TEMP_FILE_DIR + filename);

        return downloadedHash.equals(fileHash);
    }
}
