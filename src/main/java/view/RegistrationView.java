package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrationView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JLabel backToLoginLabel;
    private JLabel usernameErrorLabel;
    private JLabel passwordErrorLabel;

    public RegistrationView() {
        frame = new JFrame("Register");
        frame.setSize(650, 350);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center the frame on screen
        frame.setLocationRelativeTo(null);

        // Left panel with consistent design
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(66, 136, 245));
        leftPanel.setPreferredSize(new Dimension(250, 300));
        leftPanel.setLayout(null);

        // App name as JTextArea to allow text wrapping
        JTextArea appName = new JTextArea("Job Application Tracker");
        appName.setFont(new Font("Inter", Font.BOLD, 24));
        appName.setForeground(Color.WHITE);
        appName.setBounds(20, 50, 210, 60);
        appName.setWrapStyleWord(true);
        appName.setLineWrap(true);
        appName.setOpaque(false);
        appName.setEditable(false);
        appName.setFocusable(false);
        leftPanel.add(appName);

        // App description
        JLabel appDescriptionLabel = new JLabel("<html>Your personal job application tracking tool.<br>Register to keep track of your applications.</html>");
        appDescriptionLabel.setForeground(Color.WHITE);
        appDescriptionLabel.setBounds(20, 120, 210, 100);
        appDescriptionLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        leftPanel.add(appDescriptionLabel);

        // Add left panel to the frame
        frame.add(leftPanel, BorderLayout.WEST);

        // Main registration panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 240, 240));
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add components to the main panel
        placeComponents(mainPanel);

        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        // Title label
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setBounds(130, 20, 150, 40);
        panel.add(titleLabel);

        // Username Label and TextField
        JLabel userLabel = new JLabel("New Username:");
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        userLabel.setBounds(30, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 80, 200, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        usernameField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(usernameField);

        // Username error label with wider bounds and HTML wrapping
        usernameErrorLabel = new JLabel("");
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setBounds(150, 110, 230, 30); // Increased width
        usernameErrorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        panel.add(usernameErrorLabel);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("New Password:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordLabel.setBounds(30, 140, 150, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 140, 200, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(passwordField);

        // Password error label with wider bounds and HTML wrapping
        passwordErrorLabel = new JLabel("");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setBounds(150, 170, 230, 30); // Increased width
        passwordErrorLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        panel.add(passwordErrorLabel);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setBounds(150, 200, 100, 35);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Inter", Font.BOLD, 14));
        registerButton.setBackground(new Color(66, 135, 245));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createLineBorder(new Color(66, 135, 245), 2));
        panel.add(registerButton);

        // Hover effect for the register button
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                registerButton.setBackground(new Color(55, 115, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                registerButton.setBackground(new Color(66, 135, 245));
            }
        });

        // Back to login label with hand cursor
        backToLoginLabel = new JLabel("<HTML><U>Back to Login</U></HTML>");
        backToLoginLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        backToLoginLabel.setBounds(150, 250, 150, 25);
        backToLoginLabel.setForeground(new Color(66, 135, 245));
        backToLoginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(backToLoginLabel);
    }

    // Method to make the registration view visible
    public void showView() {
        frame.setVisible(true);
    }

    public void hideView() {
        frame.setVisible(false);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Add listener for the register button
    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addBackToLoginListener(MouseAdapter listener) {
        backToLoginLabel.addMouseListener(listener);
    }

    // Methods to set error messages with HTML for wrapping
    public void setUsernameError(String message) {
        usernameErrorLabel.setText("<html>" + message + "</html>");
    }

    public void setPasswordError(String message) {
        passwordErrorLabel.setText("<html>" + message + "</html>");
    }

    public void clearErrors() {
        usernameErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    public void disposeView() {
        frame.dispose();
    }
}
