package com.fcup;

import org.jgroups.util.Util;
import com.fcup.utilities.*;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

class ShardManager {
    private final DbManager dbManager;

    // TODO clean up double-variable mess
    List<Shard> shards = new LinkedList<>();
    private final List<String> operationsAsString = new LinkedList<>();

    public ShardManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
        dbManager.connect();
    }

    public ShardManager(DbManager dbManager) throws SQLException, ClassNotFoundException {
        this.dbManager = dbManager;
        dbManager.connect();
    }

    public void loadLocalOperationsFromDB() throws SQLException {
        try {
            List<Shard> operationsFromDB = dbManager.readEntries();
            for (Shard shard : operationsFromDB) {
                shards.add(shard);
                operationsAsString.add(shard.asJSONString());
            }
        } catch (SQLException e) {
            System.err.println("Could not read operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void storeOperation(Shard shard) {
        storeOperationInLocal(shard);
        writeOperationIntoDB(shard);
    }

    private void storeOperationInLocal(Shard shard) {
        synchronized (operationsAsString) {
            shards.add(shard);
            operationsAsString.add(shard.asJSONString());
        }
    }

    public void syncOperations(final List<String> newOperations) {
        synchronized (operationsAsString) {
            for (String op : newOperations)
            {
                syncOperation(op);
            }
        }

    }

    public void syncOperation(String op) {
        if(!operationsAsString.contains(op)) {
            final Shard shard = Shard.fromJSON(op);
            shards.add(shard);
            operationsAsString.add(op);
            writeOperationIntoDB(shard);
            System.out.println("New Operations: " + op);
        }
    }

    private void writeOperationIntoDB(Shard shard) {
        try {
            dbManager.insertShard(shard);
        } catch (Exception e) {
            System.err.println("Could not insert shard: ");
            e.printStackTrace();
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized (operationsAsString) {
            Util.objectToStream(operationsAsString, new DataOutputStream(output));
        }
    }

    public void syncLocalStoragePools(List<StoragePool> storagePools) {
        for (Shard op : shards) {
            syncLocalPoolsWithOperationPool(storagePools, op);
        }
    }

    void syncLocalPoolsWithOperationPool(List<StoragePool> storagePools, Shard newShard) {
        StoragePool operationStoragePool = newShard.findOrCreateStoragePoolToStoreMe(storagePools);

        if (!storagePools.contains(operationStoragePool)) {
            storagePools.add(operationStoragePool);
            System.out.println("Registering new storage pool...");
        }

        newShard.addMyselfToStoragePool(operationStoragePool);
    }

    public void emptyShardDatabase() throws SQLException {
        dbManager.emptyDatabase();
        dbManager.createOperationsTableIfNotExists();
    }

    public void deleteShard(List<StoragePool> storagePools, String shardId) {
        try {
            dbManager.deleteFileShards(shardId);
        } catch (SQLException e) {
            System.err.println("Could not delete shard from local database: ");
            e.printStackTrace();
        }

        // also delete from local storage pool
        for (Shard shard : shards) {
            if (shard.getId().equals(shardId)) {
                StoragePool operationStoragePool = shard.findOrCreateStoragePoolToStoreMe(storagePools);
                operationStoragePool.removeShard(shardId);
                break;
            }
        }
    }
}
