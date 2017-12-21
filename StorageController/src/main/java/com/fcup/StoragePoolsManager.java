package com.fcup;

import org.jgroups.*;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fcup.utilities.*;

public class StoragePoolsManager extends ReceiverAdapter {

    JChannel jgroupsChannel;
    ShardManager shardManager;
    boolean isLeader = false;
    List<StoragePool> storagePools;
    private static String CONFIG_FILE = "config.xml"; /* google_config.xml */
    String localIP;

    // todo pattern ?
    StoragePoolsManager() throws Exception {
        this(CONFIG_FILE);
    }

    public StoragePoolsManager(Scanner sc) throws Exception {
        this(CONFIG_FILE, sc);
    }

    private StoragePoolsManager(String CONFIG_FILE) throws Exception {
        this(CONFIG_FILE, new Scanner(System.in));
    }

    private StoragePoolsManager(String CONFIG_FILE, Scanner sc) throws Exception {
        StoragePoolsManager.CONFIG_FILE = CONFIG_FILE;
        jgroupsChannel = new JChannel(CONFIG_FILE).setReceiver(this);
        shardManager = new ShardManager();
        storagePools = new ArrayList<>();

        askAdminForLocalIP(sc);
    }

    public void connectToChannel() throws Exception {
        jgroupsChannel.connect("ChatCluster");
        electNewLeader();
        System.out.println("Connected to channel");
    }

    public void disconnectFromChannel() {
        jgroupsChannel.disconnect();
    }

    public void sync() throws Exception {
        shardManager.loadLocalOperationsFromDB();
        jgroupsChannel.getState(null, 10000); // will callback setState
        System.out.println("Synced!");
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
        electNewLeader();
    }

    private void electNewLeader() {
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
        String message = msg.getObject();
        System.out.println("Received: " + message);
        shardManager.storeOperation(Shard.fromJSON(message));
        shardManager.syncOperation(message);
        shardManager.syncLocalStoragePools(storagePools);
    }

    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        shardManager.syncOperations(newOperations);
        shardManager.syncLocalStoragePools(storagePools);
    }

    public void getState(OutputStream output) throws Exception {
        shardManager.getState(output);
    }

    public void sendMessage(Shard shard) {
        try {
            jgroupsChannel.send(null, shard.asJSONString());
            shardManager.storeOperation(shard);
        } catch (Exception e) {
            System.err.println("Could not send shard " + shard.toString() + ":");
            e.printStackTrace();
        }
    }


    private void askAdminForLocalIP(Scanner sc) {
        System.out.println("Please enter local IP:");
        localIP = sc.nextLine();
    }
}
