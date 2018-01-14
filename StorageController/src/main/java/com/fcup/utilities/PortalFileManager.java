package com.fcup.utilities;

import com.fcup.DbManager;
import com.fcup.Shard;
import com.fcup.StorageController;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class PortalFileManager implements Runnable {
    private final String TEMP_FILE_DIR = "tmp/";

    private final int POOL_RECEIVING_PORT = 26001;
    private final int POOL_SENDING_PORT = 26002;

    private String filename;
    private String fileHash;
    private String token;
    private int fileSize;

    private final Socket socket;

    public PortalFileManager(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("A connection was made");
        verifyTempDir();

        try(DataInputStream dis = new DataInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true)) {

            String action = dis.readLine();
            filename = dis.readLine();

            if (action.equals("upload")) {
                receivePortalUpload(dis);

                if (validUpload()) {
                    storeSize();
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

        } catch (IOException | NoSuchAlgorithmException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }

    private void storeSize() {
        File uploaded = new File(TEMP_FILE_DIR + "/" + filename);
        fileSize = (int)uploaded.length();
    }

    private void verifyTempDir() {
        File tempDir = new File(TEMP_FILE_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdir();
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

        System.out.println("Received " + filename);

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

        System.out.println("Finished seeding file " + filename);

        outgoingFile.delete();
    }

    private boolean validUpload() throws IOException, NoSuchAlgorithmException {
        String downloadedHash = FileHasher.hashFile(TEMP_FILE_DIR + filename);

        return downloadedHash.equals(fileHash);
    }

    private void distributeShards(File shardDirectory) throws IOException {
        System.out.println("Uploading " + filename + " shards to pools.");
        StorageController sc = StorageController.getController();

        // Loop through each shard and send it over
        for (File shardFile : shardDirectory.listFiles()) {

            // Get target pool id from hash function
            String shardID = shardFile.getName();
            int poolID = ShardDispatcher.idToIndex(shardID, sc.storagePools.size());
            StoragePool targetPool = sc.storagePools.get(poolID);

            System.out.println("Sending " + shardID + " to pool " + targetPool.getIp());

            try (Socket clientSocket = new Socket(targetPool.getIp(), POOL_SENDING_PORT);
                 OutputStream os = clientSocket.getOutputStream();
                 PrintWriter out = new PrintWriter(os, true);
                 FileInputStream fis = new FileInputStream(shardFile);
                 FileChannel ch = fis.getChannel()) {

                // Send shardID to upload
                out.println(shardID);

                long fileLength = shardFile.length();
                int bytesRead;
                ByteBuffer buffer = ByteBuffer.allocate((int)fileLength);

                while ((bytesRead = ch.read(buffer)) > 0){
                    os.write(buffer.array(), 0, bytesRead);
                }

                os.flush();

            } catch(Exception e) {
                System.err.println("Exception when sending file: ");
                e.printStackTrace();
                sc.removeUnavailableStoragePool(targetPool);

            } finally {
                // TODO: Cleanup
            }

            System.out.println("Done sending " + shardID + " to pool " + targetPool.getIp());

            // Notify other controllers of operation after successful transfer to pool
            Shard currentShard = buildShard(shardFile.getName(), targetPool);
            sc.sendMessage(currentShard);

        }
    }

    private Shard buildShard(String shardID, StoragePool targetPool) {
        Shard currentShard = new Shard();
        currentShard.changeKeyValue("shardID", shardID);
        currentShard.changeKeyValue("filename", filename);
        currentShard.changeKeyValue("fileSize", Integer.toString(fileSize));
        currentShard.changeKeyValue("ip", targetPool.getIp());

        return currentShard;
    }

    private void receiveShards() throws SQLException, ClassNotFoundException {
        System.out.println("Receiving " + filename + " shards from pools.");

        DbManager db = new DbManager();
        db.connect();
        List<Shard> shards = db.readEntries();

        for (Shard shard : shards) {
            String shardID = shard.getId();

            try (Socket clientSocket = new Socket(shard.getIP(), POOL_RECEIVING_PORT);
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
