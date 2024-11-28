package ControllerTest;

import controller.AddJobApplicationController;
import controller.MainController;
import model.DatabaseHelper;
import model.JobApplication;
import view.AddJobApplicationView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockedStatic;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class AddJobApplicationControllerTest {

    private AddJobApplicationView mockView;
    private MainController mockMainController;
    private AddJobApplicationController controller;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockView = mock(AddJobApplicationView.class);
        mockMainController = mock(MainController.class);

        // Create controller with mocked dependencies
        controller = new AddJobApplicationController(mockView, mockMainController);
        
    }

    @Test
    void testSaveJobApplicationSuccessfully() throws SQLException {
        // Arrange: Mock valid user input
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
            // Capture the listener and trigger the save action
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Verify database interaction and view disposal
            mockedDatabaseHelper.verify(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)));
            verify(mockMainController).disposeAddView();
        }
    }
    
    @Test
    void testSaveJobApplicationDatabaseError() throws SQLException {
        // Arrange: Mock valid user input
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

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)))
                                .thenThrow(new SQLException("Database error"));

            // Capture the listener and trigger the save action
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Verify error handling
            verify(mockMainController, never()).disposeAddView();
            verify(mockView, never()).disposeView(); // Ensure view stays open
            mockedDatabaseHelper.verify(() -> DatabaseHelper.addJobApplication(any(JobApplication.class)));
            verify(mockView).showErrorDialog("Error saving job application.");
        }
    }
    @Test
    void testCancelActionDisposesView() {
        // Capture the listener and trigger the cancel action
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockView).addCancelButtonListener(captor.capture());
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Verify the view is disposed
        verify(mockMainController).disposeAddView();
    }

}
