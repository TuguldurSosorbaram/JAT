package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import utils.SQLiteConnection;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:jobTracker.db";
    
    // Initialize the SQLite database
    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null; 
        try {
            conn = SQLiteConnection.connect();
            if (conn != null) {
                stmt = conn.createStatement();

                // Create table for users
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "username TEXT NOT NULL UNIQUE, " +
                        "password TEXT NOT NULL)");

                // Create table for job applications with the specified columns
                stmt.execute("CREATE TABLE IF NOT EXISTS job_applications (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "position TEXT, " +
                        "company_name TEXT, " +
                        "salary_approximation INTEGER, " +
                        "location TEXT, " +
                        "status TEXT, " + 
                        "date_saved DATE, " +
                        "deadline DATE, " +
                        "date_applied DATE, " +
                        "follow_up DATE, " +
                        "excitement INTEGER,"+
                        "user_id INTEGER NOT NULL, " + 
                        "FOREIGN KEY (user_id) REFERENCES users (id))");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close in production
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Add a new user to the database
    public static void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getHashedPassword());
            pstmt.executeUpdate();
        }finally{
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Validate user credentials
    public static boolean validateUser(String username, String plainPassword) throws SQLException {
        String sql = "SELECT password FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password"); // Get hashed password
                return BCrypt.checkpw(plainPassword, hashedPassword); // Validate password
            }

            return false; // User not found
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException e) {
                e.printStackTrace(); // Log any errors during cleanup
            }
        }
    }

    public static boolean registerUser(String newUsername, String plainPassword) {
        String checkUserSql = "SELECT 1 FROM users WHERE username = ?";
        String insertUserSql = "INSERT INTO users (username, password) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = SQLiteConnection.connect();

            // Check if the username already exists
            checkStmt = conn.prepareStatement(checkUserSql);
            checkStmt.setString(1, newUsername);
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Username already exists
                return false;
            } else {
                // Hash the password
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

                // Insert the new user
                insertStmt = conn.prepareStatement(insertUserSql);
                insertStmt.setString(1, newUsername);
                insertStmt.setString(2, hashedPassword);
                insertStmt.executeUpdate();
                return true; // User registered successfully
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to an exception
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (checkStmt != null) checkStmt.close(); // Close PreparedStatement
                if (insertStmt != null) insertStmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors during cleanup
            }
        }
    }


    // Add a new job application to the database
    public static void addJobApplication(JobApplication job) throws SQLException {
        String sql = "INSERT INTO job_applications(position, company_name, salary_approximation, location, status, " +
                     "date_saved, deadline, date_applied, follow_up, excitement, user_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, job.getPosition());
            pstmt.setString(2, job.getCompanyName());
            pstmt.setInt(3, job.getSalaryApproximation());
            pstmt.setString(4, job.getLocation());
            pstmt.setString(5, job.getStatus());
            pstmt.setDate(6, job.getDateSaved());
            pstmt.setDate(7, job.getDeadline());
            pstmt.setDate(8, job.getDateApplied());
            pstmt.setDate(9, job.getFollowUpDate());
            pstmt.setInt(10, job.getExcitement());
            pstmt.setInt(11, job.getUserId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Re-throw exception to let the caller handle it
        } finally {
            try {
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors during cleanup
            }
        }
    }

    public static void updateJobApplication(JobApplication job) {
        String sql = "UPDATE job_applications SET position = ?, company_name = ?, salary_approximation = ?, location = ?, status = ?, " +
                     "date_saved = ?, deadline = ?, date_applied = ?, follow_up = ?, excitement = ? WHERE id = ? and user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, job.getPosition());
            pstmt.setString(2, job.getCompanyName());
            pstmt.setInt(3, job.getSalaryApproximation());
            pstmt.setString(4, job.getLocation());
            pstmt.setString(5, job.getStatus());
            pstmt.setDate(6, job.getDateSaved());
            pstmt.setDate(7, job.getDeadline());
            pstmt.setDate(8, job.getDateApplied());
            pstmt.setDate(9, job.getFollowUpDate());
            pstmt.setInt(10, job.getExcitement());
            pstmt.setInt(11, job.getId());
            pstmt.setInt(12, job.getUserId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Log any exceptions
        } finally {
            try {
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors during cleanup
            }
        }
    }
    public static void deleteJobApplication(JobApplication jobToDelete) {
        String sql = "DELETE FROM job_applications WHERE id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);

            // Set parameters
            pstmt.setInt(1, jobToDelete.getId()); // Job application ID
            pstmt.setInt(2, jobToDelete.getUserId()); // User ID to ensure ownership

            // Execute the delete operation
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Log any exceptions
        } finally {
            try {
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors during cleanup
            }
        }
    }

    public static List<JobApplication> getJobApplicationsByUser(int userId) {
        List<JobApplication> applications = new ArrayList<>();
        String sql = "SELECT * FROM job_applications WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                JobApplication job = new JobApplication();
                job.setId(rs.getInt("id"));
                job.setPosition(rs.getString("position"));
                job.setCompanyName(rs.getString("company_name"));
                job.setSalaryApproximation(rs.getInt("salary_approximation"));
                job.setLocation(rs.getString("location"));
                job.setStatus(rs.getString("status"));
                job.setDateSaved(rs.getDate("date_saved"));
                job.setDeadline(rs.getDate("deadline"));
                job.setDateApplied(rs.getDate("date_applied"));
                job.setFollowUpDate(rs.getDate("follow_up"));
                job.setExcitement(rs.getInt("excitement"));
                job.setUserId(rs.getInt("user_id")); // Populate userId
                applications.add(job);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving job applications for user: " + userId, e);
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Log errors during cleanup
            }
        }
        return applications;
    }

    public static int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = SQLiteConnection.connect(); // Get connection
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id"); // Return the user ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (pstmt != null) pstmt.close(); // Close PreparedStatement
                if (conn != null && !SQLiteConnection.isTestMode()) conn.close(); // Close connection only if not in test mode
            } catch (SQLException ex) {
                ex.printStackTrace(); // Handle errors during cleanup
            }
        }
        return -1; // Return -1 if user is not found or an error occurs
    }

}
