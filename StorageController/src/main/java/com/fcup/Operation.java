package com.fcup;

import org.json.*;

public class Operation {
    private String type;
    private String blockID;
    private int destination;
//    Source
//    Dest
//    Identifier
//    Data ?

    public Operation() {
    }

    public Operation(String operationString) {
        System.out.println(">>>> " + operationString);
        JSONObject operationJSON = new JSONObject(operationString);
        type = operationJSON.getString("type");
        blockID = operationJSON.getString("blockID");
    }

    public String toJSONInString() {
        JSONObject operationAsJson = new JSONObject("{\"type\": \"" + type + "\"" +
                ", \"blockID\": \"" + blockID + "\"}");
        return operationAsJson.toString();
    }
}
