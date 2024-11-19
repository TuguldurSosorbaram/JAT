package controller;

import model.JobApplication;
import model.DatabaseHelper;
import view.JobApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class JobApplicationController {
    private JobApplicationView jobView;
    private MainController mainController;

    public JobApplicationController(JobApplicationView jobView, MainController mainController) {
        this.jobView = jobView;
        this.mainController = mainController;

        // Display all job applications on startup
        loadJobApplications();

        // Add action listener for the Add Job button
        this.jobView.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding a new job application
                handleAddJobApplication();
            }
        });

        // Add action listener for the Edit Job button
        this.jobView.addEditButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEditJobApplication();
            }
        });
    }

    // Load job applications from the database and display them in the view
    public void loadJobApplications() {
        try {
            List<JobApplication> applications = DatabaseHelper.getAllJobApplications();
            jobView.displayJobApplications(applications);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading job applications.");
        }
    }

    // Handle adding a new job application
    private void handleAddJobApplication() {
        mainController.showAddJobApplication();
    }


    // Handle editing the selected job application
    private void handleEditJobApplication() {
        int selectedRow = jobView.getSelectedRow();
        if (selectedRow >= 0) {
            // Logic to edit the selected job application
            JobApplication job = jobView.getJobApplicationFromRow(selectedRow);
            // After editing, update the database and refresh the table
            DatabaseHelper.updateJobApplication(job);
            loadJobApplications();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a job application to edit.");
        }
    }
}
