package com.fcup;

import com.fcup.utilities.Downloader;
import org.junit.Test;

import static org.junit.Assert.*;

public class StoragePoolTest {
    private StoragePool storagePool;

    @Test
    public void createStoragePool() {
        try {
            storagePool = new StoragePool();
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Could not create storage pool !");
        }
    }

    // run this with netcat
    // TODO better tests ?
//    @Test
//    public void testUploadDownload() throws Exception {
//        String STORAGE_FOLDER = "downloads";
//        DownloaderPool downloaderPool = new DownloaderPool(STORAGE_FOLDER);
//        SeederPool seederPool = new SeederPool(STORAGE_FOLDER);
//
//        downloaderPool.run();
//        seederPool.run();
//
//        System.in.read();
//
//    }

}