package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.sql.*;

public class ReadSiteList {

    private String url;
    private ObservableList list;
    private ChoiceBox<String> choiceBox;
    private TextArea textArea;


    public ReadSiteList(ChoiceBox<String> choiceBox, TextArea textArea) {

        this.choiceBox = choiceBox;
        this.textArea = textArea;

        url = "jdbc:sqlite:C://sqlite/sites.db";
        list = FXCollections.observableArrayList();
    }

    public void readDB() {

        list.clear();
        choiceBox.setValue(null);

        try {
            Connection conn = DriverManager.getConnection(url);

            //console.appendText("SQLite DB is connected\n" );
            String sql = "SELECT * FROM gateways";//construct db query

            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {

                String site = rs.getString("site_name");
                list.addAll(site);

            }
            choiceBox.setItems(list);//add site names to list
            textArea.appendText("Site data ready\n");

            conn.close();

        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }

    }
}
