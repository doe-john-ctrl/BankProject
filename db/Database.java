package db;
import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/banksdatabase";
    private static final String USER = "root";
    private static final String PASS = "your_password";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}