package utils;

import java.io.InputStream;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection {
    private static Connection testConnection;
    private static boolean testMode = false;
    private static final String DB_NAME = "jobTracker.db";

    public static Connection connect() throws SQLException {
        if (testConnection != null) {
            return testConnection; // Use test connection if in test mode
        }

        // Get user home directory or fallback to current directory
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            userHome = "."; // Fallback to current working directory
        }

        // Define database path
        String dbPath = userHome + "/JAT/" + DB_NAME;
        Path dbFilePath = Paths.get(dbPath);
        try {
            if (!Files.exists(dbFilePath)) {
                Files.createDirectories(dbFilePath.getParent());
                try (InputStream is = SQLiteConnection.class.getResourceAsStream("/jobTracker.db")) {
                    if (is != null) {
                        Files.copy(is, dbFilePath);
                    } else {
                        // If no default database provided, create an empty file
                        Files.createFile(dbFilePath);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error initializing the database", e);
        }

        // Connect to the database
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
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







