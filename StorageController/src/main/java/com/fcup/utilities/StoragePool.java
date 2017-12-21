package com.fcup.utilities;

import com.fcup.generated.addressgetterGrpc;
import com.fcup.generated.registererGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoragePool {
    String ip;
    int port;
    ManagedChannel grpcChannel;

    public List<String> chunks;

    public StoragePool(String ip, int port) {
        this();
        this.ip = ip;
        this.port = port;
    }

    public StoragePool() {
        chunks = new ArrayList<>();
    }

    public void addChunk(String shardID) {
        if(!shardID.equals("0"))
            addChunkNotNull(shardID);
    }

    public void addChunkNotNull(String shardID) {
        if(!chunks.contains(shardID)) {
            chunks.add(shardID);
            System.out.println("Saving shard " + shardID +" to local storage - pool " + ip + ":" + port);
        }
    }

    public void removeChunk(String chunkID) {
        if(chunks.contains(chunkID)){
            chunks.remove(chunkID);
        }
    }

    public boolean hasNChunks(int n) {
        return (n == chunks.size());
    }

    public boolean containsChunk(String chunk) {
        return chunks.contains(chunk);
    }

    public boolean hasIPAndPort(String storagePoolIP, int storagePoolPort) {
        return ((ip.equals(storagePoolIP)) && (port == storagePoolPort));
    }

    public addressgetterGrpc.addressgetterBlockingStub buildBlockingStub() throws Exception{
        shutDownGrpcChannel();
        System.out.println("Contacting " + ip + ":" + port);
        grpcChannel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext(true)
                .build();
        addressgetterGrpc.addressgetterBlockingStub blockingStub = addressgetterGrpc.newBlockingStub(grpcChannel);

        return blockingStub;
    }

    // todo clean up
    // extract to own class ?
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
}
