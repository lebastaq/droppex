package com.fcup.utilities;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        for (int i = 1; i <= 6; i++) {
            if (!testConsistentHashing(i)) {
                fail("Did not hash consistently for n=" + i);
            }
        }
    }

    public boolean testConsistentHashing(int n) {
        List ids = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            String shardName = "filename.0." + Integer.toString((i));
            ids.add(shardDispatcher.idToIndex(shardName, n));
        }

        Set<Integer> set = new HashSet<Integer>(ids);

        if(set.size() != ids.size())
            return false;

        return true;
    }
}