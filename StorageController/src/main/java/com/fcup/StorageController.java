package com.fcup;

import io.grpc.StatusRuntimeException;

import com.fcup.generated.*;
import utilities.GrpcServer;
import utilities.DownloadStarter;

// TODO extract custom jChannel class ?
public class StorageController extends ChannelStateSynchronizer {
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

    // TODO implement grpc app server <-> storage controller (e.g. implement grpc client......)
    // TODO add the good chunk ID (retrieved from app server)
    // called via Grpc by the app server
    // note: the app server or storage pool MUST have started a seeder before calling...
    public void makeStoragePoolDownloadAFileFromTheAppServer() throws Exception {
        String chunkID = "Test chunk ID...."; // TODO retrieve from grpc call from app server
        String poolAddress = "127.0.0.1"; // TODO retrieve from grpc call from app server
        int poolPort = 50051; // same... this is the default one

        // TODO retrieve this from the grpc call
        String destinationAdress = "";
        int destinationPort = 26001;

        Info request = Info.newBuilder().setIp(destinationAdress).setPort(destinationPort).setChunkId(chunkID).build(); // todo host = storage pool - how to get it ?
        Status response;
        try {
            DownloadStarter downloadStarter = new DownloadStarter(poolAddress, poolPort);
            response = downloadStarter.getAnswer(request);
        } catch (StatusRuntimeException e) {
            // TODO manage this: re-construct the chunks from FEC, and dispatch them in the other pools ... ?
            System.err.println("Could not start downloader in storage pool for chunk " + chunkID);
            throw e;
        }
        System.out.println("Response: " + response.getOk());

    }

    public void start() throws Exception {
        grpcServer.startGrpcServer();
        eventLoop();
    }

    private void eventLoop() throws Exception {
        Operation operation = new Operation();
        operation.changeKeyValue("type", Integer.toString((int)(Math.random()*20)));
        doOperation(operation);
        System.out.println("Sent operation: " + operation.asJSONString());

        System.in.read();
    }

}
