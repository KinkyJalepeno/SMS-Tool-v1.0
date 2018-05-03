package Controller;

import SenderMain.DatabaseCommand;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class EditGatewayController {

    @FXML
    private Label siteNameLabel;
    //@FXML private TextField siteNameField;
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField smsPasswordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void submitGatewayDetails() throws SQLException {

        String site = siteNameLabel.getText();
        String address = ipAddressField.getText();
        String pass = smsPasswordField.getText();
        String sqlCommand = ("UPDATE gateways SET ip_address = '" + address + "', password = " +
                "'" + pass + "' WHERE site_name = '" + site + "';");

        System.out.println(sqlCommand);

        if (site.equals("") || address.equals("") || pass.equals("")) {
            errorLabel.setText("Error: Fill in all fields");
        } else {

            DatabaseCommand executeCommand = new DatabaseCommand(sqlCommand);
            executeCommand.command();

            errorLabel.setText("Submit OK");
            errorLabel.setTextFill(Color.GREEN);
        }//end if-else

    }//end submitGatewayDetails method

    public void setInitialValues(String site, String address, String password) {

        siteNameLabel.setText(site);
        ipAddressField.setText(address);
        smsPasswordField.setText(password);

    }
} //end class
