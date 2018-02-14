package jdbc;

import java.sql.*;

public class Storage {
    private String connectionURL = "jdbc:mysql://localhost:3306/dbdz?useSSL=false";
    private String user = "root";
    private String password = "root";

    private Connection connection;
    private Statement statement;

    private PreparedStatement selectSt;
    private PreparedStatement updateSt;

    public Storage() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        }

        try {
            connection = DriverManager.getConnection(connectionURL, user, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Storage storage = new Storage();

        //storage.startMenu(storage);
        //storage.addNewTable(new);
        //storage.addNewDeveloper("TestFirstName", "TestLastName" , 50, "Male" , 1000);
        //storage.addNewProject("newTestProject", "test1", 1000);
        //storage.addNewCustomer("Customer", privat);
    }
}
