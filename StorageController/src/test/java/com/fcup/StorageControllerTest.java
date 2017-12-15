package com.fcup;

import org.junit.After;
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
            // TODO adress ?
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
    public void testElectionOfLeader() {
        try {
            StorageController storageController2 = new StorageController();
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
    }

    @Test
    public void doOperationIntoDBAndThenLoadIt() throws Exception {
        Operation operation = new Operation();
        DbManager dbManager = new DbManager();

        dbManager.connect();

        storageController.connectToChannel();
        storageController.doOperation(operation);

        storageController.operationManager.operations = new LinkedList<>();
        storageController.operationManager.loadLocalOperationsFromDB();

        List<Operation> operationsExpected;
        operationsExpected = dbManager.readEntry();

        if(storageController.operationManager.operations.size() != operationsExpected.size())
        {
            fail("Stored " + storageController.operationManager.operations.size() + " operations from db instead of " + operationsExpected.size());
        }
    }


    @Test
    public void syncOperations() throws Exception {
        try{
            storageController.connectToChannel();
            storageController.operationManager.syncOperations(new ArrayList<String>());
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
            storageController.connectToChannel();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not connect to jgroupsChannel");
        }

        assertEquals(storageController.operationManager.operations, expectedNewOperations);
    }

    @Test
    public void retrieveStateWithoutException(){
        try {
            storageController.connectToChannel();
            storageController.sync();
            // TODO something with the IP adress...
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