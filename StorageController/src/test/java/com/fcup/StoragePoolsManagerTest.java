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
    public void createStateSynchronizer() {
        try {
            storagePoolsManager = new StoragePoolsManager();
        } catch (Exception e) {
            System.err.println("Could not create storagePoolsManager controller:");
            e.printStackTrace();
        }
    }

    @Test
    public void syncOperations() throws Exception {
        try{
            storagePoolsManager.connectToChannel();
            storagePoolsManager.operationManager.syncOperations(new ArrayList<String>());
        }
        catch (Exception e){
            e.printStackTrace();
            fail("Could not sync operations");
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

        assertEquals(storagePoolsManager.operationManager.operations, expectedNewOperations);
    }

    @Test
    public void retrieveStateWithoutException() throws Exception {
        StorageController storageController2 = new StorageController();
        try {
            storagePoolsManager.connectToChannel();
            storagePoolsManager.sync();
            // TODO something with the IP adress...
            storageController2.connectToChannel();
            storageController2.sync();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not sync state");
        }
        storageController2.disconnectFromChannel();
    }

    @Test
    public void syncLocalStoragePools() throws Exception {
        Operation operation1 = new Operation();
        operation1.changeKeyValue("storagePoolIP", "ip1");
        Operation operation2 = new Operation();
        operation2.changeKeyValue("storagePoolIP", "ip2");

        storagePoolsManager.syncLocalPoolsWithOperationPool(operation1);

        if (storagePoolsManager.storagePools.size() != 1) {
            fail("Did not insert first storage pool");
        }

        storagePoolsManager.syncLocalPoolsWithOperationPool(operation1);

        if (storagePoolsManager.storagePools.size() != 2) {
            fail("Did not insert second storage pool");
        }
    }

}