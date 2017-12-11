package com.fcup;

import org.json.*;

// used as a data structure - no methods
public class Operation {
    public String type;
    public String blockID;
    public int destination;
    public String jsonRepresentation;
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
        operation.jsonRepresentation = buildJSON(operation.type, operation.blockID);

        return operation;
    }

    public static Operation fromData(String type, String blockId) {
        Operation operation = new Operation();

        operation.type = type;
        operation.blockID = blockId;
        operation.jsonRepresentation = buildJSON(operation.type, operation.blockID);

        return operation;
    }

    private static String buildJSON(String type, String blockID) {
        String json =  new String("{\"type\": \"" + type + "\"" +
                ", \"blockID\": \"" + blockID + "\"}");

        return json;
    }
}
