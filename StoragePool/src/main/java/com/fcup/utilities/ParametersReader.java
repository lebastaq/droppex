package com.fcup.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ParametersReader {

    private String CONFIG_FILE  = "networkconf.json";

    public ParametersReader(String CONFIG_FILE) {
        this.CONFIG_FILE = CONFIG_FILE;
    }

    public JSONObject readParameters()  {
        JSONObject file = null;

        try {
            JSONObject fromSrcMain = readParametersFromFile("src/main/resources/" + CONFIG_FILE);
            file = fromSrcMain;
        } catch (IOException e) {
        }

        try {
            JSONObject fromRsc = readParametersFromFile("resources/" + CONFIG_FILE);
            file = fromRsc;
        } catch (IOException e) {
        }

        if (file == null) {
            System.out.println("Could not read config file");
        }

        return file;
    }

    private JSONObject readParametersFromFile(String fileName) throws IOException {
        JSONObject result = null;

        File file = new File(fileName);
        String content = FileUtils.readFileToString(file, "utf-8");
        result = new JSONObject(content);

        return result;
    }
}
