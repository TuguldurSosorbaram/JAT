package controller;

import model.DatabaseHelper;
import view.LoginView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthenticationController {
    private LoginView loginView;
    private MainController mainController;

    public AuthenticationController(LoginView loginView, MainController mainController) {
        this.loginView = loginView;
        this.mainController = mainController;

        // Set up Login button action
        this.loginView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Set up Register link click action
        this.loginView.addRegisterListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainController.showRegistrationView();
            }
        });

        // Set up Enter key action for login
        loginView.addKeyListenerForEnter(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    /**
     * Validates user credentials and navigates to the main view on successful login.
     */
    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        try {
            if (DatabaseHelper.validateUser(username, password)) {
                mainController.showMainView(DatabaseHelper.getUserIdByUsername(username));
            } else {
                mainController.showError("Login failed! Please check your credentials.");
            }
        } catch (Exception ex) {
            mainController.showError("An error occurred during login.");
        }
    }
}
