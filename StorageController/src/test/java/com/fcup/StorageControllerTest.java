package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
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

    @Test
    public void syncOperations() throws Exception {
        try{
            storageController.connectToChannel();
            storageController.syncOperations(new ArrayList<String>());
        }
        catch (Exception e){
            fail("Could not sync operations");
        }
    }

    @Test
    public void viewAccepted() {
        List<String> expectedNewOperations = new LinkedList<>();

        try {
            storageController.connectToChannel();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not connect to channel");
        }

        assertEquals(storageController.operations, expectedNewOperations);
    }

    @Test
    public void retrieveStateWithoutException(){
        try {
            storageController.connectToChannel();
            storageController.sync();
            StorageController storageController2 = new StorageController();
            storageController2.connectToChannel();
            storageController2.sync();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not sync state");
        }
    }

    // TODO add test sync

    @Test
    public void receive() throws Exception {
    }

    @Test
    public void setState() throws Exception {
    }

}