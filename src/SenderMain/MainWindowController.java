package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable{

    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextArea textArea;


    String url = "jdbc:sqlite:C://sqlite/sites.db";
    ObservableList list = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SiteList siteList = new SiteList(url, list, choiceBox);
        siteList.readList();

        textArea.setText("Site list loaded from SQLite DB: OK");
    }

    public ChoiceBox<String> getChoiceBox() {
        return choiceBox;
    }

    public ObservableList getList() {
        return list;
    }

    @FXML
    private void addGateway() throws IOException {
        AddGatewayController newGateway = new AddGatewayController();
        newGateway.openAddGatewayWindow();

        System.out.println("Back to here after opening add GW window");
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
