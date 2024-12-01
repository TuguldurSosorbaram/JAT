package unitTest.controllerTest;

import controller.AuthenticationController;
import controller.MainController;
import model.DatabaseHelper;
import view.LoginView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the AuthenticationController class.
 */
class AuthenticationControllerTest {

    private LoginView mockView;
    private MainController mockMainController;
    private AuthenticationController controller;

    /**
     * Sets up mocks and dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        mockView = mock(LoginView.class);
        mockMainController = mock(MainController.class);
        controller = new AuthenticationController(mockView, mockMainController);
    }

    /**
     * Tests successful login with valid credentials.
     */
    @Test
    void testSuccessfulLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenReturn(true);
            mockedDatabaseHelper.when(() -> DatabaseHelper.getUserIdByUsername("ValidUser"))
                                .thenReturn(1);

            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            mockedDatabaseHelper.verify(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"));
            mockedDatabaseHelper.verify(() -> DatabaseHelper.getUserIdByUsername("ValidUser"));
            verify(mockMainController).showMainView(1);
            verify(mockMainController, never()).showError(anyString());
        }
    }

    /**
     * Tests login failure with invalid credentials.
     */
    @Test
    void testInvalidCredentials() {
        when(mockView.getUsername()).thenReturn("InvalidUser");
        when(mockView.getPassword()).thenReturn("InvalidPass");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("InvalidUser", "InvalidPass"))
                                .thenReturn(false);

            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            mockedDatabaseHelper.verify(() -> DatabaseHelper.validateUser("InvalidUser", "InvalidPass"));
            verify(mockMainController).showError("Login failed! Please check your credentials.");
            verify(mockMainController, never()).showMainView(anyInt());
        }
    }

    /**
     * Tests handling of exceptions during login.
     */
    @Test
    void testExceptionDuringLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenThrow(new RuntimeException("Database error"));

            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            verify(mockMainController).showError("An error occurred during login.");
            verify(mockMainController, never()).showMainView(anyInt());
        }
    }

    /**
     * Tests navigation to the registration view when the registration link is clicked.
     */
    @Test
    void testNavigateToRegistrationView() {
        ArgumentCaptor<MouseAdapter> captor = ArgumentCaptor.forClass(MouseAdapter.class);
        verify(mockView).addRegisterListener(captor.capture());
        MouseEvent mockEvent = mock(MouseEvent.class);
        captor.getValue().mouseClicked(mockEvent);

        verify(mockMainController).showRegistrationView();
    }

    /**
     * Tests login triggered by pressing the ENTER key.
     */
    @Test
    void testEnterKeyPressTriggersLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenReturn(true);
            mockedDatabaseHelper.when(() -> DatabaseHelper.getUserIdByUsername("ValidUser"))
                                .thenReturn(1);

            ArgumentCaptor<KeyAdapter> captor = ArgumentCaptor.forClass(KeyAdapter.class);
            verify(mockView).addKeyListenerForEnter(captor.capture());
            KeyEvent mockEvent = mock(KeyEvent.class);
            when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
            captor.getValue().keyPressed(mockEvent);

            verify(mockMainController).showMainView(1);
        }
    }
}
