package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLabel;

    public LoginView() {
        frame = new JFrame("Login");
        frame.setSize(650, 350); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center the frame on screen
        frame.setLocationRelativeTo(null);

        // Left panel with updated code
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(66, 136, 245));  // Updated color
        leftPanel.setPreferredSize(new Dimension(250, 300));  // Set width for the left panel
        leftPanel.setLayout(null);  // Using absolute layout for custom placement of content

        // App name as JTextArea to allow text wrapping
        JTextArea appName = new JTextArea("Job Application Tracker");
        appName.setFont(new Font("Inter", Font.BOLD, 24));  // Increased font size
        appName.setForeground(Color.WHITE);
        appName.setBounds(20, 50, 210, 60);  // Increased height
        appName.setWrapStyleWord(true);  // Enable word wrap
        appName.setLineWrap(true);
        appName.setOpaque(false);  // Transparent background
        appName.setEditable(false);
        appName.setFocusable(false);
        leftPanel.add(appName);
        
        // App description
        JLabel appDescriptionLabel = new JLabel("<html>Your personal job application tracking tool.<br>Keep track of all your applications easily.</html>");
        appDescriptionLabel.setForeground(Color.WHITE);
        appDescriptionLabel.setBounds(20, 120, 210, 100);  // Position in the panel
        appDescriptionLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        leftPanel.add(appDescriptionLabel);

        // Add left panel to the frame
        frame.add(leftPanel, BorderLayout.WEST);

        // Main login panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 240, 240));  // Light gray background
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add components to the main panel
        placeComponents(mainPanel);
    }

    private void placeComponents(JPanel panel) {
        // Title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));  // Using Inter font
        titleLabel.setBounds(130, 20, 100, 40);
        panel.add(titleLabel);

        // Username Label and TextField
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));  // Using Inter font
        userLabel.setBounds(50, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 80, 200, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        usernameField.setFont(new Font("Inter", Font.PLAIN, 14));  // Using Inter font
        panel.add(usernameField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));  // Using Inter font
        passwordLabel.setBounds(50, 120, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 120, 200, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14));  // Using Inter font
        panel.add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 160, 100, 35);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Inter", Font.BOLD, 14));  // Using Inter font
        loginButton.setBackground(new Color(66, 135, 245));  // Blue button
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(66, 135, 245), 2));
        panel.add(loginButton);

        // Hover effect for the login button
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(55, 115, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(66, 135, 245));
            }
        });

        // Register label with hand cursor
        registerLabel = new JLabel("<HTML><U>Register</U></HTML>");
        registerLabel.setFont(new Font("Inter", Font.PLAIN, 14));  // Using Inter font
        registerLabel.setBounds(150, 210, 100, 25);
        registerLabel.setForeground(new Color(66, 135, 245));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Change cursor to hand when hovering
        panel.add(registerLabel);
    }
    
    public void showView() {
        frame.setVisible(true);
    }
    public void hideView(){
        frame.setVisible(false);
    }
    public void disposeView() {
        frame.dispose();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(MouseAdapter listener) {
        registerLabel.addMouseListener(listener);
    }
}
