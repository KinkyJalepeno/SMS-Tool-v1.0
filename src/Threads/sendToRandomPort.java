package Threads;

import SenderMain.GetConnection;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.Socket;

public class sendToRandomPort implements Runnable{

    private Socket socket;
    private String mobileNumber;
    private TextArea textArea;


    public sendToRandomPort(Socket socket, String mobileNumber, TextArea textArea) {

        this.socket = socket;
        this.mobileNumber = mobileNumber;
        this.textArea = textArea;

    }

    @Override
    public void run() {

        GetConnection connection = null;
        try {
            connection = new GetConnection(socket);
            connection.sendRandomText(mobileNumber, textArea);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
