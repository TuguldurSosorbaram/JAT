package unitTest.controllerTest;

import controller.MainController;
import controller.MainViewController;
import view.MainView;
import model.DatabaseHelper;
import model.JobApplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

class MainViewControllerTest {

    private MainView mockMainView;
    private MainController mockMainController;
    private MainViewController mainViewController;

    @BeforeEach
    void setUp() {
        // Mock MainView and MainController
        mockMainView = mock(MainView.class);
        mockMainController = mock(MainController.class);

        // Instantiate MainViewController with mocked dependencies
        mainViewController = spy(new MainViewController(mockMainView, mockMainController));
    }

    @Test
    void testLoadJobApplicationsSuccessfully() {
        // Arrange: Mock job applications and static database call
        List<JobApplication> mockApplications = Arrays.asList(mock(JobApplication.class), mock(JobApplication.class));
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.getJobApplicationsByUser(anyInt()))
                                .thenReturn(mockApplications);

            // Act
            mainViewController.loadJobApplications();

            // Assert: Verify job applications are set in the view
            verify(mockMainView).setJobApplications(mockApplications);
        }
    }

    @Test
    void testLoadJobApplicationsWithError() {
        // Arrange: Simulate database error
        reset(mockMainView);
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.getJobApplicationsByUser(anyInt()))
                                .thenAnswer(invocation -> {
                                throw new RuntimeException("Database error");
                            });
            // Act
            mainViewController.loadJobApplications();

            // Assert: Verify error dialog is shown
            verify(mockMainView, never()).setJobApplications(anyList());
            verify(mockMainView).showMessage(null, "Error loading job applications.");
        }
    }
    @Test
    void testAddJobButtonAction() {
        // Arrange: Mock an ActionEvent and a spy on MainViewController
        ActionEvent mockEvent = mock(ActionEvent.class);

        // Attach the button listener (simulate MainView behavior)
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addAddButtonListener(captor.capture()); // Capture the listener

        // Act: Trigger the captured ActionListener
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify MainController.showAddJobApplication() is called
        verify(mockMainController).showAddJobApplication();
    }
    @Test
    void testEditJobButtonWithRowSelected() {
        // Arrange: Mock a selected job application
        JobApplication mockJobApplication = mock(JobApplication.class);
        when(mockMainView.getSelectedRow()).thenReturn(0); // Simulate a valid selection
        when(mockMainView.getSelectedJobApplication()).thenReturn(mockJobApplication);

        // Capture the ActionListener attached to the Edit button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addEditButtonListener(captor.capture());

        // Act: Simulate the Edit button being clicked
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify the correct behavior
        verify(mockMainController).showEditJobApplication(mockJobApplication); // Called with the correct job application
    }
    @Test
    void testEditJobButtonWithoutRowSelected() {
        // Arrange: Simulate no row selected
        when(mockMainView.getSelectedRow()).thenReturn(-1); // No selection

        // Capture the ActionListener attached to the Edit button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addEditButtonListener(captor.capture());

        // Act: Simulate the Edit button being clicked
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify error dialog is displayed
        verify(mockMainController, never()).showEditJobApplication(any()); // Ensure no call to showEditJobApplication
        verify(mockMainView).showMessage(null, "Please select a job application to edit."); // Verify error message
    }
    @Test
    void testLogOutButtonAction() {
        // Arrange: Mock an ActionEvent
        ActionEvent mockEvent = mock(ActionEvent.class);

        // Capture the ActionListener attached to the Log Out button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addLogOutButtonListener(captor.capture()); // Capture the listener

        // Act: Simulate the Log Out button being clicked
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify that MainController methods are called correctly
        verify(mockMainController).logOutUser(); // Ensure the user is logged out
        verify(mockMainController).showLoginView(); // Ensure navigation to login view
    }
    @Test
    void testTableEditListenerWithSuccessfulUpdate() throws Exception {
        // Arrange: Mock a JobApplication
        JobApplication mockJobApplication = mock(JobApplication.class);

        // Capture the ActionListener added to MainView
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addTableEditListener(captor.capture()); // Capture the listener

        // Act: Simulate a table edit event
        ActionEvent mockEvent = mock(ActionEvent.class);
        when(mockEvent.getSource()).thenReturn(mockJobApplication); // Mock the event source

        // Assert: Verify the database update
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            captor.getValue().actionPerformed(mockEvent);
            mockedDatabaseHelper.verify(() -> DatabaseHelper.updateJobApplication(mockJobApplication));
        }
    }
    
    @Test
    void testTableEditListenerWithDatabaseError() throws Exception {
        // Arrange: Mock a JobApplication
        JobApplication mockJobApplication = mock(JobApplication.class);

        // Capture the ActionListener added to MainView
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockMainView).addTableEditListener(captor.capture()); // Capture the listener

        // Act: Simulate a table edit event
        ActionEvent mockEvent = mock(ActionEvent.class);
        when(mockEvent.getSource()).thenReturn(mockJobApplication); // Mock the event source

        // Simulate a database error
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.updateJobApplication(any()))
                                .thenThrow(new RuntimeException("Database error"));

            // Trigger the listener
            captor.getValue().actionPerformed(mockEvent);

            // Assert: Verify error handling
            verify(mockMainView).showMessage(null, "Failed to update the database.");
        }
    }

}
