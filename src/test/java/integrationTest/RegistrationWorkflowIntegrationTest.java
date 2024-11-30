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
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RegistrationWorkflowIntegrationTest {

    private Connection connection;
    private RegistrationView mockRegistrationView;
    private MainController mockMainController;
    private RegistrationController registrationController;

    @BeforeEach
    public void setUp() throws SQLException {
        // Setup in-memory database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();

        // Mock RegistrationView
        mockRegistrationView = mock(RegistrationView.class);

        // Mock MainController
        mockMainController = mock(MainController.class);

        // Initialize RegistrationController
        registrationController = new RegistrationController(mockRegistrationView, mockMainController);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        SQLiteConnection.clearTestConnection();
    }
    @Test
    public void testSuccessfulRegistration() throws SQLException {
        // Arrange
        when(mockRegistrationView.getUsername()).thenReturn("newUser");
        when(mockRegistrationView.getPassword()).thenReturn("ValidPass1@");

        // Capture ActionListener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Verify the user is added to the database
        int userId = DatabaseHelper.getUserIdByUsername("newUser");
        assertTrue(userId > 0); // Verify user ID exists

        // Verify main view is shown with correct user ID
        verify(mockMainController).showLoginView();
        verify(mockMainController, never()).showError(anyString());
    }
    @Test
    public void testRegistrationWithExistingUsername() throws SQLException {
        // Arrange
        User existingUser = new User("existingUser", "ExistingPass1@");
        DatabaseHelper.addUser(existingUser);

        when(mockRegistrationView.getUsername()).thenReturn("existingUser");
        when(mockRegistrationView.getPassword()).thenReturn("NewPass1@");

        // Capture ActionListener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController, never()).showMainView(anyInt()); // Main view should not be shown
        verify(mockRegistrationView).setUsernameError("This username is already taken."); // Check error message
    }
    @Test
    public void testRegistrationWithInvalidPassword() throws SQLException {
        // Arrange
        when(mockRegistrationView.getUsername()).thenReturn("newUser");
        when(mockRegistrationView.getPassword()).thenReturn("weak"); // Invalid password

        // Capture ActionListener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockRegistrationView).addRegisterListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController, never()).showMainView(anyInt()); // Main view should not be shown
        verify(mockRegistrationView).setPasswordError("Password must be at least 8 characters long."); // Check error message
    }
}

