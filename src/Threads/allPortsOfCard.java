package Threads;

import SenderMain.GetConnection;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;

public class allPortsOfCard implements Runnable {

    private Socket socket;
    private int card;
    private String mobile;
    private TextArea textArea;

    public allPortsOfCard(Socket socket, int card, String mobile, TextArea textArea) {

        this.socket = socket;
        this.card = card;
        this.mobile = mobile;
        this.textArea = textArea;

    }

    public void run(){
        try {
            GetConnection connection = new GetConnection(socket);
            connection.sendToAllCardsPorts(mobile, card, textArea);

        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Check gateway connection - no response");
        }
    }
}
