package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.jgroups.util.Util.assertEquals;
import static org.junit.Assert.*;

public class StoragePoolsManagerTest {
    private StoragePoolsManager storagePoolsManager;

    @Before
    public void initStoragePool() {
        storagePoolsManager = createStoragePool();
    }

    private StoragePoolsManager createStoragePool() {
        StoragePoolsManager storagePoolManagerLocal = null;
        try {
            storagePoolManagerLocal = new StoragePoolsManager();
            System.out.println("OK");
        } catch (Exception e) {
            System.err.println("Could not create storage controller:");
            e.printStackTrace();
        }

        return storagePoolManagerLocal;
    }

    @Test
    public void syncOperations() throws Exception {
        try{
            storagePoolsManager.connectToChannel();
            storagePoolsManager.shardManager.syncOperations(new ArrayList<String>());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Could not sync shards");
        }
    }

    @Test
    public void viewAccepted() {
        List<String> expectedNewOperations = new LinkedList<>();

        try {
            storagePoolsManager.connectToChannel();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not connect to jgroupsChannel");
        }

        assertEquals(storagePoolsManager.shardManager.shards, expectedNewOperations);
    }

    @Test
    public void retrieveStateWithoutException() throws Exception {
        StoragePoolsManager storagePoolsManager2 = createStoragePool();
        try {
            this.storagePoolsManager.connectToChannel();
            this.storagePoolsManager.sync();
            // TODO something with the IP adress...
            storagePoolsManager2.connectToChannel();
            storagePoolsManager2.sync();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not sync state");
        }
        storagePoolsManager2.disconnectFromChannel();
    }

    @Test
    public void syncLocalStoragePools() throws Exception {
        Shard shard1 = new Shard();
        shard1.changeKeyValue("storagePoolIP", "ip1");
        Shard shard2 = new Shard();
        shard2.changeKeyValue("storagePoolIP", "ip2");

        storagePoolsManager.shardManager.syncOperation(shard1.asJSONString());
        storagePoolsManager.shardManager.syncLocalPoolsWithOperationPool(storagePoolsManager.storagePools, shard1);

        if (storagePoolsManager.storagePools.size() == 1) {
            if (!storagePoolsManager.storagePools.get(0).hasNShards(1)) {
                fail("Did not insert correct number of shards!");
            }
        }
        else
            fail("Did not insert first storage pool: size = " + storagePoolsManager.storagePools.size());

        storagePoolsManager.shardManager.syncOperation(shard2.asJSONString());
        storagePoolsManager.shardManager.syncLocalPoolsWithOperationPool(storagePoolsManager.storagePools, shard2);

        if (storagePoolsManager.storagePools.size() == 1) {
            if (!storagePoolsManager.storagePools.get(1).hasNShards(1)) {
                fail("Did not insert correct number of shards!");
            }
        }
    }

}