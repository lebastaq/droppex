package utilities;

import java.util.ArrayList;
import java.util.List;

public class StoragePool {
    String ip;
    int port;

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
}
