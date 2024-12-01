package integrationTest;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import model.DatabaseHelper;
import model.JobApplication;
import model.User;
import utils.SQLiteConnection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CRUD operations on the database.
 * Ensures correct behavior for creating, reading, updating, and deleting job applications.
 */
public class DatabaseCrudIntegrationTest {
    private Connection connection;

    /**
     * Sets up an in-memory SQLite database and initializes the schema before each test.
     *
     * @throws SQLException if there is an error setting up the database.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();
    }

    /**
     * Cleans up the database connection after each test.
     *
     * @throws SQLException if there is an error closing the database connection.
     */
    @AfterEach
    void tearDown() throws SQLException {
        SQLiteConnection.connect().close(); // Close the test connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        SQLiteConnection.clearTestConnection(); // Clear the test mode
    }

    /**
     * Tests the creation of a job application in the database.
     */
    @Test
    public void testCreateJobApplication() throws SQLException {
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

    /**
     * Tests reading multiple job applications for a user from the database.
     */
    @Test
    public void testReadJobApplications() throws SQLException {
        // Arrange
        User user = new User("jobUser", "password123");
        DatabaseHelper.addUser(user);
        int userId = DatabaseHelper.getUserIdByUsername("jobUser");

        JobApplication job1 = new JobApplication("Developer", "TechCorp", 80000, "Remote", "Applied", null, null, null, null, 4, userId);
        JobApplication job2 = new JobApplication("Tester", "CodeInc", 70000, "On-site", "Interview", null, null, null, null, 5, userId);

        DatabaseHelper.addJobApplication(job1);
        DatabaseHelper.addJobApplication(job2);

        // Act
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(userId);

        // Assert
        assertEquals(2, jobs.size());
    }

    /**
     * Tests updating a job application's status in the database.
     */
    @Test
    public void testUpdateJobApplication() throws SQLException {
        // Arrange
        User user = new User("jobUser", "password123");
        DatabaseHelper.addUser(user);
        int userId = DatabaseHelper.getUserIdByUsername("jobUser");

        JobApplication job = new JobApplication("Developer", "TechCorp", 80000, "Remote", "Applied", null, null, null, null, 4, userId);
        DatabaseHelper.addJobApplication(job);

        JobApplication savedJob = DatabaseHelper.getJobApplicationsByUser(userId).get(0);
        savedJob.setStatus("Interview");

        // Act
        DatabaseHelper.updateJobApplication(savedJob);
        JobApplication updatedJob = DatabaseHelper.getJobApplicationsByUser(userId).get(0);

        // Assert
        assertEquals("Interview", updatedJob.getStatus());
    }

    /**
     * Tests deleting a job application from the database.
     */
    @Test
    public void testDeleteJobApplication() throws SQLException {
        // Arrange
        User user = new User("jobUser", "password123");
        DatabaseHelper.addUser(user);
        int userId = DatabaseHelper.getUserIdByUsername("jobUser");

        JobApplication job = new JobApplication("Developer", "TechCorp", 80000, "Remote", "Applied", null, null, null, null, 4, userId);
        job.setId(1);
        DatabaseHelper.addJobApplication(job);

        // Act
        DatabaseHelper.deleteJobApplication(job);
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(userId);

        // Assert
        assertEquals(0, jobs.size());
    }
}