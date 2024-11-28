package controllerTest;

import controller.AuthenticationController;
import controller.MainController;
import model.DatabaseHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import view.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import org.mockito.MockedStatic;

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
}
