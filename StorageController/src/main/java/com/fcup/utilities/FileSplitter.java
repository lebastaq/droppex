package com.fcup.utilities;

import java.io.*;

public class FileSplitter {
    private final int BLOCK_SIZE_BYTES = 10 * 1024 * 1024;
    private final String TEMP_FILE_DIR = "tmp/";

    private final String filename;

    public FileSplitter(String filename) {
        this.filename = filename;
    }

    /**
     * Reads files with a buffer set to the max block size
     * and writes them out to the original video directory
     * https://stackoverflow.com/questions/10864317/how-to-break-a-file-into-pieces-using-java
     * @throws IOException
     */
    private void splitFile(String filename) throws IOException {
        byte[] blockBuffer = new byte[BLOCK_SIZE_BYTES];

        try (FileInputStream fi = new FileInputStream(new File(TEMP_FILE_DIR + filename));
             BufferedInputStream bi = new BufferedInputStream(fi)) {

            // Filename without extension
            String strippedFile = filename.substring(0, filename.lastIndexOf("."));

            int blockIndex = 0;

            int bytesRead = 0;
            while ((bytesRead = bi.read(blockBuffer)) > 0) {
                String blockName = TEMP_FILE_DIR + strippedFile + "/" + filename +  "." +  blockIndex;
                File currentBlock = new File(blockName);

                try (FileOutputStream fo = new FileOutputStream(currentBlock)) {
                    fo.write(blockBuffer, 0, bytesRead);
                    fo.close();

                    blockIndex++;
                }
            }
        }
    }
}
