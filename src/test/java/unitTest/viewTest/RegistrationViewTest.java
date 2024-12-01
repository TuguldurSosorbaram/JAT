package unitTest.viewTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import static org.junit.jupiter.api.Assertions.*;
import view.RegistrationView;
import unitTest.viewTest.TestUtils;

/**
 * Unit tests for the RegistrationView class.
 */
public class RegistrationViewTest {
    private RegistrationView registrationView;

    /**
     * Initializes the RegistrationView instance before each test.
     */
    @BeforeEach
    public void setUp() {
        registrationView = new RegistrationView();
    }

    /**
     * Tests retrieving the username input field value.
     */
    @Test
    public void testGetUsername() {
        JTextField usernameField = (JTextField) TestUtils.getPrivateField(registrationView, "usernameField");
        usernameField.setText("newUser");
        assertEquals("newUser", registrationView.getUsername(), "Username should match the input value");
    }

    /**
     * Tests retrieving the password input field value.
     */
    @Test
    public void testGetPassword() {
        JPasswordField passwordField = (JPasswordField) TestUtils.getPrivateField(registrationView, "passwordField");
        passwordField.setText("newPassword");
        assertEquals("newPassword", registrationView.getPassword(), "Password should match the input value");
    }

    /**
     * Tests setting the username error message.
     */
    @Test
    public void testSetUsernameError() {
        registrationView.setUsernameError("Invalid username");
        JLabel usernameErrorLabel = (JLabel) TestUtils.getPrivateField(registrationView, "usernameErrorLabel");
        assertEquals("<html>Invalid username</html>", usernameErrorLabel.getText(), "Username error message should be set correctly");
    }

    /**
     * Tests setting the password error message.
     */
    @Test
    public void testSetPasswordError() {
        registrationView.setPasswordError("Weak password");
        JLabel passwordErrorLabel = (JLabel) TestUtils.getPrivateField(registrationView, "passwordErrorLabel");
        assertEquals("<html>Weak password</html>", passwordErrorLabel.getText(), "Password error message should be set correctly");
    }

    /**
     * Tests clearing all error messages.
     */
    @Test
    public void testClearErrors() {
        registrationView.setUsernameError("Invalid username");
        registrationView.setPasswordError("Weak password");
        registrationView.clearErrors();

        JLabel usernameErrorLabel = (JLabel) TestUtils.getPrivateField(registrationView, "usernameErrorLabel");
        JLabel passwordErrorLabel = (JLabel) TestUtils.getPrivateField(registrationView, "passwordErrorLabel");

        assertEquals("", usernameErrorLabel.getText(), "Username error should be cleared");
        assertEquals("", passwordErrorLabel.getText(), "Password error should be cleared");
    }

    /**
     * Tests adding an ActionListener to the Register button.
     */
    @Test
    public void testAddRegisterListener() {
        ActionListener mockListener = (ActionEvent e) -> {};

        registrationView.addRegisterListener(mockListener);
        JButton registerButton = (JButton) TestUtils.getPrivateField(registrationView, "registerButton");
        ActionListener[] listeners = registerButton.getActionListeners();

        assertTrue(listeners.length > 0, "Register button should have at least one ActionListener");
        assertEquals(mockListener, listeners[0], "The added ActionListener should match the mock listener");
    }

    /**
     * Tests adding a MouseListener to the BackToLogin label.
     */
    @Test
    public void testAddBackToLoginListener() {
        MouseAdapter mockListener = new MouseAdapter() {};

        registrationView.addBackToLoginListener(mockListener);
        JLabel backToLoginLabel = (JLabel) TestUtils.getPrivateField(registrationView, "backToLoginLabel");
        MouseListener[] listeners = backToLoginLabel.getMouseListeners();

        assertTrue(listeners.length > 0, "BackToLogin label should have at least one MouseListener");
        assertEquals(mockListener, listeners[0], "The added MouseListener should match the mock listener");
    }

    /**
     * Tests the visibility control methods for the RegistrationView.
     */
    @Test
    public void testViewVisibilityControls() {
        JFrame frame = (JFrame) TestUtils.getPrivateField(registrationView, "frame");

        registrationView.hideView();
        assertFalse(frame.isVisible(), "Frame should not be visible after calling hideView()");

        registrationView.disposeView();
        assertFalse(frame.isDisplayable(), "Frame should no longer be displayable after calling disposeView()");
    }
}
