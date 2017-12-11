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
import java.util.LinkedList;
import java.util.List;

public class StorageContainer extends ReceiverAdapter {
    JChannel channel;
    String user_name = System.getProperty("user.name", "n/a");
    final List<String> operations = new LinkedList<>();

    public static void main(String[] args) {
        try {
            StorageContainer storageContainer = null;
            storageContainer = new StorageContainer();
            storageContainer.connectToChannel();
            storageContainer.start();
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
        Operation operation = Operation.fromData("test", "block id");
        sendOperation(operation);
        System.out.println("Sent operation: " + operation.jsonRepresentation);
        while (true) {

        }
    }

    public StorageContainer() throws Exception {
        channel = new JChannel("config.xml");
        channel.setReceiver(this);
    }

    public void connectToChannel() throws Exception {
        channel.connect("ChatCluster");
        channel.getState(null, 1000);
    }

    public void sendOperation(Operation operation) throws Exception {
        channel.send(null, operation.jsonRepresentation);
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        synchronized(operations) {
            operations.add(line);
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        synchronized(operations) {
            syncOperations(newOperations);
        }
    }

    public void syncOperations(final List<String> newOperations) {
        for (String op : newOperations) {
            System.out.println(op);
        }
    }


}
