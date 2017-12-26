package com.fcup.utilities;

import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.deleterGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoragePool {
    private String ip;
    private int port;
    private ManagedChannel grpcChannel;

    public final List<String> shards;

    public StoragePool(String ip, int port) {
        this();
        this.ip = ip;
        this.port = port;
    }

    public StoragePool() {
        shards = new ArrayList<>();
    }

    public void addShard(String shardID) {
        if(!shardID.equals("0"))
            addShardNotNull(shardID);
    }

    private void addShardNotNull(String shardID) {
        if(!shards.contains(shardID)) {
            shards.add(shardID);
            System.out.println("Saving shard " + shardID +" to local storage - pool " + ip + ":" + port);
        }
    }

    public void removeChunk(String shardID) {
        if(shards.contains(shardID)){
            shards.remove(shardID);
        }
    }

    public boolean hasNShards(int n) {
        return (n == shards.size());
    }

    public boolean hasIPAndPort(String storagePoolIP, int storagePoolPort) {
        return ((ip.equals(storagePoolIP)) && (port == storagePoolPort));
    }

    // TODO point of this function not super clear
    public addressgetterGrpc.addressgetterBlockingStub buildMasterSetterBlockingStubAndConnect() {
        shutDownGrpcChannel();
        System.out.println("Contacting " + ip + ":" + port);
        grpcChannel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext(true)
                .build();

        return addressgetterGrpc.newBlockingStub(grpcChannel);
    }

    private void shutDownGrpcChannel() {
        if (grpcChannel != null) {
            if(!grpcChannel.isShutdown()) {
                grpcChannel.shutdownNow();
                try {
                    grpcChannel.awaitTermination(1000, TimeUnit.MICROSECONDS);
                } catch (InterruptedException e) {
                    System.out.println("Could not shutdown channel");
                    e.printStackTrace();
                }
            }
            grpcChannel = null;
        }
    }

    public boolean containsShard(String shardId) {
        return shards.contains(shardId);
    }

    public deleterGrpc.deleterBlockingStub buildDeleterBlockingStub() {
        return deleterGrpc.newBlockingStub(grpcChannel);
    }
}
