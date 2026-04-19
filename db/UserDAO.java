package db;

import java.sql.*;
import model.User;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/banksdatabase";
    private static final String USER = "root";
    private static final String PASS = "your_password";

    // SAVE USER
    public void saveUser(User user) {
        String query = "INSERT INTO users VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, user.getAccountNumber());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.setDouble(4, user.getBalance());

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // GET USER
    public User getUser(String accNo) {
        String query = "SELECT * FROM users WHERE account_number=?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, accNo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("account_number"),
                        rs.getString("password")
                );

                user.setBalance(rs.getDouble("balance")); // IMPORTANT
                return user;
            }

        } catch (Exception e) {
            System.out.println("Error fetching user: " + e.getMessage());
        }

        return null;
    }

    // UPDATE BALANCE
    public void updateBalance(String accNo, double balance) {
        String query = "UPDATE users SET balance=? WHERE account_number=?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDouble(1, balance);
            ps.setString(2, accNo);

            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error updating balance: " + e.getMessage());
        }
    }
}