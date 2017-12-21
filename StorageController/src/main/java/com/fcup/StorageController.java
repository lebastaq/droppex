package com.fcup;

import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.storageControllerInfo;
import com.fcup.utilities.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.jgroups.View;

import java.net.UnknownHostException;

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
        System.in.read();
    }

    @Override
    public void viewAccepted(View new_view) {
        super.viewAccepted(new_view);

        if(isLeader)
            sendMasterIPAndPortToStoragePools();
    }

    private void sendMasterIPAndPortToStoragePools() {
        System.out.println("... will now contact storage pools");
        for (StoragePool storagePool : storagePools) {
            storageControllerInfo request = grpcServer.buildSetIPAndPortRequest(localIP);
            try {
                addressgetterGrpc.addressgetterBlockingStub blockingStub = storagePool.buildBlockingStub();

                blockingStub.setAddress(request);
                System.out.println("Sent new address to storage pool !");
            } catch (IllegalArgumentException|UnknownHostException e) {
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
