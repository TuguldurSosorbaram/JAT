package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * RegistrationView provides the user interface for new user registration.
 * Allows users to enter their credentials and register, with error messages displayed for invalid input.
 */
public class RegistrationView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel backToLoginLabel;
    private JLabel usernameErrorLabel;
    private JLabel passwordErrorLabel;

    /**
     * Constructs the RegistrationView and initializes the UI components.
     */
    public RegistrationView() {
        frame = new JFrame("Register");
        frame.setSize(650, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center the frame on screen
        frame.setLocationRelativeTo(null);

        // Left panel with application description
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(66, 136, 245)); // Blue background
        leftPanel.setPreferredSize(new Dimension(250, 300));
        leftPanel.setLayout(null);

        // Application name
        JTextArea appName = new JTextArea("Job Application Tracker");
        appName.setFont(new Font("Inter", Font.BOLD, 24));
        appName.setForeground(Color.WHITE);
        appName.setBounds(20, 50, 210, 60);
        appName.setWrapStyleWord(true);
        appName.setLineWrap(true);
        appName.setOpaque(false); // Transparent background
        appName.setEditable(false);
        appName.setFocusable(false);
        leftPanel.add(appName);

        // Application description
        JLabel appDescriptionLabel = new JLabel("<html><div style='text-align: center;'>Your personal job application tracking tool.<br>Keep track of all your applications easily.<br> Author: Tuguldur Sosorbaram</html>");
        appDescriptionLabel.setForeground(Color.WHITE);
        appDescriptionLabel.setBounds(20, 120, 210, 100);
        appDescriptionLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        leftPanel.add(appDescriptionLabel);

        // Add left panel to the frame
        frame.add(leftPanel, BorderLayout.WEST);

        // Main registration panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add input fields and labels to the main panel
        placeComponents(mainPanel);

        // Initially hide the frame
        frame.setVisible(false);
    }

    /**
     * Adds input fields, labels, and buttons to the main panel.
     *
     * @param panel the main panel to populate.
     */
    private void placeComponents(JPanel panel) {
        // Title label
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setBounds(130, 20, 150, 40);
        panel.add(titleLabel);

        // Username label and input field
        JLabel userLabel = new JLabel("New Username:");
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        userLabel.setBounds(30, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 80, 200, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        usernameField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(usernameField);

        // Error label for username
        usernameErrorLabel = new JLabel("");
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setBounds(150, 110, 230, 30); // Positioned below the username field
        usernameErrorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        panel.add(usernameErrorLabel);

        // Password label and input field
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 140, 150, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 140, 200, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(passwordField);

        // Error label for password
        passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setBounds(150, 170, 230, 30); // Positioned below the password field
        passwordErrorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        panel.add(passwordErrorLabel);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setBounds(150, 200, 100, 35);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Inter", Font.BOLD, 14));
        registerButton.setBackground(new Color(66, 135, 245)); // Blue background
        registerButton.setForeground(Color.WHITE); // White text
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(66, 135, 245), 2));
        panel.add(registerButton);

        // Hover effect for the register button
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(55, 115, 230)); // Slightly darker blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(66, 135, 245)); // Original blue
            }
        });

        // Back to login label
        backToLoginLabel = new JLabel("<HTML><U>Back to Login</U></HTML>");
        backToLoginLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        backToLoginLabel.setBounds(150, 250, 150, 25);
        backToLoginLabel.setForeground(new Color(66, 135, 245)); // Blue text
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(backToLoginLabel);

        // Initially hide the view
        this.hideView();
    }

    // Methods for showing, hiding, and disposing the view

    /**
     * Displays the registration view.
     */
    public void showView() {
        frame.setVisible(true);
    }

    /**
     * Hides the registration view.
     */
    public void hideView() {
        frame.setVisible(false);
    }

    /**
     * Disposes of the registration view and its resources.
     */
    public void disposeView() {
        frame.dispose();
    }

    // Getters for retrieving user input

    /**
     * Retrieves the entered username.
     *
     * @return the username as a string.
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Retrieves the entered password.
     *
     * @return the password as a string.
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Methods for managing error messages

    /**
     * Sets an error message for the username field.
     *
     * @param message the error message to display.
     */
    public void setUsernameError(String message) {
        usernameErrorLabel.setText("<html>" + message + "</html>");
    }

    /**
     * Sets an error message for the password field.
     *
     * @param message the error message to display.
     */
    public void setPasswordError(String message) {
        passwordErrorLabel.setText("<html>" + message + "</html>");
    }

    /**
     * Clears all error messages.
     */
    public void clearErrors() {
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    // Methods for adding listeners

    /**
     * Adds an ActionListener to the register button.
     *
     * @param listener the ActionListener to handle registration actions.
     */
    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    /**
     * Adds a MouseAdapter to the back-to-login label.
     *
     * @param listener the MouseAdapter to handle navigation back to the login screen.
     */
    public void addBackToLoginListener(MouseAdapter listener) {
        backToLoginLabel.addMouseListener(listener);
    }
}
