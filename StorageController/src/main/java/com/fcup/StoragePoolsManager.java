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
import org.json.JSONObject;

public class StoragePoolsManager extends ReceiverAdapter {

    JChannel jgroupsChannel;
    ShardManager shardManager;
    boolean isLeader = false;
    List<StoragePool> storagePools;
    private static String CONFIG_FILE = "config.xml";
    String localIP = null;

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
        getParameters(sc);
        StoragePoolsManager.CONFIG_FILE = CONFIG_FILE;
        jgroupsChannel = new JChannel(CONFIG_FILE).setReceiver(this);
        shardManager = new ShardManager();
        storagePools = new ArrayList<>();

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

    void electNewLeader() {
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

    public void setState(InputStream input) {
        try {
            shardManager.emptyShardDatabase();
            List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
            shardManager.syncOperations(newOperations);
            shardManager.syncLocalStoragePools(storagePools);
        } catch (Exception e) {
            System.out.println("Could not sync database from other controllers !");
            e.printStackTrace();
            System.exit(0);
        }
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


    // todo reformat...
    private void getParameters(Scanner sc) {
//        ParametersReader parametersReader = new ParametersReader();
//        JSONObject parameters = parametersReader.readFromFile();
//
//        if(parameters.has("Config-file")) {
//            CONFIG_FILE = parameters.getString("Config-file");
//        }
//
//        if(parameters.has("IP")) {
//            localIP = parameters.getString("IP");
//            System.out.println("OK");
//        }
//        else{
//            System.out.println("Not ok");
//        }

        askAdminForLocalIP(sc);

        System.out.println("Config:" + CONFIG_FILE);
        System.out.println("IP: " + localIP);
    }

    private void askAdminForLocalIP(Scanner sc) {
//        System.out.println("Default IP is: " + localIP + ". Do you want to change it ? (enter to accept default ip)");
//        String in = sc.nextLine();
//
//        if(!in.isEmpty()){
            System.out.println("Please enter local IP:");
            localIP = sc.nextLine();
//        }
    }

}
