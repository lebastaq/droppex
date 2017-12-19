package com.fcup;

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

    @Test
    public void createStorageFolderIfNotExists() throws Exception {

    }

}