package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * LoginView provides the user interface for user authentication.
 * Users can enter their credentials and either login or navigate to the registration page.
 */
public class LoginView {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLabel;

    /**
     * Constructs the LoginView and initializes the UI components.
     * Configures the layout, colors, and interaction elements.
     */
    public LoginView() {
        frame = new JFrame("Login");
        frame.setSize(650, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Center the frame on the screen
        frame.setLocationRelativeTo(null);

        // Left panel with app description
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(66, 136, 245)); // Blue background
        leftPanel.setPreferredSize(new Dimension(250, 300));
        leftPanel.setLayout(null); // Absolute layout for precise positioning

        // Application name displayed on the left panel
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

        // Add the left panel to the frame
        frame.add(leftPanel, BorderLayout.WEST);

        // Main login panel
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(240, 240, 240)); // Light gray background
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add components to the main panel
        placeComponents(mainPanel);
    }

    /**
     * Places and configures all components on the main panel.
     *
     * @param panel the panel where components will be added.
     */
    private void placeComponents(JPanel panel) {
        // Title label
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setBounds(130, 20, 100, 40);
        panel.add(titleLabel);

        // Username Label and TextField
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        userLabel.setBounds(50, 80, 100, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 80, 200, 30);
        usernameField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        usernameField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(usernameField);

        // Password Label and PasswordField
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordLabel.setBounds(50, 120, 100, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 120, 200, 30);
        passwordField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14));
        panel.add(passwordField);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 160, 100, 35);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Inter", Font.BOLD, 14));
        loginButton.setBackground(new Color(66, 135, 245)); // Blue button background
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createLineBorder(new Color(66, 135, 245), 2));
        panel.add(loginButton);

        // Hover effect for the login button
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(55, 115, 230)); // Slightly darker blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(66, 135, 245)); // Original blue
            }
        });

        // Register label with underline and hand cursor
        registerLabel = new JLabel("<HTML><U>Register</U></HTML>");
        registerLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        registerLabel.setBounds(150, 210, 100, 25);
        registerLabel.setForeground(new Color(66, 135, 245));
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(registerLabel);

        // Hide view by default
        this.hideView();
    }

    // Methods for showing, hiding, and disposing the view

    /**
     * Displays the login view to the user.
     */
    public void showView() {
        frame.setVisible(true);
    }

    /**
     * Hides the login view from the user.
     */
    public void hideView() {
        frame.setVisible(false);
    }

    /**
     * Disposes of the login view and its resources.
     */
    public void disposeView() {
        frame.dispose();
    }

    // Getters for retrieving user input

    /**
     * Retrieves the entered username from the username field.
     *
     * @return the entered username as a string.
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Retrieves the entered password from the password field.
     *
     * @return the entered password as a string.
     */
    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Methods for adding event listeners

    /**
     * Adds an ActionListener to the login button.
     *
     * @param listener the ActionListener to handle login actions.
     */
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    /**
     * Adds a MouseAdapter to the register label.
     *
     * @param listener the MouseAdapter to handle register actions.
     */
    public void addRegisterListener(MouseAdapter listener) {
        registerLabel.addMouseListener(listener);
    }

    /**
     * Adds a KeyAdapter to the username and password fields for handling Enter key events.
     *
     * @param listener the KeyAdapter to handle key events.
     */
    public void addKeyListenerForEnter(KeyAdapter listener) {
        usernameField.addKeyListener(listener);
        passwordField.addKeyListener(listener);
    }
}
