package com.fcup;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fcup.generated.*;

public class StorageController extends ReceiverAdapter {
    JChannel jgroupsChannel;
    DbManager dbManager;
    String user_name = System.getProperty("user.name", "n/a");
    List<Operation> operations = new LinkedList<>();

    private final ManagedChannel grpcChannel;
    private final downloaderGrpc.downloaderBlockingStub blockingStub;

    // TODO change this to the real grpcPort in the google VMs ?
    // TODO test it in 1 VM, then 2 different ones
    private String host = "127.0.0.1";
    private int grpcPort = 50051;


    public static void main(String[] args) {
        try {
            // TODO retrieve local adress + grpcPort ?
            StorageController storageController = new StorageController("127.0.0.1", 50051);
            storageController.connectToChannel();
            storageController.sync();
            storageController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO implement grpc app server <-> storage controller
    // TODO add the good chunk ID (retrieved from app server)
    // called via Grpc by the app server
    public void makeStoragePoolDownloadAFileFromTheAppServer() {
        String chunkID = "Test chunk ID...."; // todo retrieve from grpc call from app server
        Info request = Info.newBuilder().setIp(host).setPort(grpcPort).setChunkId(chunkID).build();
        Status response;
        try {
            response = blockingStub.startDownloader(request);
        } catch (StatusRuntimeException e) {
            // TODO manage this: re-construct the chunks from FEC, and dispatch them in the other pools ... ?
            System.err.println("Could not start downloader in storage pool for chunk " + chunkID);
            return;
        }
        System.out.println("Response: " + response.getOk());

    }

    public void start() throws Exception {
        eventLoop();
        jgroupsChannel.close();
    }

    private void eventLoop() throws Exception {
        Operation operation = new Operation();
        operation.changeKeyValue("type", Integer.toString((int)(Math.random()*20)));
//        operation.type = "Test";
//        operation.blockID = "Test block ID";
//        operation.chunkID = "Test chunk ID";
//        operation.destination = "dest src";
//        operation.source = "test source";
//        operation.ID = "test ID";
        doOperation(operation);
        System.out.println("Sent operation: " + operation.asJSONString());
        while (true) {

        }
    }

    public StorageController(String host, int port) throws Exception {
        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build());
    }

    public StorageController(ManagedChannel channel) throws Exception {
        this.grpcChannel = channel;
        blockingStub = downloaderGrpc.newBlockingStub(this.grpcChannel);
        dbManager = new DbManager();
        dbManager.connect();
        jgroupsChannel = new JChannel("config.xml");
        jgroupsChannel.setReceiver(this);
    }


    public void connectToChannel() throws Exception {
        jgroupsChannel.connect("ChatCluster");
    }

    public void sync() throws Exception {
        loadLocalOperationsFromDB();
        jgroupsChannel.getState(null, 10000); // will callback local setState
    }

    public void doOperation(Operation operation) throws Exception {
        jgroupsChannel.send(null, operation.asJSONString());
        storeOperationInLocal(operation);
        writeOperationIntoDB(operation);
    }

    public void storeOperationInLocal(Operation operation) {
        operations.add(operation);
    }

    public void viewAccepted(View new_view) {
        System.out.println("Joined View: " + new_view);
    }

    public void receive(Message msg) {
        String line = msg.getObject();
        System.out.println("Received: " + line);
        synchronized(operations) {
            operations.add(Operation.fromJSON(line));
            // TODO update database
        }
    }

    public void setState(InputStream input) throws Exception {
        List<String> newOperations = Util.objectFromStream(new DataInputStream(input));
        synchronized(operations) {
            syncOperations(newOperations);
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized(operations) {
            Util.objectToStream(operations, new DataOutputStream(output));
        }
    }

    public void syncOperations(final List<String> newOperations) throws SQLException {
        for (String op : newOperations)
        {
            if(!operations.contains(op)) {
                final Operation operation = Operation.fromJSON(op);
                operations.add(operation);
                writeOperationIntoDB(operation);
            }
        }

        System.out.println("Operations:");
        for (Operation op : operations) {
            System.out.println(op.asJSONString());
        }
    }

    public void loadLocalOperationsFromDB() throws SQLException {
        try {
            List<Operation> operationsFromDB = dbManager.readEntry();
            for (Operation operation : operationsFromDB) {
                operations.add(operation);
            }
        } catch (SQLException e) {
            System.err.println("Could not read operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void writeOperationIntoDB(Operation operation) throws SQLException {
        try {
            dbManager.insertOperation(operation);
        } catch (Exception e) {
            System.err.println("Could not insert operation: ");
            e.printStackTrace();
            throw new SQLException();
        }
    }

    public void closeGrpcChannel() throws InterruptedException {
        if(grpcChannel != null)
            grpcChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


}
