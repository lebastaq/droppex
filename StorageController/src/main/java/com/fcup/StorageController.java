package com.fcup;

import utilities.GrpcServer;

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

}
