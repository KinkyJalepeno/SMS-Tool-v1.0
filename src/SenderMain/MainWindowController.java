package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        choiceBox.setItems(list);

    }

    @FXML
    private void addGateway() {

        AddGatewayController controller = new AddGatewayController();
        controller.setMyCallback( () -> {
            try {
                getSiteList();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            Stage stage = new Stage();
            FXMLLoader loader = null;
            Parent root1 = loader.load(getClass().getResource("Add Gateway Window.fxml"));
            stage.setTitle("Add a Gateway");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't load new window");
        }

        System.out.println("back here after opening window");
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
