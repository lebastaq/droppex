package com.fcup.utilities;

import com.fcup.StoragePoolsManager;
import com.fcup.generated.storageControllerInfo;
import io.grpc.ServerBuilder;

public class GrpcServer {
    private int port = 50100;
    private io.grpc.Server server = null;

    public GrpcServer() {
    }

    public void startGrpcServer(StoragePoolsManager storagePoolsManager) {
        do {
            try {
                server = ServerBuilder.forPort(port)
                        .addService(new StoragePoolRegisterer(storagePoolsManager))
                        .build()
                        .start();
            }
            catch(java.io.IOException e){
                System.out.println("Could not start server on port " + port);
                port ++;
            }
        } while(server == null);
        System.out.println("Grpc server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                GrpcServer.this.stopServer();
                System.err.println("*** server shut down");
            }
        });
    }

    public void stopServer() {
        if (server != null) {
            server.shutdown();
        }
    }

    public storageControllerInfo buildSetIPAndPortRequest(String localIP) {
        return storageControllerInfo.newBuilder().setIp(localIP).setPort(port).build();
    }
}
