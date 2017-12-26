package com.fcup.utilities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ShardDispatcherTest {
    ShardDispatcher shardDispatcher;

    @Before
    public void initShardDispatcher() {
        shardDispatcher = new ShardDispatcher();
    }

    // test one file with one block and X shards - X pools
    @Test
    public void testConsistentHashing() {
        for (int i = 1; i < 20; i++) {
            if (!testConsistentHashing(i)) {
                fail("Did not hash consistently for n=" + i);
            }
        }
    }

    public boolean testConsistentHashing(int n) {
        List ids = new ArrayList<Integer>();
        System.out.println("====================== " + n);
        for (int i = 0; i < n; i++) {
            String shardName = "filename.0." + Integer.toString((i));
            ids.add(shardDispatcher.idToIndex(shardName, n));
            System.out.println(shardName + "-- " + ids.get(ids.size() - 1));
        }

        return true;
    }
}