package unitTest.viewTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import view.LoginView;

/**
 * Unit tests for the LoginView class.
 */
public class LoginViewTest {
    private LoginView loginView;

    /**
     * Sets up a new instance of LoginView before each test.
     */
    @BeforeEach
    public void setUp() {
        loginView = new LoginView();
    }

    /**
     * Tests the getUsername method to ensure it correctly retrieves the text from the username field.
     */
    @Test
    public void testGetUsername() {
        // Access the private username field using TestUtils
        JTextField usernameField = (JTextField) TestUtils.getPrivateField(loginView, "usernameField");
        usernameField.setText("testUser");

        // Assert that getUsername returns the correct value
        assertEquals("testUser", loginView.getUsername());
    }

    /**
     * Tests the getPassword method to ensure it correctly retrieves the text from the password field.
     */
    @Test
    public void testGetPassword() {
        // Access the private password field using TestUtils
        JPasswordField passwordField = (JPasswordField) TestUtils.getPrivateField(loginView, "passwordField");
        passwordField.setText("testPassword");

        // Assert that getPassword returns the correct value
        assertEquals("testPassword", loginView.getPassword());
    }

    /**
     * Tests the visibility control methods (hideView and disposeView) to verify their behavior.
     */
    @Test
    public void testViewVisibilityControls() {
        // Verify that hideView hides the frame
        loginView.hideView();
        assertFalse(((JFrame) TestUtils.getPrivateField(loginView, "frame")).isVisible(), 
            "Frame should not be visible after calling hideView().");

        // Verify that disposeView disposes of the frame
        loginView.disposeView();
        assertFalse(((JFrame) TestUtils.getPrivateField(loginView, "frame")).isDisplayable(), 
            "Frame should no longer be displayable after calling disposeView().");
    }
}
