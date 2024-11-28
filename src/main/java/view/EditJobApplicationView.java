package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.text.NumberFormatter;
import model.JobApplication;

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

    public EditJobApplicationView(JFrame parentFrame) {
        dialog = new JDialog(parentFrame, "Edit Job Application", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 600);

        // Input fields
        positionField = new JTextField();
        companyNameField = new JTextField();
        
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(true);
        formatter.setMinimum(0);
        salaryApproxField = new JFormattedTextField(formatter);

        locationField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{"Saved", "Applying", "Applied", "Interviewing", "Negotiating", "Accepted", "I withdrew", "No response", "Rejected"});

        // Spinners for dates
        deadlineSpinner = new JSpinner(new SpinnerDateModel());
        dateAppliedSpinner = new JSpinner(new SpinnerDateModel());
        followUpDateSpinner = new JSpinner(new SpinnerDateModel());

        // Spinner for excitement level
        excitementSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));

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

    public void showView() {
        dialog.setVisible(true);
    }

    public void hideView() {
        dialog.setVisible(false);
    }

    public void disposeView() {
        dialog.dispose();
    }
    public void showDialog(String Text){
        JOptionPane.showMessageDialog(null, Text);
    }
}
