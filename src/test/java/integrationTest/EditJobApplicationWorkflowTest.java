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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class EditJobApplicationWorkflowTest {
    private Connection connection;
    private MainView mockMainView;
    private MainController mockMainController;
    private MainViewController mainController;
    private EditJobApplicationView editJobView;
    private EditJobApplicationController editController;

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

    @AfterEach
    void tearDown() throws SQLException {
        SQLiteConnection.connect().close(); // Close the test connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        SQLiteConnection.clearTestConnection(); // Clear the test mode
    }
    @Test
    public void testAddJobApplication() throws SQLException{
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
        
        editController = new EditJobApplicationController(mockMainController, editJobView,jobToEdit);
        
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(editJobView).addSaveButtonListener(captor.capture());
        
        JobApplication Updatedjob = new JobApplication();
        Updatedjob.setPosition("Updated Position");
        Updatedjob.setCompanyName("Updated Company");
        Updatedjob.setSalaryApproximation(1234);
        Updatedjob.setLocation("Updated Location");
        Updatedjob.setStatus("saved");
        Updatedjob.setExcitement(2);
        Updatedjob.setUserId(1);
        
        when(editJobView.getUpdatedJobApplication()).thenReturn(Updatedjob);

        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);
        jobs = DatabaseHelper.getJobApplicationsByUser(1);
        
        verify(mockMainController).disposeEditView();
        assertEquals(1, jobs.size());
        assertEquals("Updated Position", jobs.get(0).getPosition());
        assertEquals("Updated Company", jobs.get(0).getCompanyName());
    }
    
    @Test
    public void testCancelButton() throws SQLException{
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
        
        editController = new EditJobApplicationController(mockMainController, editJobView,jobToEdit);
        
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(editJobView).addCancelButtonListener(captor.capture());

        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);
        jobs = DatabaseHelper.getJobApplicationsByUser(1);
        
        verify(mockMainController).disposeEditView();
        assertEquals(1, jobs.size());
    }
    
}
