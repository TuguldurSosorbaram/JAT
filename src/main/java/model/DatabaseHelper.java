package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

                // Create table for job applications with the specified columns
                stmt.execute("CREATE TABLE IF NOT EXISTS job_applications (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "position TEXT, " +
                        "company_name TEXT, " +
                        "salary_approximation REAL, " +
                        "location TEXT, " +
                        "status TEXT, " + 
                        "date_saved DATE, " +
                        "deadline DATE, " +
                        "date_applied DATE, " +
                        "follow_up DATE, " +
                        "excitement INTEGER)");

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
    // Add a new job application to the database
    public static void addJobApplication(JobApplication job) throws SQLException {
        String sql = "INSERT INTO job_applications(position, company_name, salary_approximation, location, status, " +
                "date_saved, deadline, date_applied, follow_up, excitement) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, job.getPosition());
            pstmt.setString(2, job.getCompanyName());
            pstmt.setDouble(3, job.getSalaryApproximation());
            pstmt.setString(4, job.getLocation());
            pstmt.setString(5, job.getStatus());
            pstmt.setDate(6, job.getDateSaved());
            pstmt.setDate(7, job.getDeadline());
            pstmt.setDate(8, job.getDateApplied());
            pstmt.setDate(9, job.getFollowUpDate());
            pstmt.setInt(10, job.getExcitement());

            pstmt.executeUpdate();
        }
    }
    public static List<JobApplication> getAllJobApplications() {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM job_applications";

        try (Connection conn = SQLiteConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                JobApplication job = new JobApplication();
                job.setId(rs.getInt("id"));
                job.setPosition(rs.getString("position"));
                job.setCompanyName(rs.getString("company_name"));
                job.setSalaryApproximation(rs.getDouble("salary_approximation"));
                job.setLocation(rs.getString("location"));
                job.setStatus(rs.getString("status"));
                job.setDateSaved(rs.getDate("date_saved"));
                job.setDeadline(rs.getDate("deadline"));
                job.setDateApplied(rs.getDate("date_applied"));
                job.setFollowUpDate(rs.getDate("follow_up"));
                job.setExcitement(rs.getInt("excitement"));
                applications.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    public static void updateJobApplication(JobApplication job) {
        String sql = "UPDATE job_applications SET position = ?, company_name = ?, salary_approximation = ?, location = ?, status = ?, " +
                     "date_saved = ?, deadline = ?, date_applied = ?, follow_up = ?, excitement = ? WHERE id = ?";

        try (Connection conn = SQLiteConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, job.getPosition());
            pstmt.setString(2, job.getCompanyName());
            pstmt.setDouble(3, job.getSalaryApproximation());
            pstmt.setString(4, job.getLocation());
            pstmt.setString(5, job.getStatus());
            pstmt.setDate(6, job.getDateSaved());
            pstmt.setDate(7, job.getDeadline());
            pstmt.setDate(8, job.getDateApplied());
            pstmt.setDate(9, job.getFollowUpDate());
            pstmt.setInt(10, job.getExcitement());
            pstmt.setInt(11, job.getId()); // Assuming the ID is used for identifying the record

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
