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
        initDBManagerForTests();
        storageController = createStorageController(dbManager);
    }

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

    private StorageController createStorageController(DbManager dbManager) {
        StorageController storageControllerLocal = null;
        try {
            String data = "127.0.0.1";
            System.setIn(new ByteArrayInputStream(data.getBytes()));

            Scanner sc = new Scanner(System.in);

            storageControllerLocal = new StorageController(sc);

            // custom local database
            storageControllerLocal.shardManager = new ShardManager(dbManager);
        } catch (Exception e) {
            System.err.println("Could not create storage controller:");
            e.printStackTrace();
        }

        return storageControllerLocal;
    }


    @Test
    public void testElectionOfLeader() throws Exception {
        StorageController storageController2 = createStorageController(dbManager);
        try {
            storageController.connectToChannel();
            storageController.sync();
            storageController2.connectToChannel();
            storageController2.sync();
            storageController.disconnectFromChannel();

            // force re-election
            storageController2.electNewLeader();

            if(!storageController.isLeader)
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

        dbManager.connect();

        storageController.connectToChannel();
        storageController.sendMessage(shard);

        storageController.shardManager.shards = new LinkedList<>();
        storageController.shardManager.loadLocalOperationsFromDB();

        List<Shard> operationsExpected;
        operationsExpected = dbManager.readEntries();

        if(storageController.shardManager.shards.size() != operationsExpected.size())
        {
            fail("Loaded " + storageController.shardManager.shards.size() + " shards from db instead of " + operationsExpected.size());
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