package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.text.NumberFormatter;
import model.JobApplication;

/**
 * A view for editing an existing job application.
 * Allows the user to update fields such as position, company name, salary, location, status, dates, and excitement level.
 */
public class EditJobApplicationView {
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
     * Constructs the EditJobApplicationView with a parent frame.
     * Configures all input fields, labels, and buttons for user interaction.
     *
     * @param parentFrame the parent JFrame to attach the dialog to.
     */
    public EditJobApplicationView(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Edit Job Application", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 600);

        // Input fields for job details
        positionField = new JTextField();
        companyNameField = new JTextField();

        // Formatter to restrict salary input to integers
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(true); // Allow invalid data temporarily
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

    /**
     * Populates the input fields with the data from the given JobApplication.
     *
     * @param job the JobApplication to load data from.
     */
    public void setJobApplication(JobApplication job) {
        positionField.setText(job.getPosition());
        companyNameField.setText(job.getCompanyName());
        salaryApproxField.setText(String.valueOf(job.getSalaryApproximation()));
        locationField.setText(job.getLocation());
        statusComboBox.setSelectedItem(job.getStatus());
        deadlineSpinner.setValue(job.getDeadline());
        dateAppliedSpinner.setValue(job.getDateApplied());
        followUpDateSpinner.setValue(job.getFollowUpDate());
        excitementSpinner.setValue(job.getExcitement());
    }

    /**
     * Retrieves the updated JobApplication based on the user input.
     *
     * @return a JobApplication object populated with the updated values.
     */
    public JobApplication getUpdatedJobApplication() {
        return new JobApplication(
            positionField.getText(),
            companyNameField.getText(),
            Integer.parseInt(salaryApproxField.getText()),
            locationField.getText(),
            (String) statusComboBox.getSelectedItem(),
            new java.sql.Date(((Date) deadlineSpinner.getValue()).getTime()),
            new java.sql.Date(((Date) dateAppliedSpinner.getValue()).getTime()),
            new java.sql.Date(((Date) followUpDateSpinner.getValue()).getTime()),
            (int) excitementSpinner.getValue(),
            -1
        );
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
     * Displays a message dialog with the provided text.
     *
     * @param text the message to display.
     */
    public void showDialog(String text) {
        JOptionPane.showMessageDialog(null, text);
    }
}
