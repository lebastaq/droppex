package com.fcup;

import com.fcup.utilities.ChunkSeeder;
import com.fcup.utilities.DownloadInfo;
import com.fcup.utilities.Downloader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class ChunkSeederTest {

    private final int BASE_PORT = 26000;
    private String chunkID = "chunkIDXYZ";
    private DownloadInfo client;
    private String STORAGE_FOLDER = "temp_stor";
    private String DOWNLOAD_FOLDER = "temp_down";

    @Before
    public void createTempFoldersAndDummyFile() {
        createTempFolder(STORAGE_FOLDER);
        createTempFolder(DOWNLOAD_FOLDER);
        fillFolderWithDummyFile(STORAGE_FOLDER, chunkID);
    }

    // TODO tidy this up
    @Test
    public void testChunkSeederAndDownloader() {
        try {
            client = new DownloadInfo("127.0.0.1", BASE_PORT, chunkID);
            Downloader downloader = new Downloader(DOWNLOAD_FOLDER, client);
            downloader.start();
            System.out.println("Downloader is running");

            ServerSocket serverSocket = new ServerSocket(BASE_PORT);
            System.out.println("Created serversocket");

            ChunkSeeder chunkSeeder = new ChunkSeeder(serverSocket.accept(), STORAGE_FOLDER);
            System.out.println("Accepted connection!");
            chunkSeeder.start();
            System.out.println("Chunk Seeder is running");

            chunkSeeder.join();
            downloader.join();
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Could not send and receive a chunk");
        }

        if(!filesAreEqual(DOWNLOAD_FOLDER + "/" + chunkID, STORAGE_FOLDER + "/" + chunkID)){
            fail("Did not download file correctly");
        }

    }

    private boolean filesAreEqual(String file1Name, String file2Name) {
        File file1 = new File(file1Name);
        File file2 = new File(file2Name);
        boolean isTwoEqual = false;

        try {
            isTwoEqual = FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isTwoEqual;
    }

    @After
    public void deleteTempFolders() {
        deleteFolderAndContents(STORAGE_FOLDER);
        deleteFolderAndContents(DOWNLOAD_FOLDER);
    }

    private void createTempFolder(String folderName) {
        File folder = new File(folderName);

        if (!folder.exists()) {
            try{
                folder.mkdir();
            }
            catch(SecurityException se){
                throw se;
            }
        }
    }

    private void fillFolderWithDummyFile(String folder, String chunkID)  {
        try {
            File out = new File(folder + "/" + chunkID);
            FileWriter fw = null;
            fw = new FileWriter(out);
            BufferedWriter writer = new BufferedWriter(fw);

            for(int i = 0; i < 50; i++) {
                writer.write("dummy content");
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFolderAndContents(String folderName) {
        try {
            FileUtils.deleteDirectory(new File(folderName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}