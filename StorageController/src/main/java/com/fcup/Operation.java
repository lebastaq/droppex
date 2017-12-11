package com.fcup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        params.put("destination", dummy);
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

    public String asJSON() {
        String json =  "{";

        for(Map.Entry<String, String> param :params.entrySet()){
            json += "\"" + param.getKey() + "\":";
            json += "\"" + param.getValue() + "\",";
        }

        json += "}";

        return json;
    }

    public boolean hasChunkIDAndBlockID(String chunkID, String blockID) {
        return (params.get("chunkID").equals(chunkID)
                && params.get("blockID").equals(blockID));
    }
}
