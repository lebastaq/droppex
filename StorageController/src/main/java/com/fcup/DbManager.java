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
        this("src/main/resources/operations-db");
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

    public List<Operation> readEntry() throws SQLException {
        Map<String, String> params = new HashMap<>();
        return readEntry(params);
    }


    public List<Operation> readEntry(Map<String, String> params) throws SQLException {
        List<Operation> results = new ArrayList<>();
        String query = "SELECT * FROM " + table;
        if(params.size() > 0)
            query += " WHERE ";
        String separator = "";
        // TODO tidy this up
        for(Map.Entry<String, String> param :params.entrySet()){
            query += separator;
            query += param.getKey() + "=";
            query += "'" + param.getValue() + "'";
            separator = " AND ";
        }
        query += ";";
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        while (rs.next()) {
            Operation operation = new Operation();
            for(int i = 1; i < columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                operation.changeKeyValue(colName, rs.getString(colName));
            }
            results.add(operation);
        }

        return results;
    }

    public void insertOperation(Operation operation) throws Exception {
        String request = "INSERT INTO " + table + " (";

        // TODO tidy this up
        String separator = "";
        for (String key : operation.getKeys()) {
            request += separator;
            request += "'" + key + "'";
            separator = ",";
        }
        request += ") VALUES(";

        separator = "";
        for (String value : operation.getValues()) {
            request += separator;
            request += "'" + value + "'";
            separator = ",";
        }
        request += ")";
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate( request );
    }

    public void createOperationsTableIfNotExists() throws SQLException {
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
