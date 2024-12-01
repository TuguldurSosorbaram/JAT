package controller;

import model.DatabaseHelper;
import view.RegistrationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrationController {
    private RegistrationView registrationView;
    private MainController mainController;
    private boolean testMode = false;

    public RegistrationController(RegistrationView registrationView, MainController mainController) {
        this.registrationView = registrationView;
        this.mainController = mainController;

        // Set up listener for the register button
        registrationView.addRegisterListener(e -> handleRegistration());

        // Set up listener for navigating back to the login view
        registrationView.addBackToLoginListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainController.showLoginView();
            }
        });
    }

    /**
     * Handles user registration, including input validation and database interaction.
     */
    private void handleRegistration() {
        String newUsername = registrationView.getUsername();
        String newPassword = registrationView.getPassword();

        // Clear previous error messages
        registrationView.clearErrors();

        boolean isValid = validateInput(newUsername, newPassword);

        if (!isValid) {
            return; // Stop registration if validation fails
        }

        try {
            if (DatabaseHelper.registerUser(newUsername, newPassword)) {
                mainController.showLoginView();
            } else {
                registrationView.setUsernameError("This username is already taken.");
            }
        } catch (Exception ex) {
            if (!testMode) {
                ex.printStackTrace();
            }
            registrationView.setUsernameError("Registration failed! Please try again.");
        }
    }

    /**
     * Validates username and password input fields.
     *
     * @param username the input username
     * @param password the input password
     * @return true if both username and password pass validation, false otherwise
     */
    private boolean validateInput(String username, String password) {
        boolean isValid = true;

        // Validate username
        if (username.length() < 4) {
            registrationView.setUsernameError("Username must be longer than 4 characters.");
            isValid = false;
        } else if (!Character.isLetter(username.charAt(0))) {
            registrationView.setUsernameError("Username must start with a letter.");
            isValid = false;
        } else if (!username.matches("[A-Za-z0-9]+")) {
            registrationView.setUsernameError("Username can only contain letters and numbers.");
            isValid = false;
        }

        // Validate password
        if (password.length() < 8) {
            registrationView.setPasswordError("Password must be at least 8 characters long.");
            isValid = false;
        } else if (!password.matches(".*\\d.*")) {
            registrationView.setPasswordError("Password must include at least one number.");
            isValid = false;
        } else if (!password.matches(".*[A-Z].*")) {
            registrationView.setPasswordError("Password must include at least one uppercase letter.");
            isValid = false;
        } else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            registrationView.setPasswordError("Password must include at least one special character.");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Enables test mode to suppress exception stack traces during testing.
     */
    public void enableTestMode() {
        this.testMode = true;
    }
}
