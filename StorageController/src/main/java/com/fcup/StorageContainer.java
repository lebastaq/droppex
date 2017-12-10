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
    final List<String> operations = new LinkedList<>();

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
//            operations.clear();
//            operations.addAll(list);
            syncOperations(list);
        }
        System.out.println("received operations (" + list.size() + ")");
    }

    private void syncOperations(List<String> list) {
        for(String operationString : list) {
            Operation operation = Operation.fromJSON(operationString);
            System.out.println("Got operation: " + operation.jsonRepresentation);
        }
    }


    private void start() throws Exception {
        channel=new JChannel().setReceiver(this);
        channel.connect("ChatCluster");
        channel.getState(null, 10000);
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
                System.out.println("Sending " + operation.jsonRepresentation);
                Message msg = new Message(null, operation.jsonRepresentation);
                channel.send(msg);
            }
            catch(Exception e) {
            }
        }
    }


    public static void main(String[] args) throws Exception {
        new StorageContainer().start();
    }
}
