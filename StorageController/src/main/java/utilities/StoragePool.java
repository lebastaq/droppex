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

    public void addChunk(String chunkID) {
        if(!chunks.contains(chunkID)) {
            chunks.add(chunkID);
            System.out.println("Adding chunk " + chunkID +" to pool " + ip + ":" + port);
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
