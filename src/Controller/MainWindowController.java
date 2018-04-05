package Controller;

import SenderMain.DatabaseCommand;
import SenderMain.GetConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.Collator;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{


    @FXML private ChoiceBox<String> choiceBox;
    @FXML private TextArea textArea;
    @FXML private Label connectionStatusLabel;
    @FXML private Label serverStatusLabel;

    private String site;
    private String address;
    private String pass;

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
        textArea.appendText("SQLite loaded\n");
    }

    public void readDatabaseRow(){

        String sqlCommand = "SELECT * FROM gateways WHERE site_name = '" + choiceBox.getValue() + "';";
        //System.out.println(sqlCommand);

        DatabaseCommand sendCommand = null;
        try {
            sendCommand = new DatabaseCommand(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendCommand.readRow();

        site = sendCommand.getSite();
        address = sendCommand.getAddress();
        pass = sendCommand.getPassword();

    }

    @FXML
    private void clearTextArea(){

        textArea.clear();
    }
    @FXML
    private void addGateway() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/AddGatewayWindow.fxml"));
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
            //System.out.println("back here after opening window");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void deleteGateway() throws SQLException {

        String gateWay = choiceBox.getValue();
        if(gateWay == null){
            textArea.setText("You must choose an entry to delete from drop-down");
        }else{
            String sqlCommand = "DELETE FROM gateways WHERE site_name = '" + gateWay +"';";
            //textArea.setText(sqlCommand);

            DatabaseCommand executeCommand = new DatabaseCommand(sqlCommand);
            executeCommand.command();

            textArea.appendText(gateWay + " successfully deleted from DB\n");
            getSiteList();
            choiceBox.setValue("");
        }
    }
    @FXML
    private void editGateway(){

        String site = choiceBox.getValue();
        if(site == null){
            textArea.setText("Please select a site to edit !");
        }else {
//            try{
//                Stage stage = new Stage();
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("GUI/EditGatewayWindow.FXML"));
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }






            readDatabaseRow();
        }
    }
    @FXML
    private void connectToSite(){

    String choice = choiceBox.getValue();
    if (choice == null){
        textArea.appendText("You must select a site from the dropdown first !\n");
        return;
    }else {
        //textArea.setText("You selected " + choice);
        readDatabaseRow();
        System.out.println(site + " " + address + " " + pass);
        }
        try (GetConnection conn = new GetConnection(address)){

            String authenticationResult = conn.authenticate(pass);
            textArea.setText(authenticationResult);

        }catch(Exception e){
        e.printStackTrace();
        }

    }//end connect to site method
}//End class
