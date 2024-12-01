package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.text.NumberFormatter;

/**
 * A view for adding a new job application, providing input fields for all
 * necessary job details and buttons to save or cancel the action.
 */
public class AddJobApplicationView {
    private JDialog dialog;
    private JTextField positionField;
    private JTextField companyNameField;
    private JTextField salaryApproxField;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JSpinner deadlineSpinner;
    private JSpinner dateAppliedSpinner;
    private JSpinner followUpDateSpinner;
    private JSpinner excitementSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    /**
     * Constructs the AddJobApplicationView with a parent frame.
     * Configures all input fields, labels, and buttons for user interaction.
     *
     * @param parentFrame the parent JFrame to attach the dialog to.
     */
    public AddJobApplicationView(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Add Job Application", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 600);

        // Input fields for job details
        positionField = new JTextField();
        companyNameField = new JTextField();
        
        // Formatter to restrict salary input to integers
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false); // Disable thousand separators
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(true); // Allow user to enter invalid data temporarily
        formatter.setMinimum(0); // Optional: restrict input to non-negative numbers
        salaryApproxField = new JFormattedTextField(formatter);

        locationField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{
            "Saved", "Applying", "Applied", "Interviewing", "Negotiating", 
            "Accepted", "I withdrew", "No response", "Rejected"
        });

        // Spinners for date inputs
        deadlineSpinner = new JSpinner(new SpinnerDateModel());
        dateAppliedSpinner = new JSpinner(new SpinnerDateModel());
        followUpDateSpinner = new JSpinner(new SpinnerDateModel());

        // Spinner for excitement level (1 to 5)
        excitementSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));

        // Adding labels and input fields to the dialog
        dialog.add(new JLabel("Position:"));
        dialog.add(positionField);
        dialog.add(new JLabel("Company Name:"));
        dialog.add(companyNameField);
        dialog.add(new JLabel("Salary Approximation:"));
        dialog.add(salaryApproxField);
        dialog.add(new JLabel("Location:"));
        dialog.add(locationField);
        dialog.add(new JLabel("Status:"));
        dialog.add(statusComboBox);
        dialog.add(new JLabel("Deadline:"));
        dialog.add(deadlineSpinner);
        dialog.add(new JLabel("Date Applied:"));
        dialog.add(dateAppliedSpinner);
        dialog.add(new JLabel("Follow-Up Date:"));
        dialog.add(followUpDateSpinner);
        dialog.add(new JLabel("Excitement:"));
        dialog.add(excitementSpinner);

        // Adding Save and Cancel buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        // Center dialog on the parent frame
        dialog.setLocationRelativeTo(parentFrame);
    }

    /**
     * Adds an ActionListener to the Save button.
     *
     * @param listener the ActionListener to handle save actions.
     */
    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    /**
     * Adds an ActionListener to the Cancel button.
     *
     * @param listener the ActionListener to handle cancel actions.
     */
    public void addCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

    // Getters for retrieving user input from the form fields

    public String getPosition() {
        return positionField.getText();
    }

    public String getCompanyName() {
        return companyNameField.getText();
    }

    public int getSalaryApproximation() {
        try {
            return Integer.parseInt(salaryApproxField.getText());
        } catch (NumberFormatException e) {
            return 0; // Default to 0 if parsing fails
        }
    }

    public String getLocation() {
        return locationField.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public Date getDeadline() {
        return new java.sql.Date(((Date) deadlineSpinner.getValue()).getTime());
    }

    public Date getDateApplied() {
        return new java.sql.Date(((Date) dateAppliedSpinner.getValue()).getTime());
    }

    public Date getFollowUpDate() {
        return new java.sql.Date(((Date) followUpDateSpinner.getValue()).getTime());
    }

    public int getExcitement() {
        return (int) excitementSpinner.getValue();
    }

    // Methods for controlling the dialog visibility

    /**
     * Displays the dialog to the user.
     */
    public void showView() {
        dialog.setVisible(true);
    }

    /**
     * Hides the dialog from the user.
     */
    public void hideView() {
        dialog.setVisible(false);
    }

    /**
     * Disposes of the dialog resources.
     */
    public void disposeView() {
        dialog.dispose();
    }

    /**
     * Displays an error message dialog with the given text.
     *
     * @param errorText the error message to display.
     */
    public void showErrorDialog(String errorText) {
        JOptionPane.showMessageDialog(null, errorText);
    }
}
