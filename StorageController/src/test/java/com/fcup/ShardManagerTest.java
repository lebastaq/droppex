package com.fcup;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ShardManagerTest {
    private DbManager dbManager;
    private ShardManager shardManager;

    @Before
    public void setUpDbManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
        dbManager.connect();

        shardManager = new ShardManager();
    }

    @Test
    public void writeOperationIntoDBAndThenLoadIt() throws Exception {
        Shard shard = new Shard();

        shardManager.storeOperation(shard);

        shardManager.shards = new LinkedList<>();
        shardManager.loadLocalOperationsFromDB();

        List<Shard> operationsExpected;
        operationsExpected = dbManager.readEntries();

        if(shardManager.shards.size() != operationsExpected.size())
        {
            fail("Stored " + shardManager.shards.size() + " shards from db instead of " + operationsExpected.size());
        }
    }





}