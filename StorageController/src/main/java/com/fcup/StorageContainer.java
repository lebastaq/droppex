package com.fcup;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.List;
import java.util.LinkedList;

public class StorageContainer extends ReceiverAdapter {
    JChannel channel;
    String user_name = System.getProperty("user.name", "n/a");
    List<String> operations = new LinkedList<>();
    List<String> newOperationsFromNetwork = new LinkedList<>();

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        synchronized(operations) {
            operations.add(line);
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(operations) {
            Util.objectToStream(operations, new DataOutputStream(output));
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        List<String> list = Util.objectFromStream(new DataInputStream(input));
        synchronized(operations) {
            newOperationsFromNetwork.addAll(operations);
        }
        System.out.println("received operations (" + list.size() + ")");
    }

    /*
    TODO compare against local stack of operations
    TODO for each new operation, execute it...
     */
    public void syncOperations() {
        channel.getState();
    }




    public void connectToChannel() throws Exception {
        channel.connect("ChatCluster");
    }

    public void start() {
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith("quit") || line.startsWith("exit")) {
                    break;
                }
                Operation operation = Operation.fromData("test", "block id");
                Message msg = new Message(null, operation.jsonRepresentation);
                channel.send(msg);
            }
            catch(Exception e) {
            }
        }
    }

    public StorageContainer() throws Exception {
        channel = new JChannel("config.xml");
        channel.setReceiver(this);
    }

    public static void main(String[] args) throws Exception {
        StorageContainer storageContainer = new StorageContainer();
        storageContainer.connectToChannel();
        storageContainer.syncOperations();
        storageContainer.start();
    }
}
