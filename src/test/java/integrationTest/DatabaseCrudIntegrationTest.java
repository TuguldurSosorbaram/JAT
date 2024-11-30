package integrationTest;

import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.DatabaseHelper;
import model.JobApplication;
import model.User;

import static org.junit.jupiter.api.Assertions.*;
import utils.SQLiteConnection;

public class DatabaseCrudIntegrationTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        SQLiteConnection.connect().close(); // Close the test connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        SQLiteConnection.clearTestConnection(); // Clear the test mode
    }

    @Test
    public void testCreateJobApplication() throws SQLException {
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

    @Test
    public void testUpdateJobApplication() throws SQLException {
        // Arrange
        // Create a job application and update its status
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
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(userId);
        
        
        DatabaseHelper.deleteJobApplication(job);
        jobs = DatabaseHelper.getJobApplicationsByUser(userId);
        // Assert
        assertEquals(0, jobs.size());
    }
}
