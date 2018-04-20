package SenderMain;

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

    public String sendRandomText(String mobileNumber) throws IOException {

        output.println("{\"number\":\"" + mobileNumber + "\", \"msg\":\"Random port test\",\"queue_type\":\"master\",\"unicode\":\"5\"}");

        String response = input.readLine();
        //TODO Skip the above server response, it's not needed but store the next
        response = input.readLine();

        return response;
    }
}//end class
