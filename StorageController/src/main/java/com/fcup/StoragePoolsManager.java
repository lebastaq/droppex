package com.fcup;

import org.jgroups.*;
import org.jgroups.util.Util;
import utilities.StoragePool;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StoragePoolsManager extends ReceiverAdapter {

    JChannel jgroupsChannel;
    OperationManager operationManager;
    boolean isLeader = false;
    List<StoragePool> storagePools;
    static String CONFIG_FILE = "config.xml";

    public StoragePoolsManager() throws Exception {
        this(CONFIG_FILE);
    }

    public StoragePoolsManager(String CONFIG_FILE) throws Exception {
        this.CONFIG_FILE = CONFIG_FILE;
        jgroupsChannel = new JChannel(CONFIG_FILE).setReceiver(this);
        operationManager = new OperationManager();
        storagePools = new ArrayList<>();
    }

    public void connectToChannel() throws Exception {
        jgroupsChannel.connect("ChatCluster");
        System.out.println("Connected to channel");
    }

    public void disconnectFromChannel() {
        jgroupsChannel.disconnect();
    }

    public void sync() throws Exception {
        operationManager.loadLocalOperationsFromDB();
        jgroupsChannel.getState(null, 1000); // will callback setState
        System.out.println("Synced!");
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
        electNewLeader();
        // TODO send new leader's IP to storage controller and app server
    }

    public void electNewLeader() {
        View view = jgroupsChannel.getView();
        Address address = view.getMembers()
                .get(0);
        if (address.equals(jgroupsChannel.getAddress())) {
            System.out.println("I'm (" + jgroupsChannel.getAddress() + ") the leader");
            isLeader = true;
        } else {
            System.out.println("I'm (" + jgroupsChannel.getAddress() + ") not the leader");
            isLeader = false;
        }
    }

    public void receive(Message msg) {
        String newOperation = msg.getObject();
        System.out.println("Received: " + newOperation);
        operationManager.storeOperation(Operation.fromJSON(newOperation));
        operationManager.syncOperation(newOperation);
        operationManager.syncLocalStoragePools(storagePools);
    }

    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        operationManager.syncOperations(newOperations);
    }

    public void getState(OutputStream output) throws Exception {
        operationManager.getState(output);
    }

    public void doOperation(Operation operation) throws Exception {
        jgroupsChannel.send(null, operation.asJSONString());
        operationManager.storeOperation(operation);
    }
}
