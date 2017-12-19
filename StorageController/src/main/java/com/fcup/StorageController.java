package com.fcup;

import io.grpc.StatusRuntimeException;

import com.fcup.generated.*;
import utilities.GrpcServer;
import utilities.DownloadStarter;

public class StorageController extends StoragePoolsManager {
    String user_name = System.getProperty("user.name", "n/a");
    GrpcServer grpcServer;
    private final static String CONFIG_FILE = "config.xml"; /* google_config.xml */

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
        grpcServer.startGrpcServer();
        eventLoop();
    }

    private void eventLoop() throws Exception {
        Shard shard = new Shard();
        shard.changeKeyValue("chunkID", Integer.toString((int)(Math.random()*20)));
        shard.changeKeyValue("storagePoolIP", "dummy 2");
        doOperation(shard);
        System.out.println("Sent shard: " + shard.asJSONString());

        System.in.read();
    }

}
