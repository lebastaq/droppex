package com.fcup;

import org.jgroups.util.Util;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class OperationManager {
    DbManager dbManager;
    List<Operation> operations = new LinkedList<>();

    public OperationManager() throws SQLException, ClassNotFoundException {
        dbManager = new DbManager();
        dbManager.connect();
    }

    public void loadLocalOperationsFromDB() throws SQLException {
        try {
            List<Operation> operationsFromDB = dbManager.readEntry();
            for (Operation operation : operationsFromDB) {
                operations.add(operation);
            }
        } catch (SQLException e) {
            System.err.println("Could not read operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void storeOperationInLocal(Operation operation) {
        synchronized (operations) {
            operations.add(operation);
        }
    }

    public void syncOperations(final List<String> newOperations) throws SQLException {
        synchronized (operations) {
            for (String op : newOperations)
            {
                if(!operations.contains(op)) {
                    final Operation operation = Operation.fromJSON(op);
                    operations.add(operation);
                    writeOperationIntoDB(operation);
                }
            }
        }


        System.out.println("Operations:");
        for (Operation op : operations) {
            System.out.println(op.asJSONString());
        }
    }

    public void writeOperationIntoDB(Operation operation) throws SQLException {
        try {
            dbManager.insertOperation(operation);
        } catch (Exception e) {
            System.err.println("Could not insert operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void getState(OutputStream output) throws Exception {
        Util.objectToStream(operations, new DataOutputStream(output));
    }
}
