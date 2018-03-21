package SenderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteCommand {

    private Statement stmt;
    private ResultSet rs;


    public ExecuteCommand(String sqlCommand, Connection connection) throws SQLException {

        stmt = connection.createStatement();
        rs = stmt.executeQuery(sqlCommand);

    }

    public void readList(ObservableList<String> list) throws SQLException {

        while (rs.next()) {
            String site = rs.getString("site_name");
            list.addAll(site);
        }

    }
}
