package model;

import java.sql.*;
import utils.SQLiteConnection;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:jobTracker.db";

    // Initialize the SQLite database
    public static void initializeDatabase() {
        try (Connection conn = SQLiteConnection.connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create table for users
                stmt.execute("CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, password TEXT)");

                // Create table for job applications
                stmt.execute("CREATE TABLE IF NOT EXISTS job_applications (company_name TEXT, position TEXT, date_applied DATE)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new user to the database
    public static void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
        }
    }

    // Validate user credentials
    public static boolean validateUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();  // True if user exists
        }
    }
    public static boolean registerUser(String newUsername, String newPassword) {
        String checkUserSql = "SELECT * FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkUserSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {

            // Check if the username already exists
            checkStmt.setString(1, newUsername);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Username already exists
                return false;
            } else {
                // Username does not exist, proceed with registration
                insertStmt.setString(1, newUsername);
                insertStmt.setString(2, newPassword);
                insertStmt.executeUpdate();
                return true; // User registered successfully
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to an exception
        }
    }
}
