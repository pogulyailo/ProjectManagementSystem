package jdbc;


import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Storage {

    private static String URL = "jdbc:mysql://localhost:3306/dbdz?useSSL=false";
    private static String user = "root";
    private static String password = "root";

    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        Driver driver;

        try{
            driver = new FabricMySQLDriver();
        }
        catch (SQLException e){
         System.out.println("Error when creating the driver");
         return;
        }

        try {
            DriverManager.registerDriver(driver);
        }
        catch (SQLException e){
            System.out.println("Driver failed to register");
            return;
        }

        try {
            connection = DriverManager.getConnection(URL, user, password);
            System.out.println("code");
        }
        catch (SQLException e){
            System.out.println("Driver failed to register");
        }
        finally {
            if(connection != null)
                connection.close();
        }
    }
}
