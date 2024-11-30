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

public class LoginWorkflowIntegrationTest {

    private Connection connection;
    private LoginView mockLoginView;
    private MainController mockMainController;
    private AuthenticationController authController;

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
        authController = new AuthenticationController(mockLoginView,mockMainController);
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
    public void testSuccessfulLogin() throws SQLException {
        
        when(mockLoginView.getUsername()).thenReturn("testUser");
        when(mockLoginView.getPassword()).thenReturn("ValidPass1@");
        User user = new User("testUser", "ValidPass1@");
        DatabaseHelper.addUser(user);
        // Trigger login via listener
        ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
        verify(mockLoginView).addLoginListener(captor.capture());
        ActionEvent mockEvent = mock(ActionEvent.class);
        captor.getValue().actionPerformed(mockEvent);

        // Verify the main view is shown with the correct user ID
        verify(mockMainController).showMainView(1);
        verify(mockMainController, never()).showError(anyString());
    }

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
        verify(mockMainController).showError("Login failed! Please check your credentials."); // Verify error message
    }
}
