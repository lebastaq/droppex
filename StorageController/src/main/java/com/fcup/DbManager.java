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
    private final String table = "shards";
    private String dbUrl;

    public DbManager() {
        this("shards-db");
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


    public List<Shard> readEntries() throws SQLException {
        Map<String, String> params = new HashMap<>();
        return readEntries(params);
    }


    public List<Shard> readEntries(Map<String, String> params) throws SQLException {
        List<Shard> results = new ArrayList<>();
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
            Shard shard = new Shard();
            for(int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                shard.changeKeyValue(colName, rs.getString(colName));
            }
            results.add(shard);
        }

        return results;
    }

    public void insertOperation(Shard shard) throws Exception {
        String request = "INSERT INTO " + table + " (";

        // TODO tidy this up
        String separator = "";
        for (String key : shard.getKeys()) {
            request += separator;
            request += "'" + key + "'";
            separator = ",";
        }
        request += ") VALUES(";

        separator = "";
        for (String value : shard.getValues()) {
            request += separator;
            request += "'" + value + "'";
            separator = ",";
        }
        request += ")";
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate( request );
    }

    public void createOperationsTableIfNotExists() throws SQLException {
        String sql = buildCreateTableQuery();
        Statement statement = dbConnection.createStatement();
        statement.execute(sql);
    }

    private String buildCreateTableQuery() {
        Shard shard = new Shard();
        
        String sql = "CREATE TABLE IF NOT EXISTS " + table + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n";

        String separator = "";
        for(String key: shard.getKeys()){
            sql += separator + " " + key + " text";
            separator = ",";
        }
        sql += ");";
        return sql;
    }
}
