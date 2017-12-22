package com.fcup;

import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.storageControllerInfo;
import com.fcup.utilities.*;
import io.grpc.StatusRuntimeException;
import java.util.Scanner;
import org.jgroups.View;

public class StorageController extends StoragePoolsManager {
    String user_name = System.getProperty("user.name", "n/a");
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

    // TODO something about these two constructors
    public StorageController(Scanner sc) throws Exception {
        super(sc);
        grpcServer = new GrpcServer();
    }

    public void start() throws Exception {
        Thread psThread = new Thread(portalServer, "AppServer File Transfer Thread");
        psThread.start();

//        grpcServer.startGrpcServer(this);
//        eventLoop();
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
        if (isLeader) {
            System.out.println("... will now contact storage pools to update master controller ip and port");
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
}
