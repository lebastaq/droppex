package com.fcup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.fail;

public class StorageControllerTest {
    private StorageController storageController;
    private DbManager dbManager;
    private final String dbName = "dummy-dbName";

    @Before
    public void initSingleController() {
        storageController = createStorageController();
    }

    @Before
    public void initDBManagerForTests() {
        dbManager = new DbManager(dbName);

        try{
            dbManager.connectToDB();
            dbManager.createDBFileIfNotExists();
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Couldn't create database file");
        }
    }

    private StorageController createStorageController() {
        StorageController storageControllerLocal = null;
        try {
            String data = "127.0.0.1";
            System.setIn(new ByteArrayInputStream(data.getBytes()));

            Scanner sc = new Scanner(System.in);

            storageControllerLocal = new StorageController(sc);
            System.out.println("OK");
        } catch (Exception e) {
            System.err.println("Could not create storage controller:");
            e.printStackTrace();
        }

        return storageControllerLocal;
    }


    @Test
    public void testElectionOfLeader() throws Exception {
        StorageController storageController2 = createStorageController();
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
        storageController.sendMessage(shard);

        storageController.shardManager.shards = new LinkedList<>();
        storageController.shardManager.loadLocalOperationsFromDB();

        List<Shard> operationsExpected;
        operationsExpected = dbManager.readEntries();

        if(storageController.shardManager.shards.size() != operationsExpected.size())
        {
            fail("Stored " + storageController.shardManager.shards.size() + " shards from dbName instead of " + operationsExpected.size());
        }
    }


    @After
    public void disconnectFromChannel() {
        storageController.disconnectFromChannel();
    }


    @After
    public void removeTestDb() {
        Path path = Paths.get(dbName);
        try {
            java.nio.file.Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}