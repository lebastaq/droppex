package com.fcup.utilities;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StoragePoolTest {
    private StoragePool storagePool;

    @Before
    public void initStoragePool() {
        storagePool = new StoragePool();
    }

    @Test
    public void addChunk() throws Exception {
        String chunk = "test";
        storagePool.addShard(chunk);

        if (!storagePool.shards.get(0).equals(chunk)) {
            fail("Did not insert chunk correctly");
        }

    }

    @Test
    public void removeChunk() throws Exception {
        String chunk = "test";
        storagePool.addShard(chunk);
        storagePool.removeShard(chunk);

        if (storagePool.shards.size() != 0) {
            fail("Did not remove chunk correctly");
        }

    }

}