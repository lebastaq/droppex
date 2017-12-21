package com.fcup;

import utilities.GrpcServer;
import utilities.PortalServer;

public class StorageController extends StoragePoolsManager {
    String user_name = System.getProperty("user.name", "n/a");
    GrpcServer grpcServer;
    PortalServer portalServer;

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

    public void start() throws Exception {
        Thread psThread = new Thread(portalServer, "AppServer File Transfer Thread");
        psThread.start();

        grpcServer.startGrpcServer(this);
        eventLoop();
    }

    private void eventLoop() throws Exception {
        Shard shard = new Shard();
        shard.changeKeyValue("chunkID", Integer.toString((int)(Math.random()*20)));
        shard.changeKeyValue("storagePoolIP", "dummy 2");
        sendMessage(shard);
        System.out.println("Sent shard: " + shard.asJSONString());

        System.in.read();
    }

}
