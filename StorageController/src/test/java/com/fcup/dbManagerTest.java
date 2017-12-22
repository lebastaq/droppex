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
    private final String db = "non-existent-database";
    private DbManager dbManager;

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
    public void insertAndReadEntry() {
        String shardID = "testchunkID";
        try{
            dbManager.connect();
            Shard shard = new Shard();
            shard.changeKeyValue("shardID", shardID);
            dbManager.insertOperation(shard);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not insert values into database");
        }

        try {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("shardID", shardID);
            List<Shard> operationsRead = dbManager.readEntries(queryParams);

            if(!operationsRead.get(0).hasShardID(shardID))
                fail("Read bad value from database: " + "should have read " + shardID + ", read " + operationsRead.get(0).toString());

        } catch (SQLException e) {
            e.printStackTrace();
            fail("Could not read values from database");
        }
    }

    @Test
    public void insertAndReadSingleEntry() {
        String shardID = "testchunkID";
        try{
            dbManager = new DbManager(db);
            dbManager.connect();
            Shard shard = new Shard();
            shard.changeKeyValue("shardID", shardID);
            dbManager.insertOperation(shard);
            System.out.println("Inserted shard: " + shard.toString());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not insert values into database");
        }

        try {
            List<Shard> operationsRead = dbManager.readEntries();

            if (operationsRead.size() != 1) {
                fail("Read more than one entry");
            }

            if(!operationsRead.get(0).hasShardID(shardID))
                fail("Read bad value from database: " + "should have read " + shardID + ", read " + operationsRead.get(0).toString());

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