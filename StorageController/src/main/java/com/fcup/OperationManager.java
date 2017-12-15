package com.fcup;

import org.jgroups.util.Util;
import utilities.StoragePool;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OperationManager {
    DbManager dbManager;

    // TODO clean up double-variable mess
    List<Operation> operations = new LinkedList<>();
    List<String> operationsAsString = new LinkedList<>();

    public OperationManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
        dbManager.connect();
    }

    public void loadLocalOperationsFromDB() throws SQLException {
        try {
            List<Operation> operationsFromDB = dbManager.readEntry();
            for (Operation operation : operationsFromDB) {
                operations.add(operation);
                operationsAsString.add(operation.asJSONString());
            }
        } catch (SQLException e) {
            System.err.println("Could not read operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void storeOperationInLocal(Operation operation) {
        synchronized (operationsAsString) {
            operations.add(operation);
            operationsAsString.add(operation.asJSONString());
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
            final Operation operation = Operation.fromJSON(op);
            operations.add(operation);
            operationsAsString.add(op);
            writeOperationIntoDB(operation);
            System.out.println("New Operations: " + op);
        }
    }

    public void writeOperationIntoDB(Operation operation) {
        try {
            dbManager.insertOperation(operation);
        } catch (Exception e) {
            System.err.println("Could not insert operation");
//            e.printStackTrace();
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized (operationsAsString) {
            Util.objectToStream(operationsAsString, new DataOutputStream(output));
        }
    }

    public void syncLocalStoragePools(List<StoragePool> storagePools) {
        for (Operation op : operations) {
            syncLocalPoolsWithOperationPool(storagePools, op);
        }
    }

    protected void syncLocalPoolsWithOperationPool(List<StoragePool> storagePools, Operation newOperation) {
        StoragePool operationStoragePool = newOperation.isInStoragePool(storagePools);
        if (!storagePools.contains(operationStoragePool)) {
            storagePools.add(operationStoragePool);
        }

        newOperation.addMyselfToStoragePool(operationStoragePool);
    }
}
