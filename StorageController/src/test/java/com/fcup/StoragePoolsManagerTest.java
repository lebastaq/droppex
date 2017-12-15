package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.jgroups.util.Util.assertEquals;
import static org.junit.Assert.*;

public class StoragePoolsManagerTest {
    private StoragePoolsManager stateSynchronizer;

    @Before
    public void createStateSynchronizer() {
        try {
            stateSynchronizer = new StoragePoolsManager();
        } catch (Exception e) {
            System.err.println("Could not create storage controller:");
            e.printStackTrace();
        }
    }

    @Test
    public void syncOperations() throws Exception {
        try{
            stateSynchronizer.connectToChannel();
            stateSynchronizer.operationManager.syncOperations(new ArrayList<String>());
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
            stateSynchronizer.connectToChannel();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not connect to jgroupsChannel");
        }

        assertEquals(stateSynchronizer.operationManager.operations, expectedNewOperations);
    }

    @Test
    public void retrieveStateWithoutException() throws Exception {
        StorageController storageController2 = new StorageController();
        try {
            stateSynchronizer.connectToChannel();
            stateSynchronizer.sync();
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
    }

}