package unitTest.viewTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import view.LoginView;

public class LoginViewTest {
    private LoginView loginView;

    @BeforeEach
    public void setUp() {
        loginView = new LoginView();
    }

    @Test
    public void testGetUsername() {
        JTextField usernameField = (JTextField) TestUtils.getPrivateField(loginView, "usernameField");
        usernameField.setText("testUser");
        assertEquals("testUser", loginView.getUsername());
    }

    @Test
    public void testGetPassword() {
        JPasswordField passwordField = (JPasswordField) TestUtils.getPrivateField(loginView, "passwordField");
        passwordField.setText("testPassword");
        assertEquals("testPassword", loginView.getPassword());
    }

    @Test
    public void testViewVisibilityControls() {

        loginView.hideView();
        assertFalse(((JFrame) TestUtils.getPrivateField(loginView, "frame")).isVisible());

        loginView.disposeView();
        assertFalse(((JFrame) TestUtils.getPrivateField(loginView, "frame")).isDisplayable());
    }
}
