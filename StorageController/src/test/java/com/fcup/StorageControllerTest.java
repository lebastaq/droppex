package com.fcup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.jgroups.util.Util.assertEquals;
import static org.junit.Assert.fail;

public class StorageControllerTest {
    private StorageController storageController;

    @Before
    public void createStorageController() {
        try {
            storageController = new StorageController();
        } catch (Exception e) {
            System.err.println("Could not create storage controller:");
            e.printStackTrace();
        }
    }

    // should be getAnswer at the same time as a storage pool
    // TODO finish it...
    @Test
    public void startDownloaderInStoragePool()  {
        try {
            storageController.makeStoragePoolDownloadAFileFromTheAppServer();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not make storage pool download a file");
        }
    }

    @Test
    public void testElectionOfLeader() throws Exception {
        StorageController storageController2 = new StorageController();
        try {
            storageController.connectToChannel();
            storageController.sync();
            storageController2.connectToChannel();
            storageController2.sync();
            if(!storageController.isLeader || storageController2.isLeader)
                fail("Did not assign leader correctly");

            storageController.jgroupsChannel.close();

            if(!storageController2.isLeader)
                fail("Did not change leader correctly");

        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception");
        }
        storageController2.disconnectFromChannel();
    }

    @Test
    public void doOperationIntoDBAndThenLoadIt() throws Exception {
        Shard shard = new Shard();
        DbManager dbManager = new DbManager();

        dbManager.connect();

        storageController.connectToChannel();
        storageController.doOperation(shard);

        storageController.shardManager.shards = new LinkedList<>();
        storageController.shardManager.loadLocalOperationsFromDB();

        List<Shard> operationsExpected;
        operationsExpected = dbManager.readEntries();

        if(storageController.shardManager.shards.size() != operationsExpected.size())
        {
            fail("Stored " + storageController.shardManager.shards.size() + " shards from db instead of " + operationsExpected.size());
        }
    }


    @After
    public void disconnectFromChannel() {
        storageController.disconnectFromChannel();
    }

}