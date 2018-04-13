package SenderMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GetConnection implements AutoCloseable{

    private final PrintWriter output;
    private final BufferedReader input;
    private Socket socket;

    public GetConnection(Socket socket) throws IOException {
        this.socket = socket;
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

    @Override
    public void close() throws Exception {
        socket.close();
        input.close();
        output.close();
    }


}//end class
