package com.fcup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
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
            List<String> values = new ArrayList<>();
            values.add("testType");
            values.add("testchunkID");
            values.add("testblockID");
            values.add("testdestination");
            values.add("testsource");
            dbManager.insertEntry(values);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not insert values into database");
        }

        try {
            Map<String, String> values = new HashMap<>();
            values.put("chunkID", "testchunkID");
            List<Operation> operationsRead = dbManager.readEntry(values);

            if(!operationsRead.get(0).chunkID.equals("testchunkID"))
                fail("Read bad value from database:" + operationsRead.get(0).chunkID + " instead of " + "testchunkID");

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not read values from database");
        }
    }

    @Test
    public void getAllEntriesFromTable() throws Exception {
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