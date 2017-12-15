package utilities;

import java.util.ArrayList;
import java.util.List;

public class StoragePool {
    String ip;
    int port;

    List<String> chunks;

    public StoragePool(int port, String ip) {
        this();
        this.ip = ip;
        this.port = port;
    }

    public StoragePool() {
        chunks = new ArrayList<>();
    }

    public void addChunk(String chunkID) {
        chunks.add(chunkID);
    }

    public void removeChunk(String chunkID) {
        if(chunks.contains(chunkID)){
            chunks.remove(chunkID);
        }
    }

    public boolean hasNChunks(int n) {
        return (n == chunks.size());
    }
}
