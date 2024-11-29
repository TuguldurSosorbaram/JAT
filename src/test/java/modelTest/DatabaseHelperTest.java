package modelTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SQLiteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import model.DatabaseHelper;
import model.JobApplication;
import model.User;

import static org.junit.jupiter.api.Assertions.*;
public class DatabaseHelperTest {
    private Connection connection;
    
    @BeforeEach
    void setUp() throws SQLException {
        // Use in-memory SQLite database for testing
        this.connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);

        // Initialize database schema
        DatabaseHelper.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        SQLiteConnection.connect().close(); // Close the test connection
        SQLiteConnection.clearTestConnection(); // Clear the test mode
    }
    @Test
    void testAddAndValidateUser() throws SQLException {
        // Arrange
        User user = new User("testUser", "testPassword");

        // Act
        DatabaseHelper.addUser(user);

        // Assert: Validate user credentials
        assertTrue(DatabaseHelper.validateUser("testUser", "testPassword"));
        assertFalse(DatabaseHelper.validateUser("testUser", "wrongPassword"));
        assertFalse(DatabaseHelper.validateUser("unknownUser", "testPassword"));
    }
    @Test
    void testRegisterUser() throws SQLException {
        // Act
        boolean registered = DatabaseHelper.registerUser("newUser", "newPassword");
        boolean duplicate = DatabaseHelper.registerUser("newUser", "newPassword");

        // Assert
        assertTrue(registered);
        assertFalse(duplicate);
    }
    @Test
    void testAddJobApplication() throws SQLException {
        // Arrange
        User user = new User("jobUser", "password123");
        DatabaseHelper.addUser(user);
        int userId = DatabaseHelper.getUserIdByUsername("jobUser");

        JobApplication job = new JobApplication(
            "Developer", "TechCorp", 80000, "Remote", "Applied",
            null, null, null, null, 4, userId
        );

        // Act
        DatabaseHelper.addJobApplication(job);
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(userId);

        // Assert
        assertEquals(1, jobs.size());
        JobApplication savedJob = jobs.get(0);
        assertEquals("Developer", savedJob.getPosition());
        assertEquals("TechCorp", savedJob.getCompanyName());
        assertEquals(80000, savedJob.getSalaryApproximation());
        assertEquals("Remote", savedJob.getLocation());
        assertEquals("Applied", savedJob.getStatus());
        assertEquals(4, savedJob.getExcitement());
        assertEquals(userId, savedJob.getUserId());
    }
    @Test
    void testUpdateJobApplication() throws SQLException {
        // Arrange
        User user = new User("updateUser", "password456");
        DatabaseHelper.addUser(user);
        int userId = DatabaseHelper.getUserIdByUsername("updateUser");

        JobApplication job = new JobApplication(
            "Tester", "SoftCorp", 60000, "Onsite", "Applied",
            null, null, null, null, 3, userId
        );
        DatabaseHelper.addJobApplication(job);

        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(userId);
        JobApplication jobToUpdate = jobs.get(0);
        jobToUpdate.setPosition("Senior Tester");
        jobToUpdate.setSalaryApproximation(70000);
        jobToUpdate.setExcitement(5);

        // Act
        DatabaseHelper.updateJobApplication(jobToUpdate);
        List<JobApplication> updatedJobs = DatabaseHelper.getJobApplicationsByUser(userId);

        // Assert
        assertEquals(1, updatedJobs.size());
        JobApplication updatedJob = updatedJobs.get(0);
        assertEquals("Senior Tester", updatedJob.getPosition());
        assertEquals(70000, updatedJob.getSalaryApproximation());
        assertEquals(5, updatedJob.getExcitement());
    }
     @Test
    void testGetUserIdByUsername() throws SQLException {
        // Arrange
        DatabaseHelper.registerUser("uniqueUser", "securePassword");

        // Act
        int userId = DatabaseHelper.getUserIdByUsername("uniqueUser");
        int unknownId = DatabaseHelper.getUserIdByUsername("unknownUser");

        // Assert
        assertTrue(userId > 0);
        assertEquals(-1, unknownId);
    }
    
}
