package com.fcup.utilities;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ParametersReader {

    // TODO something about this address ?
    private String CONFIG_FILE  = "src/main/resources/networkconf.json";

    public JSONObject readFromFile()  {
        JSONObject result = new JSONObject();

        try {
            File file = new File(CONFIG_FILE);
            String content = FileUtils.readFileToString(file, "utf-8");
            result = new JSONObject(content);
        } catch (IOException e) {
            System.out.println("Could not read config file");
            e.printStackTrace();
        }

        return result;
    }
}
