package com.fcup;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StorageController extends ReceiverAdapter {
    JChannel channel;
    DbManager dbManager;
    String user_name = System.getProperty("user.name", "n/a");
    List<Operation> operations = new LinkedList<>();

    public static void main(String[] args) {
        try {
            StorageController storageController = new StorageController();
            storageController.connectToChannel();
            storageController.sync();
            storageController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        eventLoop();
        channel.close();
    }

    /*
    TODO compare against local stack of operations
    TODO for each new operation, execute it...
     */
    private void eventLoop() throws Exception {
        Operation operation = new Operation();
        operation.changeKeyValue("type", Integer.toString((int)(Math.random()*20)));
//        operation.type = "Test";
//        operation.blockID = "Test block ID";
//        operation.chunkID = "Test chunk ID";
//        operation.destination = "dest src";
//        operation.source = "test source";
//        operation.ID = "test ID";
        doOperation(operation);
        System.out.println("Sent operation: " + operation.asJSONString());
        while (true) {

        }
    }

    public StorageController() throws Exception {
        dbManager = new DbManager();
        dbManager.connect();
        channel = new JChannel("config.xml");
        channel.setReceiver(this);
    }

    public void connectToChannel() throws Exception {
        channel.connect("ChatCluster");
    }

    public void sync() throws Exception {
        loadLocalOperationsFromDB();
        channel.getState(null, 10000); // will callback local setState
    }

    public void doOperation(Operation operation) throws Exception {
        channel.send(null, operation.asJSONString());
        storeOperationInLocal(operation);
        writeOperationIntoDB(operation);
    }

    public void storeOperationInLocal(Operation operation) {
        operations.add(operation);
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        synchronized(operations) {
            operations.add(Operation.fromJSON(line));
            // TODO update database
        }
    }

    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        synchronized(operations) {
            syncOperations(newOperations);
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(operations) {
            Util.objectToStream(operations, new DataOutputStream(output));
        }
    }

    public void syncOperations(final List<String> newOperations) throws SQLException {
        for (String op : newOperations)
        {
            if(!operations.contains(op)) {
                final Operation operation = Operation.fromJSON(op);
                operations.add(operation);
                writeOperationIntoDB(operation);
            }
        }

        System.out.println("Operations:");
        for (Operation op : operations) {
            System.out.println(op.asJSONString());
        }
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

    public void writeOperationIntoDB(Operation operation) throws SQLException {
        try {
            dbManager.insertOperation(operation);
        } catch (Exception e) {
            System.err.println("Could not insert operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }


}
