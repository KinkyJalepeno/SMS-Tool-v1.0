package SenderMain;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;

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
    private void submitGatewayDetails() throws SQLException {

        String site = siteNameField.getText();
        String address = ipAddressField.getText();
        String pass = smsPasswordField.getText();
        String sqlCommand = ("INSERT INTO gateways VALUES ('" + site + "','" + address + "','" + pass + "')");

        if(site.equals("") || address.equals("") || pass.equals("")){
            errorLabel.setText("Error: Fill in all fields");
        }else {

            AddNewGateway addGateway = new AddNewGateway(sqlCommand);
            addGateway.writeToDatabase();

            errorLabel.setText("Submit OK");

            AddGatewayController controller = new AddGatewayController();
            controller.reloadSiteList();

        }//end if-else
    }//end submitGatewayDetails method

    private void reloadSiteList() {
        System.out.print("I'd like to reload the site list");

    }

} //end class
