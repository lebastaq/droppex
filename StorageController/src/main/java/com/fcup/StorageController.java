package com.fcup;

import com.fcup.generated.*;
import com.fcup.utilities.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.Scanner;
import org.jgroups.View;

public class StorageController extends StoragePoolsManager {
    private final int PORTAL_PORT = 50100;

    private GrpcServer grpcServer;
    private PortalServer portalServer;

    static StorageController storageController;

    public static void main(String[] args) {
        try {
            storageController = new StorageController();
            storageController.connectToChannel();
            storageController.sync();
            storageController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static StorageController getController() {
        return storageController;

    }

    public StorageController() throws Exception {
        super();
        grpcServer = new GrpcServer();
        portalServer = new PortalServer();

    }

    public StorageController(Scanner sc) throws Exception {
        super(sc);
        grpcServer = new GrpcServer();
    }

    public void start() throws Exception {
        Thread psThread = new Thread(portalServer, "AppServer StoredFile Transfer Thread");
        psThread.start();
    }

    public void sendGroupMessageToDeleteShard(String shardId) {
        super.sendGroupMessageToDeleteShard(shardId);
        if (isLeader) {
            findPoolAndSendDeletionMessage(shardId);
        } else {
            System.err.println("Received grpc message asking to delete file. I am not the master controller, this should not happen");
        }
    }

    private void findPoolAndSendDeletionMessage(String shardId) {
        StoragePool storagePoolContainingShard = findPoolContainingShard(shardId);

        if (storagePoolContainingShard == null) {
            System.err.println("Could not find storage pool containing shard " + shardId);
        } else {
            sendDeletionMessage(storagePoolContainingShard, shardId);
        }
    }

    private StoragePool findPoolContainingShard(String shardId) {
        StoragePool storagePoolContainingShard = null;

        for (StoragePool storagePool : storagePools) {
            if (storagePool.containsShard(shardId)) {
                storagePoolContainingShard = storagePool;
                break;
            }
        }
        return storagePoolContainingShard;
    }


    private void sendDeletionMessage(StoragePool storagePoolContainingShard, String shardId) {
        ShardId shardInfo = grpcServer.buildDeleteShardRequest(shardId);
        try{
            deleterGrpc.deleterBlockingStub blockingStub = storagePoolContainingShard.buildDeleterBlockingStub();
            blockingStub.delete(shardInfo);
        } catch (Exception e){
            System.err.println("Unhandled exception");
            e.printStackTrace();
        }
    }

    @Override
    public void viewAccepted(View new_view) {
        super.viewAccepted(new_view);

        if(isLeader) {
            grpcServer.startGrpcServer();
            sendMasterIPToStoragePools();
            sendMasterIPToAppServer();
        }
        else{
            grpcServer.stopServer();
        }
    }

    private void sendMasterIPToAppServer() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", PORTAL_PORT)
                                                      .usePlaintext(true)
                                                      .build();

        LeaderServiceGrpc.LeaderServiceBlockingStub stub = LeaderServiceGrpc.newBlockingStub(channel);

        stub.announceLeader(LeaderIP.newBuilder().setIp(localIP).build());

        channel.shutdown();
    }

    private void sendMasterIPToStoragePools() {
        System.out.println("Contacting storage pools to update master controller ip and port");
        for (StoragePool storagePool : storagePools) {
            storageControllerInfo request = grpcServer.buildSetIPAndPortRequest(localIP);
            try {
                addressgetterGrpc.addressgetterBlockingStub blockingStub = storagePool.buildMasterSetterBlockingStubAndConnect();

                blockingStub.setAddress(request);
                System.err.println("Sent new address to storage pool !");
            } catch (IllegalArgumentException e) {
                System.err.println("Could not connect: Invalid storage pool name");
            } catch (StatusRuntimeException e) {
                System.err.println("Could not connect: pool unavailable");
            } catch (Exception e) {
                System.err.println("Unhandled exception");
                e.printStackTrace();
            }
        }
    }
}
