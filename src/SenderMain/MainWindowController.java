package SenderMain;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextArea textArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            DBConnection connection = new DBConnection(choiceBox);
            connection.readList();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    @FXML
    private void addGateway() throws IOException {

        AddGatewayController newGateway = new AddGatewayController();
        newGateway.openAddGatewayWindow();



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
