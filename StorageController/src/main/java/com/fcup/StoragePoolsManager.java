package com.fcup;

import com.fcup.generated.PoolInfo;
import com.fcup.generated.registererGrpc;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
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
    ShardManager shardManager;
    boolean isLeader = false;
    List<StoragePool> storagePools;
    static String CONFIG_FILE = "config.xml"; /* google_config.xml */

    public StoragePoolsManager() throws Exception {
        this(CONFIG_FILE);
    }

    public StoragePoolsManager(String CONFIG_FILE) throws Exception {
        this.CONFIG_FILE = CONFIG_FILE;
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

        if(isLeader)
            sendNewMasterControllerIPToStoragePools();
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

    private void sendNewMasterControllerIPToStoragePools() {
        for (StoragePool storagePool : storagePools) {
//            storageControllerInfo request = PoolInfo.newBuilder().setIp(localIPAdress).setPort(localGrpcPort).build(); // todo host = storage pool - how to get it ?
//            try {
//                this.grpcChannel = ManagedChannelBuilder.forAddress(remoteControllerAddress, remoteControllerPort)
//                        .usePlaintext(true)
//                        .build();
//                blockingStub = registererGrpc.newBlockingStub(this.grpcChannel);
//                blockingStub.register(request);
//                connected = true;
//            } catch (StatusRuntimeException e) {
//                attempts ++;
//                remoteControllerPort++;
//            }
//
//            System.out.println("Registered as " + localIPAdress + ":" + localGrpcPort + " to " + remoteControllerAddress + ":" + remoteControllerPort);
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
}
