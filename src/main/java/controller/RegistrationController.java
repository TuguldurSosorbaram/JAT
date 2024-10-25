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

        try {
            // Save the new user to the database
            DatabaseHelper.registerUser(newUsername, newPassword);
            mainController.showLoginView();

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Registration failed!");
        }
    }
}
