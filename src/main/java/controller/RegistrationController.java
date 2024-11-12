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

    public RegistrationController(RegistrationView registrationView, MainController mainController) {
        this.registrationView = registrationView;
        this.mainController = mainController;

        // Add action listener for register button
        this.registrationView.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegistration();
            }
        });

        this.registrationView.addBackToLoginListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainController.showLoginView();
            }
        });
    }

    private void handleRegistration() {
        String newUsername = registrationView.getUsername();
        String newPassword = registrationView.getPassword();

        // Clear previous error messages
        registrationView.clearErrors();

        boolean isValid = true;

        // Validate username
        if (newUsername.length() < 4) {
            registrationView.setUsernameError("Username must be longer than 4 characters.");
            isValid = false;
        } else if (!Character.isLetter(newUsername.charAt(0))) {
            registrationView.setUsernameError("Username must start with a letter.");
            isValid = false;
        } else if (!newUsername.matches("[A-Za-z0-9]+")) {
            registrationView.setUsernameError("Username can only contain letters and numbers.");
            isValid = false;
        }

        // Validate password
        if (newPassword.length() < 8) {
            registrationView.setPasswordError("Password must be at least 8 characters long.");
            isValid = false;
        } else if (!newPassword.matches(".*\\d.*")) {
            registrationView.setPasswordError("Password must include at least one number.");
            isValid = false;
        } else if (!newPassword.matches(".*[A-Z].*")) {
            registrationView.setPasswordError("Password must include at least one uppercase letter.");
            isValid = false;
        } else if (!newPassword.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            registrationView.setPasswordError("Password must include at least one special character.");
            isValid = false;
        }

        if (!isValid) {
            return; // Stop registration if validation fails
        }

        try {
            // Save the new user to the database
            DatabaseHelper.registerUser(newUsername, newPassword);
            mainController.showLoginView();
        } catch (Exception ex) {
            ex.printStackTrace();
            registrationView.setUsernameError("Registration failed! Please try again.");
        }
    }
}
