package SenderMain;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.SQLException;

public class AddGatewayController {

    @FXML private TextField siteNameField;
    @FXML private TextField ipAddressField;
    @FXML private TextField smsPasswordField;
    @FXML private Label errorLabel;
    private GetSiteList siteList;

    @FXML
    private void submitGatewayDetails() throws SQLException {

        String site = siteNameField.getText();
        String address = ipAddressField.getText();
        String pass = smsPasswordField.getText();
        String sqlCommand = ("INSERT INTO gateways VALUES ('" + site + "','" + address + "','" + pass + "')");

        if(site.equals("") || address.equals("") || pass.equals("")){
            errorLabel.setText("Error: Fill in all fields");
        }else {

            DatabaseCommand executeCommand = new DatabaseCommand(sqlCommand);
            executeCommand.command();

            errorLabel.setText("Submit OK");
            siteList.action();

        }//end if-else

    }//end submitGatewayDetails method

    public void sendGetSiteList(GetSiteList siteList){

        this.siteList = siteList;
    }

} //end class
