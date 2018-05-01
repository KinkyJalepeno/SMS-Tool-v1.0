package Controller;

import SenderMain.DatabaseCommand;
import SenderMain.GetConnection;
import SenderMain.JsonJob;

import Threads.allPortsOfCard;

import Threads.sendToAllPortsOfAllCards;
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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.text.Collator;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {


    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private TextArea textArea;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private Label serverStatusLabel;
    @FXML
    private TextField mobileNumberField;
    @FXML
    private TextField cardField;
    @FXML
    private TextField portField;
    @FXML
    private TextField cardAddressField;
    @FXML
    private TextField numberOfCardsField;

    private String site;
    private String address;
    private String pass;
    private Socket socket;

    private int port = 63333;

    // **** This inits the site list in the drop-down box ****
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            getSiteList();
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        choiceBox.setItems(new SortedList<String>(list, Collator.getInstance()));
        textArea.appendText("SQLite loaded\n");
    }


    // **** This section contains SQLIte operations ****
    public void readDatabaseRow() {

        String sqlCommand = "SELECT * FROM gateways WHERE site_name = '" + choiceBox.getValue() + "';";
        //System.out.println(sqlCommand);

        DatabaseCommand sendCommand = null;
        try {
            sendCommand = new DatabaseCommand(sqlCommand);
            sendCommand.readRow();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //sendCommand.readRow();

        this.site = sendCommand.getSite();
        this.address = sendCommand.getAddress();
        this.pass = sendCommand.getPassword();

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
        if (gateWay == null) {
            textArea.setText("You must choose an entry to delete from drop-down");
        } else {
            String sqlCommand = "DELETE FROM gateways WHERE site_name = '" + gateWay + "';";
            //textArea.setText(sqlCommand);

            DatabaseCommand executeCommand = new DatabaseCommand(sqlCommand);
            executeCommand.command();

            textArea.appendText(gateWay + " successfully deleted from DB\n");
            getSiteList();
            choiceBox.setValue("");
        }
    }

    @FXML
    private void editGateway() {

        String site = choiceBox.getValue();
        if (site == null) {
            textArea.setText("Please select a site to edit !");
        } else {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/EditGatewayWindow.fxml"));
                Parent root1 = loader.load();

                readDatabaseRow();

                EditGatewayController controller = loader.getController();
                controller.setInitialValues(site, address, pass);

                stage.setTitle("Add a Gateway");
                stage.setScene(new Scene(root1));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // **** This section sets up comms with selected site ****
    @FXML
    private void connectToSite() {

        String choice = choiceBox.getValue();
        if (choice == null) {
            textArea.appendText("You must select a site from the drop-down first !\n");
            return;
        } else {
            readDatabaseRow();

            System.out.println(site + " " + address + " " + pass);
        }
        try {
            socket = new Socket(address, port);

            GetConnection conn = new GetConnection(socket);
            String authenticationResult = conn.authenticate(pass);
            textArea.setText(authenticationResult + "\n\n");

            connectionStatusLabel.setText("Connected");
            connectionStatusLabel.setTextFill(Color.GREEN);

            getServerStatus();

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Check server details, connection timed out...");
        }

    }//end connect to site method

    private void getServerStatus() throws IOException {

        GetConnection conn = new GetConnection(socket);
        String response = conn.getServerStatus();

        JsonJob getStatus = new JsonJob(response);
        getStatus.parseStatus();

        String serverStatus = getStatus.getServerCurrentStatus();
        serverStatusLabel.setText(getStatus.getServerCurrentStatus());

        if (serverStatus.equals("Running")) {
            serverStatusLabel.setTextFill(Color.GREEN);
        } else {
            serverStatusLabel.setTextFill(Color.RED);
        }

        textArea.appendText("Email2SMS Service: " + getStatus.getEmail2smsStatus() + "\n");
        textArea.appendText("Email Service: " + getStatus.getEmailStatus() + "\n");
        textArea.appendText("MySql Service: " + getStatus.getMySqlStatus() + "\n\n");

    }

    private void displayResult(String response) {

        JsonJob job = new JsonJob(response);
        job.parseSendResponse();

        textArea.appendText("Number: \t\t" + job.getNumber() + "\n");
        textArea.appendText("Card Add: \t" + job.getCardAddress() + "\n");
        textArea.appendText("Port Num: \t" + job.getPortNumber() + "\n");
        textArea.appendText("Result: \t\t" + job.getReply() + "\n\n");

    }

    private void getServerRunPauseStatus() throws IOException{

        GetConnection conn = new GetConnection(socket);
        String response = conn.getServerStatus();

        JsonJob getStatus = new JsonJob(response);
        getStatus.parseStatus();

        String serverStatus = getStatus.getServerCurrentStatus();
        serverStatusLabel.setText(getStatus.getServerCurrentStatus());

        if (serverStatus.equals("Running")) {
            serverStatusLabel.setTextFill(Color.GREEN);
        } else {
            serverStatusLabel.setTextFill(Color.RED);
        }
    }


    // **** This section is for main UI button operations ****
    @FXML
    private void clearTextArea() {

        textArea.clear();
    }

    @FXML
    private void sendToRandomPort() throws IOException {

        textArea.setText("Sending..................\n\n");
        String mobileNumber = mobileNumberField.getText();
        if (mobileNumber.equals("")) {
            textArea.setText("You must enter a mobile number first !");
        } else {

            GetConnection connection = new GetConnection(socket);
            String response = connection.sendRandomText(mobileNumber);

            displayResult(response);

        }

    }

    @FXML
    private void sendToSpecificCardPort() throws IOException {

        String connectionStatus = connectionStatusLabel.getText();
        if (connectionStatus.equals("Disconnected")) {
            textArea.setText("Connect to a gateway first you tard !");
            return;
        }

        boolean mobileCheck = mobileNumberCheck();
        if (mobileCheck == false) {
            return;
        } else {
            boolean cardAddressCheck = cardAddressCheck();
            if (cardAddressCheck == false) {
                return;
            } else {
                boolean portCheck = portNumberCheck();
                if (portCheck == false) {
                    return;
                } else {
                    GetConnection connection = new GetConnection(socket);
                    String response = connection.sendToCardPort(mobileNumberField.getText(), cardField.getText(), portField.getText());

                    displayResult(response);
                }
            }
        }
    }

    @FXML
    private void sendToAllPortsOfCard() {

        String connectionStatus = connectionStatusLabel.getText();
        if (connectionStatus.equals("Disconnected")) {
            textArea.setText("Connect to a gateway first you tard !");
            return;
        }

        boolean mobileCheck = mobileNumberCheck();
        if (mobileCheck == false) {
            return;
        } else {
            boolean cardAddressCheck = cardAddressCheckTwo();
            if (cardAddressCheck == false) {
                return;
            } else {
                String cardAdd = cardAddressField.getText();
                int card = Integer.parseInt(cardAdd);
                (new Thread(new allPortsOfCard(socket, card, mobileNumberField.getText(), textArea))).start();
            }
        }
    }

    @FXML
    private void sendToAllPortsOfAllCards(){

        textArea.setText("Sending..........................\n\n");

        String connectionStatus = connectionStatusLabel.getText();
        if (connectionStatus.equals("Disconnected")) {
            textArea.setText("Connect to a gateway first you tard !");
            return;
        }
        String numCards = numberOfCardsField.getText();
        if(numCards.equals("")){
            textArea.setText("Number of cards field must be 1 - 8");
            return;
        }
        int numberOfCards = Integer.parseInt(numberOfCardsField.getText());
        if(numberOfCards < 1 || numberOfCards >8){
            textArea.setText("Enter number of GSM cards in system 1 - 8");
            return;
        }
        boolean mobileCheck = mobileNumberCheck();
        if (mobileCheck == false) {
            return;
        } else {
            (new Thread(new sendToAllPortsOfAllCards(socket, mobileNumberField.getText(), textArea,numberOfCards))).start();
        }

    }

    @FXML
    private void queryGeneralQueue() throws IOException {

        GetConnection connection = new GetConnection(socket);
        String response = connection.queryGeneralQueue();

        JsonJob parseQueueQuery = new JsonJob(response);
        parseQueueQuery.parseQueueQuery();

        textArea.appendText("Number of SMS in general queue: " + parseQueueQuery.getQueueLength() + "\n");

    }

    @FXML
    private void flushGeneralQueue() throws IOException {

        GetConnection connection = new GetConnection(socket);
        String response = connection.flushGeneralQueue();

        System.out.println("response = " + response);

        JsonJob getFlushResponse = new JsonJob(response);
        getFlushResponse.parseFlushResponse();

        textArea.appendText("Flush queue response: " + getFlushResponse.getReply() + "\n\n");
        queryGeneralQueue();

    }

    @FXML
    private void queryMasterQueue() throws IOException {

        GetConnection connection = new GetConnection(socket);
        String response = connection.queryMasterQueue();

        System.out.println("response = " + response);

        JsonJob parseQueueQuery = new JsonJob(response);
        parseQueueQuery.parseQueueQuery();

        textArea.appendText("Number of SMS in Master queue: " + parseQueueQuery.getQueueLength() + "\n");
    }

    @FXML
    private void flushMasterQueue() throws IOException {

        GetConnection connection = new GetConnection(socket);
        String response = connection.flushMasterQueue();

        System.out.println("response = " + response);

        JsonJob getFlushResponse = new JsonJob(response);
        getFlushResponse.parseFlushResponse();

        textArea.appendText("Flush queue response: " + getFlushResponse.getReply() + "\n\n");
        queryMasterQueue();


    }

    @FXML
    private void backupSMSQueue() throws IOException {

        GetConnection connection = new GetConnection(socket);
        String response = connection.backupSMSQueue();

        System.out.println("response = " + response);

        JsonJob parseBackupResponse = new JsonJob(response);
        parseBackupResponse.parseFlushResponse();

        textArea.appendText("Queue backup response: " + parseBackupResponse.getReply() + "\n");
    }

    @FXML
    private void pauseServer() throws IOException{

        GetConnection connection = new GetConnection(socket);
        String response = connection.pauseServer();

        System.out.println("response = " + response);

        JsonJob parsePauseRequest = new JsonJob(response);
        parsePauseRequest.parseFlushResponse();

        textArea.setText("Server pause request: " + parsePauseRequest.getReply() +"\n\n");

        getServerRunPauseStatus();
    }

    @FXML
    private void runServer() throws IOException{

        GetConnection connection = new GetConnection(socket);
        String response = connection.runServer();

        System.out.println("response = " + response);

        JsonJob parseRunRequest = new JsonJob(response);
        parseRunRequest.parseFlushResponse();

        textArea.setText("Server Run request: " + parseRunRequest.getReply() +"\n\n");

        getServerRunPauseStatus();
    }

    @FXML
    private void setScheduled() throws IOException{

        GetConnection connection = new GetConnection(socket);
        String response = connection.setScheduled();

        System.out.println("response = " + response);

        JsonJob parseScheduledRequest = new JsonJob(response);
        parseScheduledRequest.parseFlushResponse();

        textArea.setText("Request to set Scheduled mode: " + parseScheduledRequest.getReply() +"\n\n");
    }

    //Sanity checks before going to send phase
    private boolean mobileNumberCheck() {

        String number = mobileNumberField.getText();
        if (number.equals("")) {
            textArea.setText("Enter number to send to Numpty \n\n");
            return false;
        }
        return true;
    }

    private boolean cardAddressCheck() {

        String card = cardField.getText();
        if (card.equals("")) {
            textArea.setText("Enter a card address !");
            return false;
        } else {

            int cardAddress = Integer.parseInt(cardField.getText());
            if (cardAddress < 21 || cardAddress > 28) {
                textArea.setText("Card address must be between 21 and 28 !");
                return false;
            }
        }
        textArea.setText("Sending................ \n\n");
        return true;
    }

    private boolean cardAddressCheckTwo(){


        String card = cardAddressField.getText();
        if (card.equals("")) {
            textArea.setText("Enter a card address !");
            return false;
        } else {

            int cardAddress = Integer.parseInt(cardAddressField.getText());
            if (cardAddress < 21 || cardAddress > 28) {
                textArea.setText("Card address must be between 21 and 28 !");
                return false;
            }
        }
        textArea.setText("Sending................ \n\n");
        return true;
    }

    private boolean portNumberCheck() {

        String card = portField.getText();
        if (card.equals("")) {
            textArea.setText("Enter a port number !\n\n");
            return false;
        } else {

            int cardAddress = Integer.parseInt(portField.getText());
            if (cardAddress < 1 || cardAddress > 4) {
                textArea.setText("Port number must be between 1 and 4 !\n\n");
                return false;
            }
        }
        textArea.setText("Sending................ \n\n");
        return true;

    }



}//End class
