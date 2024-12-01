package integrationTest;

import controller.AuthenticationController;
import controller.MainController;
import view.LoginView;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.DatabaseHelper;
import model.User;

import static org.mockito.Mockito.*;
import utils.SQLiteConnection;

/**
 * Integration tests for the Login Workflow.
 * Validates the behavior of the login process for successful and failed login attempts.
 */
public class LoginWorkflowIntegrationTest {

    private Connection connection;
    private LoginView mockLoginView;
    private MainController mockMainController;
    private AuthenticationController authController;

    /**
     * Sets up the in-memory SQLite database and mocks dependencies before each test.
     *
     * @throws SQLException if database initialization fails.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        // Setup in-memory database
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SQLiteConnection.setConnectionForTesting(this.connection);
        DatabaseHelper.initializeDatabase();

        // Mock the LoginView
        mockLoginView = mock(LoginView.class);

        // Initialize MainController
        mockMainController = mock(MainController.class);
        authController = new AuthenticationController(mockLoginView, mockMainController);
    }

    /**
     * Tears down the in-memory SQLite database and clears test configurations after each test.
     *
     * @throws SQLException if database teardown fails.
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
     * Tests the successful login workflow.
     *
     * @throws SQLException if database interaction fails.
     */
    @Test
    public void testSuccessfulLogin() throws SQLException {
        // Arrange
        when(mockLoginView.getUsername()).thenReturn("testUser");
        when(mockLoginView.getPassword()).thenReturn("ValidPass1@");
        User user = new User("testUser", "ValidPass1@");
        DatabaseHelper.addUser(user);

        // Capture ActionListener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockLoginView).addLoginListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController).showMainView(1); // Verify main view is shown for user ID 1
        verify(mockMainController, never()).showError(anyString()); // No error should be shown
    }

    /**
     * Tests the failed login workflow.
     *
     * @throws SQLException if database interaction fails.
     */
    @Test
    public void testFailedLogin() throws SQLException {
        // Arrange
        when(mockLoginView.getUsername()).thenReturn("invalidUser");
        when(mockLoginView.getPassword()).thenReturn("InvalidPass@");
        User user = new User("testUser", "ValidPass1@");
        DatabaseHelper.addUser(user); // Add a valid user to the database

        // Capture ActionListener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockLoginView).addLoginListener(captor.capture());

        // Act
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Assert
        verify(mockMainController, never()).showMainView(anyInt()); // Ensure main view is not shown
        verify(mockMainController).showError("Login failed! Please check your credentials."); // Verify error message is shown
    }
}
