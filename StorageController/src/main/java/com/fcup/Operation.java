package com.fcup;

import java.util.HashMap;
import java.util.Map;

public class Operation {
    public String type;
    public String chunkID;
    public String blockID;
    public String destination;
    public String source;
    public String ID;

    public Operation() {

    }

    public static String asJSON(Operation operation) {
        Map<String, String> params = new HashMap<>();
        params.put("type", operation.type);
        params.put("chunkID", operation.chunkID);
        params.put("blockID", operation.blockID);
        params.put("destination", operation.destination);
        params.put("source", operation.source);
        params.put("ID", operation.ID);

        return asJSON(params);
    }

    private static String asJSON(Map<String, String> params) {
        String json =  "{";

        for(Map.Entry<String, String> param :params.entrySet()){
            json += "\"" + param.getKey() + "\":";
            json += "\"" + param.getValue() + "\",";
        }

        json += "}";

        return json;
    }
}
