package SenderMain;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.sql.*;


public class DBConnection {

    private Connection connection;
    private Statement stmt;
    private ResultSet rs;
    private String url;


    public DBConnection(String sqlCommand) throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        connection = DriverManager.getConnection(url);
        stmt = connection.createStatement();
        rs = stmt.executeQuery(sqlCommand);


    }

    public void readList(ChoiceBox<String> choiceBox, ObservableList list) throws SQLException {

        String site = null;
        while (rs.next()) {
            site = rs.getString("site_name");
            list.addAll(site);
        }
        choiceBox.setItems(list);
    }
}
