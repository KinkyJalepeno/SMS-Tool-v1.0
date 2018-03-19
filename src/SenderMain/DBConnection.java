package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class DBConnection {

    private Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private String url;
    private ObservableList list;


    public DBConnection(String sqlCommand) throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        connection = DriverManager.getConnection(url);
        stmt = connection.createStatement();
        rs = stmt.executeQuery(sqlCommand);
        list = FXCollections.observableArrayList();


    }

    public ObservableList<String> readList() throws SQLException {

        while (rs.next()) {
            String site = rs.getString("site_name");
            this.list.addAll(site);
        }
        return list;
    }


}//end class
