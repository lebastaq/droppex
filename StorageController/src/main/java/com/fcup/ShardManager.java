package com.fcup;

import org.jgroups.util.Util;
import utilities.StoragePool;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ShardManager {
    DbManager dbManager;

    // TODO clean up double-variable mess
    List<Shard> shards = new LinkedList<>();
    List<String> operationsAsString = new LinkedList<>();

    public ShardManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
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

    public void syncOperations(final List<String> newOperations) throws SQLException {
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
            dbManager.insertOperation(shard);
        } catch (Exception e) {
            System.err.println("Could not insert shard");
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

    protected void syncLocalPoolsWithOperationPool(List<StoragePool> storagePools, Shard newShard) {
        // create new storage pool if it does not exist
        StoragePool operationStoragePool = newShard.operationPoolToStoreMe(storagePools);

        if (!storagePools.contains(operationStoragePool)) {
            storagePools.add(operationStoragePool);
            System.out.println("Registering new storage pool...");
        }

        newShard.addMyselfToStoragePool(operationStoragePool);
    }
}
