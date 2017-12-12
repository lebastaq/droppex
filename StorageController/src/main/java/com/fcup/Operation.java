package com.fcup;

import org.json.JSONObject;

import java.util.*;

public class Operation {

    private Map<String, String> params;

    public void changeKeyValue(String key, String value) {
        params.put(key, value);
    }

    public Operation() {
        params = new HashMap<>();
        String dummy = "dummy";
        params.put("type", dummy);
        params.put("chunkID", dummy);
        params.put("blockID", dummy);
        params.put("storagePool", dummy);
        params.put("source", dummy);
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

            System.out.println("key: "+ key + " value: " + value);
            operation.changeKeyValue(key, value);
        }

        return operation;
    }
}
