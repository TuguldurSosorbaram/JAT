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

class AuthenticationControllerTest {

    private LoginView mockView;
    private MainController mockMainController;
    private AuthenticationController controller;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockView = mock(LoginView.class);
        mockMainController = mock(MainController.class);

        // Create controller with mocked dependencies
        controller = new AuthenticationController(mockView, mockMainController);
    }

    @Test
    void testSuccessfulLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenReturn(true);
            mockedDatabaseHelper.when(() -> DatabaseHelper.getUserIdByUsername("ValidUser"))
                                .thenReturn(1);

            // Trigger login via listener
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Verify the main view is shown with the correct user ID
            mockedDatabaseHelper.verify(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"));
            mockedDatabaseHelper.verify(() -> DatabaseHelper.getUserIdByUsername("ValidUser"));
            verify(mockMainController).showMainView(1);
            verify(mockMainController, never()).showError(anyString());
        }
    }
    
    @Test
    void testInvalidCredentials() {
        when(mockView.getUsername()).thenReturn("InvalidUser");
        when(mockView.getPassword()).thenReturn("InvalidPass");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("InvalidUser", "InvalidPass"))
                                .thenReturn(false);

            // Trigger login via listener
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Verify error message is displayed
            mockedDatabaseHelper.verify(() -> DatabaseHelper.validateUser("InvalidUser", "InvalidPass"));
            verify(mockMainController).showError("Login failed! Please check your credentials.");
            verify(mockMainController, never()).showMainView(anyInt());
        }
    }
    
    @Test
    void testExceptionDuringLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenThrow(new RuntimeException("Database error"));

            // Trigger login via listener
            ArgumentCaptor<ActionListener> captor = ArgumentCaptor.forClass(ActionListener.class);
            verify(mockView).addLoginListener(captor.capture());
            ActionEvent mockEvent = mock(ActionEvent.class);
            captor.getValue().actionPerformed(mockEvent);

            // Verify generic error message is displayed
            verify(mockMainController).showError("An error occurred during login.");
            verify(mockMainController, never()).showMainView(anyInt());
        }
    }
    
    @Test
    void testNavigateToRegistrationView() {
        // Trigger registration view navigation via listener
        ArgumentCaptor<MouseAdapter> captor = ArgumentCaptor.forClass(MouseAdapter.class);
        verify(mockView).addRegisterListener(captor.capture());
        MouseEvent mockEvent = mock(MouseEvent.class);
        captor.getValue().mouseClicked(mockEvent);

        // Verify the registration view is shown
        verify(mockMainController).showRegistrationView();
    }
    
    @Test
    void testEnterKeyPressTriggersLogin() {
        when(mockView.getUsername()).thenReturn("ValidUser");
        when(mockView.getPassword()).thenReturn("ValidPass1@");

        try (MockedStatic<DatabaseHelper> mockedDatabaseHelper = Mockito.mockStatic(DatabaseHelper.class)) {
            mockedDatabaseHelper.when(() -> DatabaseHelper.validateUser("ValidUser", "ValidPass1@"))
                                .thenReturn(true);
            mockedDatabaseHelper.when(() -> DatabaseHelper.getUserIdByUsername("ValidUser"))
                                .thenReturn(1);

            // Trigger login via ENTER key listener
            ArgumentCaptor<KeyAdapter> captor = ArgumentCaptor.forClass(KeyAdapter.class);
            verify(mockView).addKeyListenerForEnter(captor.capture());
            KeyEvent mockEvent = mock(KeyEvent.class);
            when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
            captor.getValue().keyPressed(mockEvent);

            // Verify the main view is shown
            verify(mockMainController).showMainView(1);
        }
    }
}
