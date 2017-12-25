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
    private static String JGROUPS_CONFIG = "config.xml";
    private String CONFIG_FILE = "networkconf.json";
    String localIP = null;

    // todo pattern ?
    StoragePoolsManager() throws Exception {
        this(JGROUPS_CONFIG);
    }

    public StoragePoolsManager(Scanner sc) throws Exception {
        this(JGROUPS_CONFIG, sc);
    }

    private StoragePoolsManager(String JGROUPS_CONFIG) throws Exception {
        this(JGROUPS_CONFIG, new Scanner(System.in));
    }

    private StoragePoolsManager(String JGROUPS_CONFIG, Scanner sc) throws Exception {
        getParameters(sc);
        StoragePoolsManager.JGROUPS_CONFIG = JGROUPS_CONFIG;
        jgroupsChannel = new JChannel(JGROUPS_CONFIG).setReceiver(this);
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


    private void getParameters(Scanner sc) {
        ParametersReader parametersReader = new ParametersReader(CONFIG_FILE);
        JSONObject parameters = parametersReader.readParameters();

        System.out.println("Reading from defaults file....");
        if(parameters.has("Config-file")) {
            JGROUPS_CONFIG = parameters.getString("Config-file");
            System.out.println("Jgroups config file: " + JGROUPS_CONFIG);
        }

        if(parameters.has("IP")) {
            localIP = parameters.getString("IP");
            System.out.println("Local IP: " + localIP);
        }
        else{
            askAdminForLocalIP(sc);
        }

    }

    private void askAdminForLocalIP(Scanner sc) {
            System.out.println("Please enter local IP:");
            localIP = sc.nextLine();
//        }
    }

}
