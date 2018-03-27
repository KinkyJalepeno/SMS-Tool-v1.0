package SenderMain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseCommand {

    private Connection conn;
    private String url;
    private String sqlCommand;


    public DatabaseCommand(String sqlCommand) throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        conn = DriverManager.getConnection(url);
        this.sqlCommand = sqlCommand;

    }//end set up connection constructor

    public void command() {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeQuery(sqlCommand);
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }//end command execution
}
