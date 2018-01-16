package com.fcup.utilities;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.backblaze.erasure.*;

public class FileDecoder {
    private final int DATA_SHARDS = 4;
    private final int PARITY_SHARDS = 2;
    private final int TOTAL_SHARDS = 6;
    private final int BYTES_IN_INT = 4;
    private final String filename;
    private final File shardsDir;

    private final byte[][] shards = new byte [TOTAL_SHARDS][];
    private final boolean [] shardPresent = new boolean [TOTAL_SHARDS];
    private int shardSize;

    public FileDecoder(String filename, File shardsDir) {
        this.filename = filename;
        this.shardsDir = shardsDir;
    }

    public void run() throws IOException {
        assembleBlocks();
        assembleFile();

    }

    private void assembleFile() throws IOException {
        File[] directoryFiles = shardsDir.listFiles();
        Arrays.sort(directoryFiles);

        try (FileOutputStream fos = new FileOutputStream(shardsDir.getPath() + "/" + filename);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {

            for (File block : directoryFiles) {
                if (block.isDirectory()) continue;

                Files.copy(block.toPath(), mergingStream);
                block.delete();

            }

        } catch (IOException e) {
            throw e;
        }
    }

    private void assembleBlocks() throws IOException {
        String[] shards = shardsDir.list();

        Set<String> blocks = new HashSet<>();

        for (String shard : shards) {
            if (shard.indexOf(filename) != -1) {
                String block = shard.substring(0, shard.lastIndexOf('.'));

                System.out.println("Block identified: " + block);

                blocks.add(block);
            }
        }

        for (String block : blocks) {
            assembleSublocks(block);

        }
    }

    private void assembleSublocks(String blockName) throws IOException {
        readShards(blockName);

        // Fill in missing shards
        ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);

        saveFiles(blockName);
    }

    private void readShards(String blockName) throws IOException {
        int shardCount = 0;
        for (int i = 0; i < TOTAL_SHARDS; i++) {

            File shardFile = new File(shardsDir,blockName + "." + i);
            if (shardFile.exists()) {
                System.out.println(String.format("Shard: %s exists. (%d bytes)", shardFile.getName(), (int) shardFile.length()));

                // Store the size of the first shard
                if (i == 0)  {
                    shardSize = (int) shardFile.length();

                // Verify that the sizes of all shards match
                } else if (shardSize != (int) shardFile.length()) {
                    throw new RuntimeException("Shards are not of equal sizes.");

                }

                shards[i] = new byte [shardSize];
                shardPresent[i] = true;

                try (InputStream in = new FileInputStream(shardFile)) {
                    in.read(shards[i], 0, shardSize);
                }

                shardCount++;

                shardFile.delete();
            }
        }

        System.out.println("shardCount: " + shardCount + " DATA_SHARDS: " + DATA_SHARDS);

        // We need at least DATA_SHARDS to be able to reconstruct the file.
        checkShardCount(shardCount);
        fillEmptyShardBuffers();
    }

    private void checkShardCount(int shardCount) throws IOException {
        if (shardCount < DATA_SHARDS) {
            throw new IOException("Not enough shards present");
        }
    }

    private void fillEmptyShardBuffers() {
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            if (!shardPresent[i]) {
                shards[i] = new byte [shardSize];
            }
        }
    }

    private void saveFiles(String blockName) throws IOException {
        // Combine the data shards into one buffer for convenience.
        byte[] allBytes = new byte [shardSize * DATA_SHARDS];
        for (int i = 0; i < DATA_SHARDS; i++) {
            System.arraycopy(shards[i], 0, allBytes, shardSize * i, shardSize);
        }

        // Extract the file length
        int fileSize = ByteBuffer.wrap(allBytes).getInt();

        // Write the decoded file
        String outputPath = shardsDir.getPath() + "/" + blockName;
        try (OutputStream out = new FileOutputStream(outputPath)) {
            out.write(allBytes, BYTES_IN_INT, fileSize);
        }
    }
}
