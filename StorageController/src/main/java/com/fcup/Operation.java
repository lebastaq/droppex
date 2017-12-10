package com.fcup;

import org.json.*;

public class Operation {
    private String type;
    private int blockID;
    private int destination;
//    Source
//    Dest
//    Identifier
//    Data ?

    public Operation() {
    }

    public String toJSON() {
        JSONObject operationAsJson = new JSONObject("{\"type\": \"" + type + "\"" +
                ", \"blockID\": \"" + blockID + "\"}");
        return operationAsJson.toString();
    }
}
