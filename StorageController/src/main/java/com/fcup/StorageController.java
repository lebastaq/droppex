package com.fcup;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StorageController extends ReceiverAdapter {
    JChannel channel;
    DbManager dbManager;
    String user_name = System.getProperty("user.name", "n/a");
    final List<String> operations = new LinkedList<>();

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
        channel.send(null, /*operation.asJSONString()*/ "test");
        writeOperationIntoDB(operation);
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        synchronized(operations) {
            operations.add(line);
            // TODO update database
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
//        syncOperations(newOperations);
    }

    public void syncOperations(final List<String> newOperations) throws SQLException {
        for (String op : newOperations)
        {
            if(!operations.contains(op)) {
                synchronized(operations) {
                    operations.add(op);
                }
                writeOperationIntoDB(Operation.fromJSON(op));
            }
        }

        System.out.println("Operations:");
        for (String op : operations) {
            System.out.println(op);
        }
    }

    public void loadLocalOperationsFromDB() {

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
