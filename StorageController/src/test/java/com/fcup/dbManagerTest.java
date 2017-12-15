package com.fcup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class dbManagerTest {
    String db = "non-existent-database";
    DbManager dbManager;

    @Before
    public void initDBManagerForTests() {
        dbManager = new DbManager(db);
    }

    @Test
    public void createDBFileIfNotExists() throws Exception {
        try{
            dbManager.connectToDB();
            dbManager.createDBFileIfNotExists();
        }
        catch(Exception e){
            e.printStackTrace();
            fail("Couldn't create database file");
        }
    }

    @Test
    public void createOperationsTable() throws Exception {
    }

    @Test
    public void insertAndReadEntry() {
        try{
            dbManager.connect();
            Operation operation = new Operation();
            operation.changeKeyValue("chunkID", "testchunkID");
            operation.changeKeyValue("blockID", "testblockID");
            dbManager.insertOperation(operation);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not insert values into database");
        }

        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("chunkID", "testchunkID");
            queryParams.put("blockID", "testblockID");
            List<Operation> operationsRead = dbManager.readEntries(queryParams);

            if(!operationsRead.get(0).hasChunkIDAndBlockID("testchunkID", "testblockID"))
                fail("Read bad value from database: " + "should have read " + "testchunkID, read " + operationsRead.get(0).toString());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not read values from database");
        }
    }

    @Test
    public void insertAndReadSingleEntry() {
        try{
            dbManager = new DbManager(db);
            dbManager.connect();
            Operation operation = new Operation();
            operation.changeKeyValue("blockID", "testblockID");
            operation.changeKeyValue("chunkID", "testchunkID");
            dbManager.insertOperation(operation);
            System.out.println("Inserted operation: " + operation.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not insert values into database");
        }

        try {
            List<Operation> operationsRead = dbManager.readEntries();

            if (operationsRead.size() != 1) {
                fail("Read more than one entry");
            }

            if(!operationsRead.get(0).hasChunkIDAndBlockID("testchunkID", "testblockID"))
                fail("Read bad value from database: " + "should have read " + "testchunkID, read " + operationsRead.get(0).toString());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not read values from database");
        }
    }

    @After
    public void removeTestDb() {
        Path path = Paths.get(db);
        try {
            java.nio.file.Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}