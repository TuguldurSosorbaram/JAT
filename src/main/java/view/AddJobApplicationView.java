package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;

public class AddJobApplicationView {
    private JDialog dialog;
    private JTextField positionField;
    private JTextField companyNameField;
    private JTextField salaryApproxField;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JSpinner dateSavedSpinner;
    private JSpinner deadlineSpinner;
    private JSpinner dateAppliedSpinner;
    private JSpinner followUpDateSpinner;
    private JSpinner excitementSpinner;
    private JButton saveButton;
    private JButton cancelButton;

    public AddJobApplicationView(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Add Job Application", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 600);

        // Input fields
        positionField = new JTextField();
        companyNameField = new JTextField();
        salaryApproxField = new JTextField();
        locationField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{"Applied", "Interview", "Offer", "Rejected"}); // Example statuses

        // Spinners for dates
        dateSavedSpinner = new JSpinner(new SpinnerDateModel());
        deadlineSpinner = new JSpinner(new SpinnerDateModel());
        dateAppliedSpinner = new JSpinner(new SpinnerDateModel());
        followUpDateSpinner = new JSpinner(new SpinnerDateModel());

        // Spinner for excitement level
        excitementSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1)); // Range from 1 to 5

        // Labels and Fields
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
        dialog.add(new JLabel("Date Saved:"));
        dialog.add(dateSavedSpinner);
        dialog.add(new JLabel("Deadline:"));
        dialog.add(deadlineSpinner);
        dialog.add(new JLabel("Date Applied:"));
        dialog.add(dateAppliedSpinner);
        dialog.add(new JLabel("Follow-Up Date:"));
        dialog.add(followUpDateSpinner);
        dialog.add(new JLabel("Excitement:"));
        dialog.add(excitementSpinner);

        // Buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setLocationRelativeTo(parentFrame);
    }

    public void addSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }

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
            return 0;
        }
    }

    public String getLocation() {
        return locationField.getText();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public Date getDateSaved() {
        return (Date) dateSavedSpinner.getValue();
    }

    public Date getDeadline() {
        return (Date) deadlineSpinner.getValue();
    }

    public Date getDateApplied() {
        return (Date) dateAppliedSpinner.getValue();
    }

    public Date getFollowUpDate() {
        return (Date) followUpDateSpinner.getValue();
    }

    public int getExcitement() {
        return (int) excitementSpinner.getValue();
    }

    public void showView() {
        dialog.setVisible(true);
    }

    public void hideView() {
        dialog.setVisible(false);
    }

    public void disposeView() {
        dialog.dispose();
    }
}
