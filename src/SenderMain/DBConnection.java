package SenderMain;

import javafx.collections.ObservableList;

import java.sql.*;

public class DBConnection {

    private String url;
    private Connection connection;
    private Statement stmt;
    private ResultSet rs;


    public DBConnection() throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        connection = DriverManager.getConnection(url);
    }

    public ObservableList<String> readList(ObservableList list) throws SQLException {

        String sqlCommand = "SELECT * FROM gateways";

        stmt = connection.createStatement();
        rs = stmt.executeQuery(sqlCommand);

        while (rs.next()) {
            String site = rs.getString("site_name");
            list.addAll(site);
        }
        return list;
    }
}
