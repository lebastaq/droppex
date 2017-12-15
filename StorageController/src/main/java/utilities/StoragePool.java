package utilities;

import java.util.ArrayList;
import java.util.List;

public class StoragePool {
    String ip;
    int port;

    List<String> chunks;

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
}
