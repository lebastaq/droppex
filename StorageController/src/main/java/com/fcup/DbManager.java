package com.fcup;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbManager {
    String dbName;
    private Connection dbConnection;
    private final String table = "operations";
    private String dbUrl;

    public DbManager() {
        this("src/main/operations");
    }

    public DbManager(String dbName) {
        this.dbName = dbName;
        String jdbc = "jdbc:sqlite";
        dbUrl = jdbc + ":" + dbName;
    }

    public void connect() throws SQLException, ClassNotFoundException {
        createDBFileIfNotExists();
        loadDriver();
        connectToDB();
        createOperationsTableIfNotExists();
    }

    void connectToDB() throws SQLException {
        dbConnection = DriverManager.getConnection(dbUrl);
    }

    private void loadDriver() throws ClassNotFoundException {
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
    }

    void createDBFileIfNotExists() throws SQLException {
        File f = new File(dbName);
        if(!f.exists()) {
            createDBFile();
        }
    }

    private void createDBFile() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAllEntries(String table) {
        String sMakeSelect = "SELECT response from operations";
        // TODO
    }

    public List<Operation> readEntry(Map<String, String> params) throws SQLException {
        List<Operation> results = new ArrayList<>();
        String query = "SELECT * FROM " + table + " WHERE ";
        String separator = "";
        for(Map.Entry<String, String> param :params.entrySet()){
            query += separator;
            query += param.getKey() + "=";
            query += "'" + param.getValue() + "'";
            separator = " AND ";
        }
        query += ";";
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while (rs.next()) {
            Operation operation = new Operation();
            operation.type = rs.getString("type");
            operation.chunkID = rs.getString("chunkID");
            operation.blockID = rs.getString("blockID");
            operation.destination = rs.getString("destination");
            operation.source = rs.getString("source");
            results.add(operation);
        }

        return results;
    }

    public void insertEntry(List<String> values) throws Exception {
        String request = "INSERT INTO " + table + " (type, chunkID, blockID, destination, source) VALUES(";

        String separator = "";
        for (String value : values) {
            request += separator;
            request += "'" + value + "'";
            separator = ",";
        }
        request += ")";
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate( request );
    }

    public void createOperationsTableIfNotExists() throws SQLException {
        System.out.println("Creating new database file...");
        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	type text,\n"
                + "	chunkID text,\n"
                + "	blockID text,\n"
                + "	destination text,\n"
                + "	source text\n"
                + ");";

        Statement statement = dbConnection.createStatement();
        statement.execute(sql);
    }
}
