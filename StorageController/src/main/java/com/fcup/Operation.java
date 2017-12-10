package com.fcup;

import org.json.*;

public class Operation {
    public String type;
    public String blockID;
    public int destination;
    public JSONObject jsonRepresentation;
//    Source
//    Dest
//    Identifier
//    Data ?

    private Operation() {

    }

    public static Operation fromJSON(String inputData) {
        Operation operation = new Operation();
        JSONObject operationJSON = new JSONObject(inputData);

        operation.type = operationJSON.getString("type");
        operation.blockID = operationJSON.getString("blockID");
        operation.jsonRepresentation =  new JSONObject("{\"type\": \"" + operation.type + "\"" +
                ", \"blockID\": \"" + operation.blockID + "\"}");

        return operation;
    }

    public static Operation fromData(String type, String blockId) {
        Operation operation = new Operation();

        operation.type = type;
        operation.blockID = blockId;
        operation.jsonRepresentation =  new JSONObject("{\"type\": \"" + operation.type + "\"" +
                ", \"blockID\": \"" + operation.blockID + "\"}");

        return operation;
    }

}
