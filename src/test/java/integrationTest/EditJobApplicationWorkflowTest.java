package integrationTest;

import controller.EditJobApplicationController;
import controller.MainController;
import controller.MainViewController;
import model.DatabaseHelper;
import model.JobApplication;
import utils.SQLiteConnection;
import view.MainView;
import view.EditJobApplicationView;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the Edit Job Application workflow.
 * Covers scenarios such as saving updates and canceling edits.
 */
public class EditJobApplicationWorkflowTest {
    private Connection connection;
    private MainView mockMainView;
    private MainController mockMainController;
    private MainViewController mainController;
    private EditJobApplicationView editJobView;
    private EditJobApplicationController editController;

    /**
     * Sets up an in-memory SQLite database and mocks dependencies before each test.
     *
     * @throws SQLException if there is an error setting up the database.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        // Setup in-memory database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();

        mockMainView = mock(MainView.class);
        editJobView = mock(EditJobApplicationView.class);

        // Initialize MainController
        mockMainController = mock(MainController.class);
        mainController = new MainViewController(mockMainView,mockMainController);
        
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
     * Tests saving changes to a job application.
     *
     * @throws SQLException if there is an error interacting with the database.
     */
    @Test
    public void testAddJobApplication() throws SQLException {
        // Arrange
        JobApplication job = new JobApplication();
        job.setPosition("Position");
        job.setCompanyName("Company");
        job.setSalaryApproximation(1234);
        job.setLocation("Location");
        job.setStatus("saved");
        job.setExcitement(2);
        job.setUserId(1);

        DatabaseHelper.addJobApplication(job);

        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(1);
        JobApplication jobToEdit = jobs.get(0);

        editController = new EditJobApplicationController(mockMainController, editJobView, jobToEdit);

        // Capture the save button's action listener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(editJobView).addSaveButtonListener(captor.capture());

        // Updated job details
        JobApplication updatedJob = new JobApplication();
        updatedJob.setPosition("Updated Position");
        updatedJob.setCompanyName("Updated Company");
        updatedJob.setSalaryApproximation(1234);
        updatedJob.setLocation("Updated Location");
        updatedJob.setStatus("saved");
        updatedJob.setExcitement(2);
        updatedJob.setUserId(1);

        when(editJobView.getUpdatedJobApplication()).thenReturn(updatedJob);

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);
        jobs = DatabaseHelper.getJobApplicationsByUser(1);

        // Assert
        verify(mockMainController).disposeEditView();
        assertEquals(1, jobs.size());
        assertEquals("Updated Position", jobs.get(0).getPosition());
        assertEquals("Updated Company", jobs.get(0).getCompanyName());
    }

    /**
     * Tests canceling an edit operation.
     *
     * @throws SQLException if there is an error interacting with the database.
     */
    @Test
    public void testCancelButton() throws SQLException {
        // Arrange
        JobApplication job = new JobApplication();
        job.setPosition("Position");
        job.setCompanyName("Company");
        job.setSalaryApproximation(1234);
        job.setLocation("Location");
        job.setStatus("saved");
        job.setExcitement(2);
        job.setUserId(1);

        DatabaseHelper.addJobApplication(job);

        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(1);
        JobApplication jobToEdit = jobs.get(0);

        editController = new EditJobApplicationController(mockMainController, editJobView, jobToEdit);

        // Capture the cancel button's action listener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(editJobView).addCancelButtonListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);
        jobs = DatabaseHelper.getJobApplicationsByUser(1);

        // Assert
        verify(mockMainController).disposeEditView();
        assertEquals(1, jobs.size());
    }
}
