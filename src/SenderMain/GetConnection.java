package SenderMain;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetConnection {

    private final PrintWriter output;
    private final BufferedReader input;

    public GetConnection(Socket socket) throws IOException {

        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public String authenticate(String pass) throws IOException {
        Command authenticationCommand = new Command("authentication", "\"server_password\":\"" + pass + "\"");
        return executeCommand(authenticationCommand);

    }

    public String backupSMSQueue() throws IOException {
        Command backupSMSQueue = new Command("method", "\"save_q_sms_msg\"");
        return executeShortCommand(backupSMSQueue);
    }

    public String flushGeneralQueue() throws IOException {

        Command flushGeneralQueue = new Command("method", "\"delete_queue\"");
        return executeShortCommand(flushGeneralQueue);
    }

    public String flushMasterQueue() throws IOException {
        Command flushMasterQueue = new Command("delete_queue", "\"queue_type\":\"master\"");
        return executeQueueCommand(flushMasterQueue);
    }

    public String queryGeneralQueue() throws IOException {

        Command queryGeneralQueue = new Command("method", "\"get_q_size\"");
        return executeShortCommand(queryGeneralQueue);
    }

    public String queryMasterQueue() throws IOException {
        Command queryMasterQueue = new Command("get_q_size", "\"queue_type\":\"master\"");
        return executeQueueCommand(queryMasterQueue);

    }

    public String pauseServer() throws IOException {

        Command pauseServer = new Command("method", "\"pause_server\"");
        return executeShortCommand(pauseServer);

    }

    public String runServer() throws IOException {

        Command runServer = new Command("method", "\"run_server\"");
        return executeShortCommand(runServer);
    }

    public String setScheduled() throws IOException {

        Command setScheduled = new Command("set_config_params", "\"server_status\":\"scheduled\"");
        return executeQueueCommand(setScheduled);
    }

    private String executeShortCommand(Command command) throws IOException {

        output.println(command.shortBuild());

        return input.readLine();
    }

    private String executeQueueCommand(Command command) throws IOException {

        output.println(command.build());

        return input.readLine();
    }

    private String executeCommand(Command command) throws IOException {

        output.println(command.build());
        input.readLine();

        output.println("");
        return input.readLine();
    }

    public String getServerStatus() throws IOException {

        output.println("{\"method\":\"get_server_status\"}");
        String response = input.readLine();

        return response;
    }

    public void sendRandomText(String mobileNumber, TextArea textArea) throws IOException {

        output.println("{\"number\":\"" + mobileNumber + "\", \"msg\":\"Random port test\"," +
                "\"queue_type\":\"master\",\"unicode\":\"5\"}");

        String response = input.readLine();
        //TODO Skip the above server response, it's not needed but store the next
        response = input.readLine();

        JsonJob job = new JsonJob(response);
        job.parseSendResponse();

        textArea.appendText("Card:\t\t" + job.getCardAddress() + "\nPort:\t\t\t" + job.getPortNumber() + "\nReply:\t\t" +
                job.getReply() + "\nError Code:\t" + job.getErrorCode() +"\n");
    }

    public void sendToCardPort(String mobileNumber, String card, String port, TextArea textArea) throws IOException {

        int cardAddress = Integer.parseInt(card);
        int portNumber = Integer.parseInt(port);

        output.println("{\"number\": \"" + mobileNumber + "\",\"msg\": \"Sent from: " + cardAddress + "#" + portNumber +
                "\",\"unicode\":\"5\",\"send_to_sim\":\"" + cardAddress + "#" + portNumber + "\",\"queue_type\":\"master\"}");

        String response = input.readLine();
        //TODO Skip the above server response, it's not needed but store the next
        response = input.readLine();

        JsonJob job = new JsonJob(response);
        job.parseSendResponse();

        textArea.appendText("Card:\t\t" + job.getCardAddress() + "\nPort:\t\t\t" + job.getPortNumber() + "\nReply:\t\t" +
                job.getReply() + "\nError Code:\t" + job.getErrorCode() +"\n");
    }

    public void sendToAllCardsPorts(String mobileNumber, int card, TextArea textArea) throws IOException {

        for (int i = 1; i < 5; i++) {

            output.println("{\"number\":\"" + mobileNumber + "\",\"msg\":\"Sent from: " + card + "#" + i +
                    "\",\"unicode\":\"5\",\"send_to_sim\":\"" + card + "#" + i + "\",\"queue_type\":\"master\"}");


            String response = input.readLine();
            //TODO ignore first response and move on.
            response = input.readLine();

            JsonJob job = new JsonJob(response);
            job.parseSendResponse();

            textArea.appendText("Card: " + job.getCardAddress() + " Port: " + job.getPortNumber() + " Reply: " +
                    job.getReply() + " Error Code: " + job.getErrorCode() +"\n");
        }

    }

    public void sendToAllPortsAndCards(String mobile, TextArea textArea, int numberOfCards) throws IOException {

        numberOfCards = numberOfCards + 20;

        for (int card = 21; card <= numberOfCards; card++) {
            for (int port = 1; port < 5; port++) {
                output.println("{\"number\":\"" + mobile + "\",\"msg\":\"Sent from: " + card + "#" + port +
                        "\",\"unicode\":\"5\",\"send_to_sim\":\"" + card + "#" + port + "\",\"queue_type\":\"master\"}");

                String response = input.readLine();
                response = input.readLine();

                System.out.println(response);

                JsonJob job = new JsonJob(response);
                job.parseSendResponse();

                textArea.appendText("Card: " + job.getCardAddress() + " Port: " +
                        job.getPortNumber() + " Reply: " + job.getReply() + " Code: " + job.getErrorCode() +"\n");
            }
        }
    }

}//end class
