package com.fcup;

import com.fcup.generated.*;
import com.fcup.utilities.GrpcControllerAdressGetter;
import com.fcup.utilities.GrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.ServerBuilder;
import io.grpc.StatusRuntimeException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class StoragePool {
    private final String STORAGE_FOLDER = "storage";
    private io.grpc.Server server;
    private String localIPAdress;
    private int localGrpcPort;
    private int remoteControllerPort = 50100;
    private ManagedChannel grpcChannel;
    private registererGrpc.registererBlockingStub blockingStub;
    private String remoteControllerAdress;
//    private GrpcServer storageControllerLink;

    public StoragePool() throws Exception {
        localGrpcPort = 50051;
        localIPAdress = getLocalIP();
        remoteControllerAdress = getRemoteControllerAdress();
        createStorageFolderIfNotExists();
        startGrpcServer();
        setUpGrpcClient();
        SeederPool seederPool = new SeederPool(STORAGE_FOLDER);
        DownloaderPool downloaderPool = new DownloaderPool(STORAGE_FOLDER);

        registerInStorageController();
        seederPool.run();
        downloaderPool.run();

        // exit when any key is pressed
        System.in.read();
    }

    private void setUpGrpcClient() {
        this.grpcChannel = ManagedChannelBuilder.forAddress(remoteControllerAdress, remoteControllerPort)
                .usePlaintext(true)
                .build();
        blockingStub = registererGrpc.newBlockingStub(this.grpcChannel);
    }

    private void startGrpcServer() throws Exception {
        boolean started = false;
        while(!started) {
            try {
                server = ServerBuilder.forPort(localGrpcPort)
                        .addService(new GrpcControllerAdressGetter(STORAGE_FOLDER))
                        .build()
                        .start();
                started = true;
            }
            catch(Exception e){
                localGrpcPort++;
            }
        }
        System.out.println("Grpc server started, listening on " + localGrpcPort);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                StoragePool.this.stopServer();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stopServer() {
        if (server != null) {
            server.shutdown();
        }
    }

    void createStorageFolderIfNotExists() throws SecurityException{
        File storage_folder = new File(STORAGE_FOLDER);

        if (!storage_folder.exists()) {
            try{
                storage_folder.mkdir();
            }
            catch(SecurityException se){
                throw se;
            }
        }
    }

    // TODO add GRPC call
    // for sure move this to the GrpcControllerAdressGetter
    private void deleteFile(String chunkName) {
        try {
            Path path = Paths.get(STORAGE_FOLDER + "/" + chunkName);
            Files.delete(path);
        } catch (Exception e) {
            System.err.println("Couldn't erase file" + chunkName);
        }
    }

    // TODO implement grpc call
    public void registerInStorageController() {
        int attempts = 0;
        boolean connected = false;
        while(attempts < 10 && connected == false) {
            PoolInfo request = PoolInfo.newBuilder().setIp(localIPAdress).setPort(localGrpcPort).build(); // todo host = storage pool - how to get it ?
            try {
                this.grpcChannel = ManagedChannelBuilder.forAddress(remoteControllerAdress, remoteControllerPort)
                        .usePlaintext(true)
                        .build();
                blockingStub = registererGrpc.newBlockingStub(this.grpcChannel);
                blockingStub.register(request);
                connected = true;
            } catch (StatusRuntimeException e) {
                attempts ++;
                remoteControllerPort++;
            }
        }

        if (connected == false) {
            System.err.println("Could not register in storage controller");
            System.exit(0);
        }

        System.out.println("Registered as " + localIPAdress + ":" + localGrpcPort + " to " + remoteControllerAdress + ":" + remoteControllerPort);
    }


    public static void main(String[] args) throws Exception {
        StoragePool storagePool = new StoragePool();
    }

    public String getLocalIP() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter IP address of the machine:");
        return scanner.nextLine();
    }

    public String getRemoteControllerAdress() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter IP address of the master controller:");
        return scanner.nextLine();
    }

    public void changeMasterController(String ip, int port) {
        this.remoteControllerAdress = ip;
        this.remoteControllerPort = port;
    }
}
