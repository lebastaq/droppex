package com.fcup.utilities;

import com.fcup.StoragePoolsManager;
import com.fcup.generated.*;
import io.grpc.ServerBuilder;

import java.sql.SQLException;

public class GrpcServer {
    private int port = 50100;
    private io.grpc.Server server = null;
    private boolean isStarted;

    public GrpcServer() {
        isStarted = false;
    }

    public void startGrpcServer() {
        if(!isStarted){
            do {
                try {
                    server = ServerBuilder.forPort(port)
                            .addService(new StoragePoolRegisterer())
                            .addService(new PortalControllerService())
                            .build()
                            .start();
                }
                catch(java.io.IOException | SQLException | ClassNotFoundException e){
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
            isStarted = true;
        }

    }

    public void stopServer() {
        if (server != null) {
            server.shutdown();
            isStarted = false;
        }
    }

    public storageControllerInfo buildSetIPAndPortRequest(String localIP) {
        return storageControllerInfo.newBuilder().setIp(localIP).setPort(port).build();
    }

    public ShardId buildDeleteShardRequest(String shard) {
        return ShardId.newBuilder().setId(shard).build();
    }
}
