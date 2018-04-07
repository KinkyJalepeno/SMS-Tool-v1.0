package SenderMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetConnection implements AutoCloseable{

    private int port = 63333;


    private final Socket socket;
    private final PrintWriter output;
    private final BufferedReader input;

    public GetConnection(String address) throws IOException {

        socket = new Socket(address, port);
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }//TODO This is where I'm up to on initial connection - needs the method and data
    public String getServerStatus(String pass) throws IOException {
        Command getStatusCommand = new Command("PLACEHOLDER", "PLACEHOLDER");
        return executeCommand(getStatusCommand);
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



    @Override
    public void close() throws Exception {
        socket.close();
        input.close();
        output.close();
    }


}//end class
