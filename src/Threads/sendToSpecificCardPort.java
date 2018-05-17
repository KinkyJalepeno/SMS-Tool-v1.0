package Threads;

import SenderMain.GetConnection;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.Socket;

public class sendToSpecificCardPort implements Runnable {

    private Socket socket;
    private String cardAddress;
    private String port;
    private TextArea textArea;
    private String mobileNumber;



    public sendToSpecificCardPort(Socket socket, String cardAddress, String port, String mobileNumber, TextArea textArea) {

        this.socket = socket;
        this.cardAddress = cardAddress;
        this.port = port;
        this.textArea = textArea;
        this.mobileNumber = mobileNumber;

    }

    @Override
    public void run() {

        GetConnection connection = null;
        try {
            connection = new GetConnection(socket);
            connection.sendToCardPort(mobileNumber, cardAddress, port, textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
