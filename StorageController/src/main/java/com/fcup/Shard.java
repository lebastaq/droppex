package com.fcup;

import org.json.JSONObject;
import com.fcup.utilities.*;

import java.util.*;

public class Shard {

    private final Map<String, String> params;

    public void changeKeyValue(String key, String value) {
        params.put(key, value);
    }


    public Shard() {
        params = new HashMap<>();
        String dummy = "dummy";
        changeKeyValue("operationType", dummy);
        changeKeyValue("shardID", dummy);
        changeKeyValue("filename", dummy);
        changeKeyValue("filesize", Integer.toString(0));
        changeKeyValue("storagePoolIP", dummy);
        changeKeyValue("storagePoolPort", Integer.toString(0));
    }

    public List<String> getKeys() {
        List<String> values = new ArrayList<>();
        for(Map.Entry<String, String> param :params.entrySet()){
            values.add(param.getKey());
        }
        return values;
    }

    public List<String> getValues() {
        List<String> values = new ArrayList<>();
        for(Map.Entry<String, String> param :params.entrySet()){
            values.add(param.getValue());
        }
        return values;
    }

    public boolean isDeletionOperation() {
        return params.get("operationType").equals("DEL");
    }

    public String asJSONString() {
        JSONObject json = new JSONObject();

        for(Map.Entry<String, String> param :params.entrySet()){
            json.put(param.getKey(), param.getValue());
        }

        return json.toString();
    }

    public boolean hasShardID(String shardID) {
        return params.get("shardID").equals(shardID);
    }

    public static Shard fromJSON(String operationJSONString) {
        Shard shard = new Shard();
        JSONObject operationJSON = new JSONObject(operationJSONString);

        for (String key : operationJSON.keySet()) {
            String value = (String)operationJSON.get(key);
            shard.changeKeyValue(key, value);
        }

        return shard;
    }

    public void addMyselfToStoragePool(StoragePool storagePool) {
        storagePool.addShard(params.get("shardID"));
    }

    public StoragePool findOrCreateStoragePoolToStoreMe(List<StoragePool> pools) {
        for (StoragePool pool : pools) {
            if(pool.hasIPAndPort(params.get("storagePoolIP"), Integer.parseInt(params.get("storagePoolPort")))) {
                return pool;
            }
        }
        return new StoragePool(params.get("storagePoolIP"), Integer.parseInt(params.get("storagePoolPort")));
    }

    @Override
    public String toString() {
        return asJSONString();
    }

    public String getId() {
        return params.get("shardID");
    }

    public String getFilename() {
        return params.get("filename");
    }

    public Long getFilesize() {
        return Long.parseLong(params.get("filesize"));
    }
}
