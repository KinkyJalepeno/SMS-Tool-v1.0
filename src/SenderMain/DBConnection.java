package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.sql.*;


public class DBConnection {

    private Connection connection;
    private String url;
    private ChoiceBox<String> choiceBox;


    public DBConnection(ChoiceBox<String> choiceBox) throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        connection = DriverManager.getConnection(url);
        this.choiceBox = choiceBox;

    }

    public void readList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sqlCommand = "SELECT * FROM gateways";

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlCommand);

        while (rs.next()) {
            String site = rs.getString("site_name");
            list.addAll(site);
        }
        choiceBox.setItems(list);
    }

    public void addGateway() {


    }

}//end class
