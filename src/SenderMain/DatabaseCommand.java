package SenderMain;

import java.sql.*;

public class DatabaseCommand {

    private Connection conn;
    private String url;
    private String sqlCommand;
    private Statement stmt;

    private String site;
    private String address;
    private String pass;


    public DatabaseCommand(String sqlCommand) throws SQLException {

        url = "jdbc:sqlite:C://sqlite/sites.db";
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();
        this.sqlCommand = sqlCommand;


    }//end set up connection constructor

    public void command() {
        try {
            //Statement stmt = conn.createStatement();
            stmt.executeQuery(sqlCommand);

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }//end command execution


    public String getSite(){

        return site;
    }

    public String getAddress(){

        return address;
    }

    public String getPassword(){

        return pass;
    }

    public void readRow() {
        try {
            //Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);

            site = rs.getString("site_name");
            address = rs.getString("ip_address");
            pass = rs.getString("password");

        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }
}
