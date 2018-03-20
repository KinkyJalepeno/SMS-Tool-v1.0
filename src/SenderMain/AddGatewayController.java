package SenderMain;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;

public class AddGatewayController {

    @FXML private TextField siteNameField;
    @FXML private TextField ipAddressField;
    @FXML private TextField smsPasswordField;
    @FXML private Label errorLabel;


    public void openAddGatewayWindow() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Add Gateway Window.fxml"));
        primaryStage.setTitle("Add a Gateway");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    @FXML
    private void submitGatewayDetails(){

        String site = siteNameField.getText();
        String address = ipAddressField.getText();
        String pass = smsPasswordField.getText();

        if(site.equals("") || address.equals("") || pass.equals("")){
           errorLabel.setText("Error: Fill in all fields");
        }else {

            String sqlCommand = ("INSERT INTO gateways VALUES ('" + site + "','" + address + "','" + pass + "')");


            try(DBConnection dbConnection = new DBConnection(sqlCommand)) {
                dbConnection.addGateway();

            } catch (Exception e) {
                e.printStackTrace();
            }//end try-catch
        }//end if-else


    }//end submitGatewayDetails method


} //end class
