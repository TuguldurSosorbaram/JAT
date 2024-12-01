package integrationTest;

import controller.MainController;
import controller.RegistrationController;
import utils.SQLiteConnection;
import view.RegistrationView;
import model.DatabaseHelper;
import model.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the Registration Workflow.
 * Covers successful registration, handling of existing usernames, and invalid password scenarios.
 */
public class RegistrationWorkflowIntegrationTest {

    private Connection connection;
    private RegistrationView mockRegistrationView;
    private MainController mockMainController;
    private RegistrationController registrationController;

    /**
     * Sets up an in-memory SQLite database and mocks dependencies before each test.
     *
     * @throws SQLException if there is an error setting up the database.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();

        // Mock RegistrationView and MainController
        mockRegistrationView = mock(RegistrationView.class);
        mockMainController = mock(MainController.class);

        // Initialize RegistrationController
        registrationController = new RegistrationController(mockRegistrationView, mockMainController);
    }

    /**
     * Cleans up the database connection after each test.
     *
     * @throws SQLException if there is an error closing the database connection.
     */
    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        SQLiteConnection.clearTestConnection();
    }

    /**
     * Tests successful user registration.
     *
     * @throws SQLException if there is an error interacting with the database.
     */
    @Test
    public void testSuccessfulRegistration() throws SQLException {
        // Arrange
        when(mockRegistrationView.getUsername()).thenReturn("newUser");
        when(mockRegistrationView.getPassword()).thenReturn("ValidPass1@");

        // Capture ActionListener for the register button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        int userId = DatabaseHelper.getUserIdByUsername("newUser");
        assertTrue(userId > 0); // User ID should be valid

        // Verify navigation to login view
        verify(mockMainController).showLoginView();
        verify(mockMainController, never()).showError(anyString());
    }

    /**
     * Tests registration with an existing username.
     *
     * @throws SQLException if there is an error interacting with the database.
     */
    @Test
    public void testRegistrationWithExistingUsername() throws SQLException {
        // Arrange
        User existingUser = new User("existingUser", "ExistingPass1@");
        DatabaseHelper.addUser(existingUser);

        when(mockRegistrationView.getUsername()).thenReturn("existingUser");
        when(mockRegistrationView.getPassword()).thenReturn("NewPass1@");

        // Capture ActionListener for the register button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController, never()).showMainView(anyInt()); // Main view should not be shown
        verify(mockRegistrationView).setUsernameError("This username is already taken."); // Check username error
    }

    /**
     * Tests registration with an invalid password.
     *
     * @throws SQLException if there is an error interacting with the database.
     */
    @Test
    public void testRegistrationWithInvalidPassword() throws SQLException {
        // Arrange
        when(mockRegistrationView.getUsername()).thenReturn("newUser");
        when(mockRegistrationView.getPassword()).thenReturn("weak"); // Invalid password

        // Capture ActionListener for the register button
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController, never()).showMainView(anyInt()); // Main view should not be shown
        verify(mockRegistrationView).setPasswordError("Password must be at least 8 characters long."); // Check password error
    }
}
