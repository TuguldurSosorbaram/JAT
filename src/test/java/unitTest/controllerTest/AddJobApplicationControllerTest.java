package unitTest.controllerTest;

import controller.AddJobApplicationController;
import controller.MainController;
import model.DatabaseHelper;
import model.JobApplication;
import view.AddJobApplicationView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the AddJobApplicationController class.
 */
class AddJobApplicationControllerTest {

    private AddJobApplicationView mockView;
    private MainController mockMainController;
    private AddJobApplicationController controller;

    /**
     * Sets up mocks and dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        mockView = mock(AddJobApplicationView.class);
        mockMainController = mock(MainController.class);
        controller = new AddJobApplicationController(mockView, mockMainController);
    }

    /**
     * Tests successful saving of a job application.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveJobApplicationSuccessfully() throws SQLException {
        when(mockView.getPosition()).thenReturn("Software Engineer");
        when(mockView.getCompanyName()).thenReturn("Tech Company");
        when(mockView.getSalaryApproximation()).thenReturn(100000);
        when(mockView.getLocation()).thenReturn("New York");
        when(mockView.getStatus()).thenReturn("Pending");
        when(mockView.getDeadline()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockView.getDateApplied()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockView.getFollowUpDate()).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(mockView.getExcitement()).thenReturn(4);
        when(mockMainController.getLoggedUserId()).thenReturn(1);

        try (org.mockito.MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            mockedDatabaseHelper.verify(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)));
            verify(mockMainController).disposeAddView();
        }
    }

    /**
     * Tests handling of a database error when saving a job application.
     *
     * @throws SQLException if a database error occurs.
     */
    @Test
    void testSaveJobApplicationDatabaseError() throws SQLException {
        when(mockView.getPosition()).thenReturn("Software Engineer");
        when(mockView.getCompanyName()).thenReturn("Tech Company");
        when(mockView.getSalaryApproximation()).thenReturn(100000);
        when(mockView.getLocation()).thenReturn("New York");
        when(mockView.getStatus()).thenReturn("Pending");
        when(mockView.getDeadline()).thenReturn(new java.util.Date());
        when(mockView.getDateApplied()).thenReturn(new java.util.Date());
        when(mockView.getFollowUpDate()).thenReturn(new java.util.Date());
        when(mockView.getExcitement()).thenReturn(4);
        when(mockMainController.getLoggedUserId()).thenReturn(1);

        try (org.mockito.MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)))
                                .thenThrow(new SQLException("Database error"));

            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            verify(mockMainController, never()).disposeAddView();
            mockedDatabaseHelper.verify(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)));
            verify(mockView).showErrorDialog("Error saving job application.");
        }
    }

    /**
     * Tests that the cancel action correctly disposes of the view.
     */
    @Test
    void testCancelActionDisposesView() {
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockView).addCancelButtonListener(captor.capture());
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        verify(mockMainController).disposeAddView();
    }
}
