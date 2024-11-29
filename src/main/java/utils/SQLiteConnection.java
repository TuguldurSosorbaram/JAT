package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static Connection testConnection;
    private static boolean testMode = false;

    public static Connection connect() throws SQLException {
        if (testConnection != null) {
            return testConnection;
        }
        try {
            return DriverManager.getConnection("jdbc:sqlite:jobTracker.db");
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static void setConnectionForTesting(Connection connection) {
        testConnection = connection;
        testMode = true; // Enable test mode
    }
    public static void clearTestConnection() {
        testConnection = null;
        testMode = false; // Disable test mode
    }
    public static boolean isTestMode() {
        return testMode;
    }
}
