package com.fcup.utilities;

import java.io.*;
import com.backblaze.erasure.*;

public class FileDecoder {
    private final int DATA_SHARDS = 4;
    private final int PARITY_SHARDS = 2;
    private final int TOTAL_SHARDS = 6;
    private final int BYTES_IN_INT = 4;
    private final String filename;

    private byte[][] shards;
    private boolean[] shardPresent;
    private int shardSize;
    private int shardCount;

    public FileDecoder(String filename) {
        this.filename = filename;
    }

    public void run() {

    }

    private void readShards(String filename, File shardsDir, int blockID) throws IOException {
        shards = new byte [TOTAL_SHARDS][];
        shardPresent = new boolean [TOTAL_SHARDS];
        shardSize = 0;
        shardCount = 0;

        // Read in any of the shards that are present.
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            File shardFile = new File(shardsDir,
                    filename + "." + blockID + "." +  i);

            if (shardFile.exists()) {
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
                    shardCount++;
                }
            }
        }

        fillEmpty();

    }

    private void reconstructFromParity(String filename, File shardsDir, int blockID) throws IOException {
        readShards(filename, shardsDir, blockID);

        // We need at least DATA_SHARDS to be able to reconstruct the file.
        if (shardCount < DATA_SHARDS) {
            throw new RuntimeException("Not enough shards to decode the file.");
        }

        // Use Reed-Solomon to fill in the missing shards
        ReedSolomon reedSolomon = ReedSolomon.create(DATA_SHARDS, PARITY_SHARDS);
        reedSolomon.decodeMissing(shards, shardPresent, 0, shardSize);

        saveDecoded(shardsDir, filename);
    }

    /**
     * Make empty buffers for the missing shards.
     */
    private void fillEmpty() {
        for (int i = 0; i < TOTAL_SHARDS; i++) {
            if (!shardPresent[i]) {
                shards[i] = new byte [shardSize];
            }
        }
    }

    private void saveDecoded(File shardsDir, String filename) throws IOException {
        // Write the decoded file
        File decoded = new File(shardsDir, filename);
        try (OutputStream out = new FileOutputStream(decoded)) {
            for (int i = 0; i < DATA_SHARDS; i++) {
                out.write(shards[i], BYTES_IN_INT, shardSize);

            }

            out.flush();

        }
        // TODO: Print the hash, but make sure the name you're getting is the right one
        //System.out.println(hashFile(decoded.getName()));
        System.out.println(decoded.getName());
    }
}
