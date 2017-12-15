package com.fcup;

import org.json.JSONObject;
import utilities.StoragePool;

import java.util.*;

public class Operation {

    private Map<String, String> params;

    public void changeKeyValue(String key, String value) {
        params.put(key, value);
    }

    public Operation() {
        params = new HashMap<>();
        String dummy = "dummy";
        changeKeyValue("type", dummy);
        changeKeyValue("blockID", dummy);
        changeKeyValue("storagePoolIP", dummy);
        changeKeyValue("storagePoolPort", Integer.toString(0));
        changeKeyValue("source", dummy);
        changeKeyValue("chunkID", dummy);
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

    public String asJSONString() {
        JSONObject json = new JSONObject();

        for(Map.Entry<String, String> param :params.entrySet()){
            json.put(param.getKey(), param.getValue());
        }

        return json.toString();
    }

    public boolean hasChunkIDAndBlockID(String chunkID, String blockID) {
        return (params.get("chunkID").equals(chunkID)
                && params.get("blockID").equals(blockID));
    }

    public static Operation fromJSON(String operationJSONString) {
        Operation operation = new Operation();
        JSONObject operationJSON = new JSONObject(operationJSONString);

        for (String key : operationJSON.keySet()) {
            String value = (String)operationJSON.get(key);
            operation.changeKeyValue(key, value);
        }

        return operation;
    }

    public void addMyselfToStoragePool(StoragePool storagePool) {
        storagePool.addChunk(params.get("chunkID"));
    }

    public StoragePool operationPoolToStoreMe(List<StoragePool> pools) {
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
}
