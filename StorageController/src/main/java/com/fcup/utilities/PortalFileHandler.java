package com.fcup.utilities;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class PortalFileHandler implements Runnable {
    // TODO: Increase block size to 10MB
    private final int BLOCK_SIZE_BYTES = 1024 * 1024;
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
            String fileHash = dis.readLine();
            String token = dis.readLine();

            if (action.equals("upload")) {
                receiveUpload(filename, dis);
                splitFile(filename, fileHash, token);

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

    /**
     * Reads files with a buffer set to the max block size
     * and writes them out to the original video directory
     * https://stackoverflow.com/questions/10864317/how-to-break-a-file-into-pieces-using-java
     * @throws IOException
     */
    private void splitFile(String filename, String filehash, String token) throws IOException {
        byte[] blockBuffer = new byte[BLOCK_SIZE_BYTES];

        File baseFile = new File(TEMP_FILE_DIR + filename);
        try (FileInputStream fi = new FileInputStream(baseFile);
             BufferedInputStream bi = new BufferedInputStream(fi)) {

            System.out.println(filehash);
            System.out.println(System.currentTimeMillis());

            // Make a temp directory for shards
            String blockPath = TEMP_FILE_DIR + filehash + token.substring(8,18) + Long.toString(System.currentTimeMillis());
            System.out.println(blockPath);
            File pathFile = new File(blockPath);
            pathFile.mkdir();

            int blockIndex = 0;
            int bytesRead = 0;
            while ((bytesRead = bi.read(blockBuffer)) > 0) {
                String blockName = blockPath + "/" + filename +  "." +  blockIndex;

                File currentBlock = new File(blockName);

                try (FileOutputStream fo = new FileOutputStream(currentBlock)) {
                    fo.write(blockBuffer, 0, bytesRead);
                    fo.close();

                    blockIndex++;
                }
            }

            // TODO: Clean up temp folder after successful transfer
//            pathFile.delete();
        }

        baseFile.delete();

    }

}
