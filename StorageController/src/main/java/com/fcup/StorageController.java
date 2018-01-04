package com.fcup;

import com.fcup.generated.ShardId;
import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.deleterGrpc;
import com.fcup.generated.storageControllerInfo;
import com.fcup.utilities.*;
import io.grpc.StatusRuntimeException;
import java.util.Scanner;
import org.jgroups.View;

public class StorageController extends StoragePoolsManager {
    private GrpcServer grpcServer;
    private PortalServer portalServer;

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
        portalServer = new PortalServer();

    }

    public StorageController(Scanner sc) throws Exception {
        super(sc);
        grpcServer = new GrpcServer();
    }

    public void start() throws Exception {
        Thread psThread = new Thread(portalServer, "AppServer File Transfer Thread");
        psThread.start();
    }

    // TODO add grpc call from app server (freddy)
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
            grpcServer.startGrpcServer(this);
            sendMasterIPAndPortToStoragePools();
        }
        else{
            grpcServer.stopServer();
        }
    }

    private void sendMasterIPAndPortToStoragePools() {
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
