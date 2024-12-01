package integrationTest;

import controller.AddJobApplicationController;
import controller.MainController;
import controller.MainViewController;
import model.DatabaseHelper;
import model.JobApplication;
import utils.SQLiteConnection;
import view.AddJobApplicationView;
import view.MainView;

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
 * Integration tests for the Add Job Application workflow.
 * Tests various scenarios including single-user and multi-user job application additions and cancellation of the process.
 */
public class AddJobApplicationWorkflowTest {
    private Connection connection;
    private MainView mockMainView;
    private MainController mockMainController;
    private MainViewController mainController;
    private AddJobApplicationView addJobView;
    private AddJobApplicationController addController;

    /**
     * Sets up an in-memory SQLite database and mocks required dependencies before each test.
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
        addJobView = mock(AddJobApplicationView.class);

        // Initialize MainController
        mockMainController = mock(MainController.class);
        mainController = new MainViewController(mockMainView, mockMainController);
        addController = new AddJobApplicationController(addJobView, mockMainController);
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
     * Tests adding a job application for a single user.
     */
    @Test
    public void testAddJobApplication() {
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addAddButtonListener(captor.capture());

        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        ArgumentCaptor<ActionListener> captor1 = ArgumentCaptor.forClass(ActionListener.class);
        verify(addJobView).addSaveButtonListener(captor1.capture());

        when(addJobView.getPosition()).thenReturn("Software Engineer");
        when(addJobView.getCompanyName()).thenReturn("Tech Company");
        when(addJobView.getSalaryApproximation()).thenReturn(100000);
        when(addJobView.getLocation()).thenReturn("New York");
        when(addJobView.getStatus()).thenReturn("Pending");
        when(addJobView.getDeadline()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getDateApplied()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getFollowUpDate()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getExcitement()).thenReturn(4);
        when(mockMainController.getLoggedUserId()).thenReturn(1);

        captor1.getValue().actionPerformed(mockEvent);

        verify(mockMainController).disposeAddView();
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(1);

        // Assert
        assertEquals(1, jobs.size());
    }

    /**
     * Tests adding job applications for multiple users.
     */
    @Test
    public void testAddJobApplicationMultipleUser() {
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addAddButtonListener(captor.capture());

        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        ArgumentCaptor<ActionListener> captor1 = ArgumentCaptor.forClass(ActionListener.class);
        verify(addJobView).addSaveButtonListener(captor1.capture());

        when(addJobView.getPosition()).thenReturn("Software Engineer");
        when(addJobView.getCompanyName()).thenReturn("Tech Company");
        when(addJobView.getSalaryApproximation()).thenReturn(100000);
        when(addJobView.getLocation()).thenReturn("New York");
        when(addJobView.getStatus()).thenReturn("Pending");
        when(addJobView.getDeadline()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getDateApplied()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getFollowUpDate()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getExcitement()).thenReturn(4);
        when(mockMainController.getLoggedUserId()).thenReturn(1);

        captor1.getValue().actionPerformed(mockEvent);

        verify(mockMainController).disposeAddView();
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(1);

        // Assert
        assertEquals(1, jobs.size());

        // Test for a second user
        captor.getValue().actionPerformed(mockEvent);
        reset(addJobView);
        when(addJobView.getPosition()).thenReturn("Software Engineer");
        when(addJobView.getCompanyName()).thenReturn("Tech Company");
        when(addJobView.getSalaryApproximation()).thenReturn(100000);
        when(addJobView.getLocation()).thenReturn("New York");
        when(addJobView.getStatus()).thenReturn("Pending");
        when(addJobView.getDeadline()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getDateApplied()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getFollowUpDate()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(addJobView.getExcitement()).thenReturn(4);
        when(mockMainController.getLoggedUserId()).thenReturn(2);
        captor1.getValue().actionPerformed(mockEvent);

        jobs = DatabaseHelper.getJobApplicationsByUser(2);

        // Assert
        assertEquals(1, jobs.size());
    }

    /**
     * Tests canceling the addition of a job application.
     */
    @Test
    public void testCancelAddJobApplication() {
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(addJobView).addCancelButtonListener(captor.capture());

        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController).disposeAddView();
        List<JobApplication> jobs = DatabaseHelper.getJobApplicationsByUser(1);
        assertEquals(0, jobs.size()); // No job should be added
    }
}