package unitTest.controllerTest;

import controller.EditJobApplicationController;
import controller.MainController;
import model.DatabaseHelper;
import model.JobApplication;
import view.EditJobApplicationView;

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


class EditJobApplicationControllerTest {

    private EditJobApplicationView mockView;
    private MainController mockMainController;
    private JobApplication mockJobApplication;
    private EditJobApplicationController controller;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockView = mock(EditJobApplicationView.class);
        mockMainController = mock(MainController.class);

        // Create a mock job application
        mockJobApplication = new JobApplication(
                "Software Engineer",
                "Tech Company",
                100000,
                "New York",
                "Pending",
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000),
                null,
                null,
                4,
                1
        );
        mockJobApplication.setId(1);

        // Create the controller with mocks
        controller = new EditJobApplicationController(mockMainController, mockView, mockJobApplication);
    }

    @Test
    void testSaveJobApplicationSuccessfully() throws Exception {
        // Arrange: Mock the updated job application
        JobApplication updatedJob = new JobApplication(
                "Updated Position",
                "Updated Company",
                120000,
                "Updated Location",
                "Accepted",
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000),
                null,
                null,
                5,
                1
        );
        updatedJob.setId(mockJobApplication.getId());
        updatedJob.setDateSaved(mockJobApplication.getDateSaved());
        updatedJob.setUserId(mockJobApplication.getUserId());

        when(mockView.getUpdatedJobApplication()).thenReturn(updatedJob);

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            
            // Act: Trigger save action
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Assert: Verify database update and UI interactions
            mockedDatabaseHelper.verify(() -> DatabaseHelper.updateJobApplication(updatedJob));
            verify(mockMainController).disposeEditView();
            verify(mockView, never()).showDialog("Error updating job application");
        }
    }
    @Test
    void testSaveJobApplicationDatabaseError() throws Exception {
        // Arrange: Mock the updated job application
        JobApplication updatedJob = new JobApplication(
                "Updated Position",
                "Updated Company",
                120000,
                "Updated Location",
                "Accepted",
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000),
                null,
                null,
                5,
                1
        );
        updatedJob.setId(mockJobApplication.getId());
        updatedJob.setDateSaved(mockJobApplication.getDateSaved());
        updatedJob.setUserId(mockJobApplication.getUserId());

        when(mockView.getUpdatedJobApplication()).thenReturn(updatedJob);

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            // Simulate a database error
            mockedDatabaseHelper.when(() -> DatabaseHelper.updateJobApplication(updatedJob))
                                .thenThrow(new RuntimeException("Database error"));

            // Act: Trigger save action
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addSaveButtonListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Assert: Verify database interaction and error handling
            mockedDatabaseHelper.verify(() -> DatabaseHelper.updateJobApplication(updatedJob));
            verify(mockMainController, never()).disposeEditView(); // Ensure view is not closed
            verify(mockView).showDialog("Error updating job application."); // Verify error dialog is shown
        }
    }
    @Test
    void testCancelActionDisposesView() {
        // Act: Trigger cancel action
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockView).addCancelButtonListener(captor.capture());
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify the view is disposed
        verify(mockMainController).disposeEditView();
    }

}
