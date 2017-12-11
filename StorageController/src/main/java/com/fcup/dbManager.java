package com.fcup;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbManager {
    String dbName;
    private Connection conn;
    private final String dbPath;


    public dbManager(String dbName) throws Exception {
        this.dbName = dbName;
        dbPath = "src/main/resources/";
        checkIfDBExists();
        loadDriver();
        connectToDB();
    }

    public void connectToDB() throws Exception {
        String jdbc = "jdbc:sqlite";
        String dbUrl = jdbc + ":" + dbPath + dbName;
        conn = DriverManager.getConnection(dbUrl);
    }

    private void loadDriver() throws ClassNotFoundException {
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
    }

    private void checkIfDBExists() throws Exception {
        File f = new File(dbPath + dbName);
        if(!f.exists()) {
            throw new FileNotFoundException();
        }
    }

    public void getAllEntriesFromTable(String table) {
        int iTimeout = 30;
        String sMakeTable = "CREATE TABLE dummy (id numeric, response text)";
        String sMakeInsert = "INSERT INTO dummy VALUES(1,'Hello from the database')";
        String sMakeSelect = "SELECT response from dummy";

    }
}
