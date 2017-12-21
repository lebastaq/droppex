package com.fcup;

import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.registererGrpc;
import com.fcup.generated.storageControllerInfo;
import com.fcup.utilities.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.jgroups.View;

public class StorageController extends StoragePoolsManager {
    String user_name = System.getProperty("user.name", "n/a");
    GrpcServer grpcServer;

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

    public StorageController() throws Exception {
        super();
        grpcServer = new GrpcServer();
    }

    public void start() throws Exception {
        grpcServer.startGrpcServer(this);
        eventLoop();
    }

    private void eventLoop() throws Exception {
        Shard shard = new Shard();
        shard.changeKeyValue("shardID", Integer.toString((int)(Math.random()*20)));
        shard.changeKeyValue("storagePoolIP", "dummy 2");
        sendMessage(shard);
        System.out.println("Sent shard: " + shard.asJSONString());

        System.in.read();
    }

    @Override
    public void viewAccepted(View new_view) {
        super.viewAccepted(new_view);

        if(isLeader)
            sendMasterIPAndPortToStoragePools();
    }

    private void sendMasterIPAndPortToStoragePools() {
        for (StoragePool storagePool : storagePools) {
            storageControllerInfo request = grpcServer.buildSetIPAndPortRequest(localIP);
            try {
                addressgetterGrpc.addressgetterBlockingStub blockingStub = storagePool.buildBlockingStub();

                blockingStub.setAddress(request);
                System.out.println("Sent new address to storage pool !");
            } catch (IllegalArgumentException e) {
                System.out.println("Could not connect: Invalid storage pool name");
            } catch (StatusRuntimeException e) {
                System.out.println("Could not connect: pool unavailable");
            } catch (Exception e) {
                System.out.println("Unhandled exception");
                e.printStackTrace();
            }
        }
    }
}
