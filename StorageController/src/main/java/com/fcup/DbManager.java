package com.fcup;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbManager {
    private final String dbName;
    private Connection dbConnection;
    private final String table = "shards";
    private final String dbUrl;

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
        System.out.println("Connected to db");
    }

    void connectToDB() throws SQLException {
        dbConnection = DriverManager.getConnection(dbUrl);
    }

    private void loadDriver() throws ClassNotFoundException {
        String sDriverName = "org.sqlite.JDBC";
        Class.forName(sDriverName);
    }

    void createDBFileIfNotExists() {
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
        StringBuilder query = new StringBuilder("SELECT * FROM " + table);

        if(params.size() > 0) {
            query = appendParams(query, params);
        }

        query.append(";");

        return runQuery(dbConnection.prepareStatement(query.toString()));

    }

    public List<Shard> searchFiles(String pattern) throws SQLException {
        String query = "SELECT DISTINCT filename, filesize FROM " + table;
        PreparedStatement statement;

        if (pattern.length() > 0) {
            query = query + " WHERE filename LIKE %?%;";
            statement = dbConnection.prepareStatement(query);
            statement.setString(1, pattern);

        } else {
            statement = dbConnection.prepareStatement(query);

        }

        return runQuery(statement);

    }

    private StringBuilder appendParams(StringBuilder query, Map<String, String> params) {
        query.append(" WHERE ");

        String separator = "";
        for(Map.Entry<String, String> param : params.entrySet()){
            query.append(separator);
            query.append(param.getKey()).append("=");
            query.append("'").append(param.getValue()).append("'");
            separator = " AND ";

        }

        return query;

    }

    private List<Shard> runQuery(PreparedStatement statement) throws SQLException {
        List<Shard> results = new ArrayList<>();

        ResultSet rs = statement.executeQuery();
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

    public void insertShard(Shard shard) throws Exception {
        StringBuilder request = new StringBuilder("INSERT INTO " + table + " (");

        // TODO tidy this up
        String separator = "";
        for (String key : shard.getKeys()) {
            request.append(separator);
            request.append("'").append(key).append("'");
            separator = ",";
        }
        request.append(") VALUES(");

        separator = "";
        for (String value : shard.getValues()) {
            request.append(separator);
            request.append("'").append(value).append("'");
            separator = ",";
        }
        request.append(")");
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate(request.toString());
    }

    void createOperationsTableIfNotExists() throws SQLException {
        String query = buildCreateTableQuery();
        Statement statement = dbConnection.createStatement();
        statement.execute(query);
        System.out.println("Created database table");
    }

    private String buildCreateTableQuery() {
        Shard shard = new Shard();
        
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + table + "(\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n");

        String separator = "";
        for(String key: shard.getKeys()){
            sql.append(separator).append(" ").append(key).append(" text");
            separator = ",";
        }
        sql.append(");");
        return sql.toString();
    }

    public void emptyDatabase() throws SQLException {
        String sql = "DROP TABLE `" + table + "`;";
        Statement statement = dbConnection.createStatement();
        statement.execute(sql);
        createDBFileIfNotExists();
    }

    public void deleteFileShards(String file) throws SQLException {
        PreparedStatement statement = dbConnection.prepareStatement(
                "DELETE FROM " + table + " WHERE filename = ?;");

        statement.setString(1, file);

        statement.executeUpdate();

    }
}
