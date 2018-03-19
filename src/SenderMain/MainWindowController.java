package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

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

        String sqlCommand = "SELECT * FROM gateways";

        try {
            DBConnection dbConnection = new DBConnection(sqlCommand);
            choiceBox.setItems(dbConnection.readList());

            textArea.setText("Sites successfully loaded from SQLite DB.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void connectToSite(){

    String choice = choiceBox.getValue();
    System.out.println(choice);

    }



}//End class
