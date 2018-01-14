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

import static java.lang.Math.ceil;

public class StoragePoolsManager extends ReceiverAdapter {

    JChannel jgroupsChannel;
    ShardManager shardManager;
    boolean isLeader = false;
    List<StoragePool> storagePools;
    private static String JGROUPS_CONFIG = "config.xml";
    private String CONFIG_FILE = "networkconf.json";
    String localIP = null;

    private int totalManagersThatJoined = 0;
    private int managersOnline = 0;

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
        //electNewLeader();
    }

    public void disconnectFromChannel() {
        jgroupsChannel.disconnect();
    }

    public void sync() throws Exception {
        shardManager.loadLocalOperationsFromDB();
        jgroupsChannel.getState(null, 10000); // will callback setState
        System.out.println("Synced");
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
        electNewLeader();
    }

    void electNewLeader() {
        View view = jgroupsChannel.getView();
        Address address = view.getMembers().get(0);
        updateNumberOfKnownManagersOnline(view);
        if (address.equals(jgroupsChannel.getAddress())) {
            System.out.println("I'm (" + jgroupsChannel.getAddress() + ") the leader");
            System.out.println("There are now " + totalManagersThatJoined + " managers that joined in total - "
            + managersOnline + " of which are connected right now");
            isLeader = true;
        } else {
            System.out.println("I'm (" + jgroupsChannel.getAddress() + ") not the leader");
            isLeader = false;
        }
    }

    private void updateNumberOfKnownManagersOnline(View view) {
        managersOnline = view.getMembers().size();
        if(managersOnline > totalManagersThatJoined)
            totalManagersThatJoined = managersOnline;
    }


    public void sendGroupMessageToDeleteShard(String shardId) {
        Shard shardToDelete = new Shard();
        shardToDelete.changeKeyValue("shardID", shardId);
        shardToDelete.changeKeyValue("operationType", "DEL");

        sendMessage(shardToDelete);
    }

    public void receive(Message msg) {
        String message = msg.getObject();
        System.out.println("Received: " + message);
        Shard shard = Shard.fromJSON(message);

        if (!enoughGroupMembersOnlineToAnswer()) {
            System.err.println("There are less than half the controllers online, could not process message");
        }
        else if (shard.isDeletionOperation()) {
            shardManager.deleteShard(storagePools, shard.getId());
        } else{
            shardManager.storeOperation(shard);
            shardManager.syncOperation(message);
            shardManager.syncLocalStoragePools(storagePools);
        }
    }

    public boolean enoughGroupMembersOnlineToAnswer() {
        return managersOnline >= ceil((float)totalManagersThatJoined / 2);
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
            // TODO are these 2 lines necessary ?
            shardManager.syncOperation(shard.toString());
            shardManager.syncLocalStoragePools(storagePools);
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
    }

}
