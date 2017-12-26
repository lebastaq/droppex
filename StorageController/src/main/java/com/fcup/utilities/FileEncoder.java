package com.fcup.utilities;

import java.io.*;
import java.nio.ByteBuffer;

import com.backblaze.erasure.*;

public class FileEncoder {

    // TODO: Extract later, duplicated
    public final int DATA_SHARDS = 4;
    public final int PARITY_SHARDS = 2;
    public final int TOTAL_SHARDS = 6;
    public final int BYTES_IN_INT = 4;

    private byte [][] shards;
    private int shardSize;

    // TODO: Increase block size to 10MB
    private final int BLOCK_SIZE_BYTES = 1024 * 1024;
    
    private final String fileDirectory;
    private final String filename;
    private final String token;

    private File blockDirectory;

    public FileEncoder(String fileDirectory, String filename, String token) {
        this.fileDirectory = fileDirectory;
        this.filename = filename;
        this.token = token;

    }

    public File run() {
        blockDirectory = makeTempDirectory();

        try {
            splitFile();
            encodeBlocks();
            return blockDirectory;

        } catch (IOException e) {
            e.printStackTrace();

            // Clean up temp directory
            if (blockDirectory != null && blockDirectory.exists()) {
                String[] files = blockDirectory.list();
                for(String f: files){
                    File currentFile = new File(blockDirectory.getPath(), f);
                    currentFile.delete();
                }

                blockDirectory.delete();
            }

            return null;

        }
    }

    /**
     * Reads file and splits into blocks
     * @throws IOException
     */
    private File splitFile() throws IOException {
        byte[] blockBuffer = new byte[BLOCK_SIZE_BYTES];

        File originalFile = new File(fileDirectory + filename);
        try (FileInputStream fi = new FileInputStream(originalFile);
             BufferedInputStream bi = new BufferedInputStream(fi)) {

            int blockIndex = 0;
            int bytesRead = 0;
            while ((bytesRead = bi.read(blockBuffer)) > 0) {
                String blockName = blockDirectory.getPath() + "/" + filename +  "." +  blockIndex;

                File currentBlock = new File(blockName);

                try (FileOutputStream fo = new FileOutputStream(currentBlock)) {
                    fo.write(blockBuffer, 0, bytesRead);
                    fo.close();

                    blockIndex++;
                }
            }
        } catch (IOException e) {
            // Clean up temp dir if there's an exception
            blockDirectory.delete();
            throw e;

        } finally {
            // Clean up original file on error or success
            originalFile.delete();

        }

        return blockDirectory;

    }

    private File makeTempDirectory() {
        String blockPath = fileDirectory
                + token.substring(8, 72)
                + Long.toString(System.currentTimeMillis());

        File blockDirectory = new File(blockPath);
        blockDirectory.mkdir();

        return blockDirectory;
    }

    private void encodeBlocks() throws IOException {
        int blockCount = blockDirectory.list().length;
        for (int i = 0; i < blockCount; i++) {
            String blockPath = blockDirectory.getPath() + "/" + filename + "." + i;
            encodeBlock(blockPath);

        }
    }

    private void encodeBlock(String blockName) throws IOException {
        File block = new File(blockName);
        generateShards(block);

        // Use Reed-Solomon to calculate the parity.
        ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.encodeParity(shards, 0, shardSize);

        saveShards(block);
    }

    private void generateShards(File block) throws IOException {
        int blockSize = (int)block.length();
        int storedSize = blockSize + BYTES_IN_INT;

        shardSize = (storedSize + DATA_SHARDS - 1) / DATA_SHARDS;
        shards = new byte [TOTAL_SHARDS][shardSize];

        final int bufferSize = shardSize * DATA_SHARDS;
        final byte [] allBytes = new byte[bufferSize];
        ByteBuffer.wrap(allBytes).putInt(blockSize);

        try (InputStream in = new FileInputStream(block)) {
            int bytesRead = in.read(allBytes, BYTES_IN_INT, blockSize);

            if (bytesRead != blockSize) {
                throw new IOException("Not all bytes read from block: " + block.getName());
            }
        }

        // Fill in the data shards
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(allBytes, i * shardSize, shards[i], 0, shardSize);
        }
    }

    private void saveShards(File block) throws IOException {
        // Write out the resulting files.
        for (int i = 0; i < TOTAL_SHARDS; i++) {

            File outputFile = new File(block.getParentFile(), block.getName() + "." + i);
            try(OutputStream out = new FileOutputStream(outputFile)) {
                out.write(shards[i]);

            }
        }

        block.delete();
    }
}
