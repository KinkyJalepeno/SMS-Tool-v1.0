package SenderMain;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

import java.sql.*;

public class SiteList {

    private ChoiceBox<String> choiceBox;
    private String url;
    private ObservableList<String> list;

    public SiteList(String url, ObservableList list, ChoiceBox<String> choiceBox) {

        this.choiceBox = choiceBox;
        this.url = url;
        this.list = list;

    }


    public void readList() {

        list.clear();
        choiceBox.setValue(null);

        try {
            Connection conn = DriverManager.getConnection(url);

            //console.appendText("SQLite DB is connected\n" );
            String sql = "SELECT * FROM gateways";//construct db query

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {

                String site = rs.getString("site_name");
                list.addAll(site);

            }
            choiceBox.setItems(list);//add site names to list
            //textArea.appendText("Site data ready\n");

            conn.close();

        } catch (SQLException s) {
            System.out.println(s.getMessage());
        }


    }//end read list


}
