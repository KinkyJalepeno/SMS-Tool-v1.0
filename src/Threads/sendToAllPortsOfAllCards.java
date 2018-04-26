package Threads;

import SenderMain.GetConnection;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;

public class sendToAllPortsOfAllCards implements Runnable {

    private final int numberOfCards;
    private final Socket socket;
    private final String mobile;
    private final TextArea textArea;


    public sendToAllPortsOfAllCards(Socket socket, String mobileNumber, TextArea textArea, int numberOfCards) {

        this.socket = socket;
        this.mobile = mobileNumber;
        this.textArea = textArea;
        this.numberOfCards = numberOfCards;


    }

    public void run() {
        System.out.println("numberOfCards = " + numberOfCards);
        System.out.println("mobile = " + mobile);
        try {
            GetConnection connection = new GetConnection(socket);
            connection.sendToAllPortsAndCards(mobile, textArea, numberOfCards);


        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Check gateway connection - no response");
        }

    }
}
