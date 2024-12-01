package unitTest.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import view.RegistrationView;
import model.DatabaseHelper;
import controller.MainController;
import controller.RegistrationController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

/**
 * Unit tests for RegistrationController, covering all edge cases for user registration.
 */
class RegistrationControllerTest {

    private RegistrationView mockView;
    private MainController mockMainController;
    private RegistrationController controller;

    /**
     * Sets up the test environment by mocking the view and controller and initializing the RegistrationController.
     */
    @BeforeEach
    void setUp() {
        mockView = mock(RegistrationView.class);
        mockMainController = mock(MainController.class);
        controller = new RegistrationController(mockView, mockMainController);
        controller.enableTestMode(); // Enable test mode for controlled validation
    }

    /**
     * Tests the registration process with valid inputs and verifies the expected behavior.
     */
    @Test
    void testHandleRegistration() {
        // Arrange: Mock valid user input
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        // Mock the static method
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.registerUser("ValidUser", "ValidPass1@"))
                                .thenReturn(true);
            
            ActionEvent mockEvent = mock(ActionEvent.class);
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addRegisterListener(captor.capture());
            captor.getValue().actionPerformed(mockEvent);

            // Assert: Verify interactions
            verify(mockView).clearErrors();
            verify(mockMainController).showLoginView();
            verify(mockView, never()).setUsernameError(anyString());
            verify(mockView, never()).setPasswordError(anyString());

            // Verify the static method was called with the correct arguments
            mockedDatabaseHelper.verify(() -> DatabaseHelper.registerUser("ValidUser", "ValidPass1@"));
        }
    }

    /**
     * Tests the validation of invalid usernames, including various error conditions.
     */
    @Test
    void testInvalidUsername() {
        // Arrange: Set invalid username (starts with a digit)
        when(mockView.getUsername()).thenReturn("123Invalid");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        // Capture the listener for the "Register" button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockView).addRegisterListener(captor.capture());

        // Act: Simulate button click
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify username error is displayed
        verify(mockView).setUsernameError("Username must start with a letter.");
        verify(mockView, never()).setPasswordError(anyString()); // No password errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
        
        reset(mockView);
        when(mockView.getUsername()).thenReturn("Inv");
        when(mockView.getPassword()).thenReturn("ValidPass1@");
        captor.getValue().actionPerformed(mockEvent);
        verify(mockView).setUsernameError("Username must be longer than 4 characters.");
        verify(mockView, never()).setPasswordError(anyString()); // No password errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
        
        reset(mockView);
        when(mockView.getUsername()).thenReturn("Inv@lid");
        when(mockView.getPassword()).thenReturn("ValidPass1@");
        captor.getValue().actionPerformed(mockEvent);
        verify(mockView).setUsernameError("Username can only contain letters and numbers.");
        verify(mockView, never()).setPasswordError(anyString()); // No password errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
    }

    /**
     * Tests the validation of invalid passwords, including different error conditions.
     */
    @Test
    void testInvalidPassword() {
        // Arrange: Set invalid password (missing special character)
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("Password1");

        // Capture the listener for the "Register" button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockView).addRegisterListener(captor.capture());

        // Act: Simulate button click
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert: Verify password error is displayed
        verify(mockView).setPasswordError("Password must include at least one special character.");
        verify(mockView, never()).setUsernameError(anyString()); // No username errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
        
        reset(mockView);
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("Pw");
        captor.getValue().actionPerformed(mockEvent);
        verify(mockView).setPasswordError("Password must be at least 8 characters long.");
        verify(mockView, never()).setUsernameError(anyString()); // No username errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
        
        reset(mockView);
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("Password");
        captor.getValue().actionPerformed(mockEvent);
        verify(mockView).setPasswordError("Password must include at least one number.");
        verify(mockView, never()).setUsernameError(anyString()); // No username errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
        
        reset(mockView);
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("password1@");
        captor.getValue().actionPerformed(mockEvent);
        verify(mockView).setPasswordError("Password must include at least one uppercase letter.");
        verify(mockView, never()).setUsernameError(anyString()); // No username errors
        verify(mockMainController, never()).showLoginView(); // Should not navigate
    }

    /**
     * Tests behavior when attempting to register a username that already exists in the database.
     */
    @Test
    void testUsernameAlreadyExists() {
        when(mockView.getUsername()).thenReturn("ExistingUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            // Simulate username already exists
            mockedDatabaseHelper.when(() -> DatabaseHelper.registerUser("ExistingUser", "ValidPass1@"))
                                .thenReturn(false);

            // Capture the listener and simulate button click
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addRegisterListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Assert behavior
            verify(mockView).setUsernameError("This username is already taken.");
            mockedDatabaseHelper.verify(() -> DatabaseHelper.registerUser("ExistingUser", "ValidPass1@"));
            verify(mockMainController, never()).showLoginView(); // Should not proceed
        }
    }

    /**
     * Tests the behavior when a registration attempt fails due to a database error.
     */
    @Test
    void testRegistrationFailure() {
        // Arrange: Simulate database error
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        // Mock the static method
        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            // Simulate an exception being thrown by the static method
            mockedDatabaseHelper.when(() -> DatabaseHelper.registerUser("ValidUser", "ValidPass1@"))
                                .thenThrow(new RuntimeException("Database error"));

            // Capture the listener for the "Register" button
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addRegisterListener(captor.capture());

            // Act: Simulate button click
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Assert: Verify error handling
            //verify(mockView).setUsernameError("Registration failed! Please try again.");
            verify(mockMainController, never()).showLoginView(); // Should not navigate

            // Ensure the static method was called with the correct arguments
            mockedDatabaseHelper.verify(() -> DatabaseHelper.registerUser("ValidUser", "ValidPass1@"));
        }
    }

}
