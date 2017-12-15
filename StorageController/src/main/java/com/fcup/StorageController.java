package com.fcup;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fcup.generated.*;
import utilities.GrpcServer;
import utilities.downloadStarter;

// TODO extract custom jChannel class ?
public class StorageController extends ReceiverAdapter {
    JChannel jgroupsChannel;
    OperationManager operationManager;
    String user_name = System.getProperty("user.name", "n/a");
    GrpcServer grpcServer;

    // TODO change this to the real grpcPort in the google VMs ?
    // TODO test it in 1 VM, then 2 different ones
    private String host = "127.0.0.1";
    private int grpcPort = 50051;


    public static void main(String[] args) {
        try {
            // TODO retrieve local address + grpcPort ?
            StorageController storageController = new StorageController();
            storageController.connectToChannel();
            storageController.sync();
            storageController.start();
            System.out.println("Started!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StorageController() throws Exception {
        operationManager = new OperationManager();
        grpcServer = new GrpcServer();
        jgroupsChannel = new JChannel("config.xml").setReceiver(this);
    }

    // TODO create client that registers the storage pools
    // --> receive storage pool info via Grpc
    // --> when a new storage pool connects, contact all the storage controllers to register as well, via Grpc...

    // TODO create method that receives the new storage pool
    // from the storage pool, or...
    // by the master controller
    // Done by GrpcServer...

    // TODO implement grpc app server <-> storage controller (e.g. implement grpc client......)
    // TODO add the good chunk ID (retrieved from app server)
    // called via Grpc by the app server
    // note: the app server or storage pool MUST have started a seeder before calling...
    public void makeStoragePoolDownloadAFileFromTheAppServer() throws Exception {
        String chunkID = "Test chunk ID...."; // TODO retrieve from grpc call from app server
        String poolAddress = "127.0.0.1"; // TODO retrieve from grpc call from app server
        int poolPort = 50051; // same...
        Info request = Info.newBuilder().setIp(host).setPort(grpcPort).setChunkId(chunkID).build(); // todo host = storage pool - how to get it ?
        Status response;
        try {
            downloadStarter downloadStarter = new downloadStarter(poolAddress, poolPort);
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
        jgroupsChannel.close();
    }

    private void eventLoop() throws Exception {
        Operation operation = new Operation();
        operation.changeKeyValue("type", Integer.toString((int)(Math.random()*20)));
        doOperation(operation);
        System.out.println("Sent operation: " + operation.asJSONString());

        System.in.read();
    }

    public void connectToChannel() throws Exception {
        jgroupsChannel.connect("ChatCluster");
        System.out.println("Connected to channel");
    }

    public void sync() throws Exception {
        operationManager.loadLocalOperationsFromDB();
        jgroupsChannel.getState(null, 1000); // will callback local setState
        System.out.println("Synced!");
    }

    public void doOperation(Operation operation) throws Exception {
        jgroupsChannel.send(null, operation.asJSONString());
        operationManager.storeOperationInLocal(operation);
        operationManager.writeOperationIntoDB(operation);
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        Operation newOperation = Operation.fromJSON(line);
        operationManager.storeOperationInLocal(newOperation);
        operationManager.writeOperationIntoDB(newOperation);
    }

    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        operationManager.syncOperations(newOperations);
    }

    public void getState(OutputStream output) throws Exception {
        operationManager.getState(output);
    }



}
