package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.text.Collator;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{


    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            getSiteList();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void getSiteList() throws SQLException {

        String sqlCommand = "SELECT * FROM gateways";
        ObservableList list = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C://sqlite/sites.db";
        Connection connection = DriverManager.getConnection(url);

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);

            while (rs.next()) {
                String site = rs.getString("site_name");
                list.addAll(site);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        choiceBox.setItems(new SortedList<String>(list, Collator.getInstance()));
        textArea.setText("Site list successfully loaded from SQLite DB");
    }

    @FXML
    private void addGateway() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Add Gateway Window.fxml"));
            Parent root1 = loader.load();

            AddGatewayController controller = loader.getController();
            controller.sendGetSiteList(() -> {
                try {
                    getSiteList();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            stage.setTitle("Add a Gateway");
            stage.setScene(new Scene(root1));
            stage.show();
            System.out.println("back here after opening window");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void connectToSite(){

    String choice = choiceBox.getValue();
    if (choice == null){
        textArea.appendText("You must select a site from the dropdown first !\n");
        return;
    }else {
        textArea.setText("You selected " + choice);
        }

    }//end connect to site method


}//End class
