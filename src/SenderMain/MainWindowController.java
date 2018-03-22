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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextArea textArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            DBConnection dbConnection = new DBConnection();
            choiceBox.setItems(dbConnection.readList(list));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        textArea.setText("Site list loaded from SQLite DB: OK");
    }

    @FXML
    void addGateway() {

        try {
            Stage stage = new Stage();
            Parent root1 = FXMLLoader.load(getClass().getResource("Add Gateway Window.fxml"));
            stage.setTitle("Add a Gateway");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
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
