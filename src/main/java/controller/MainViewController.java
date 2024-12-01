package controller;

import model.JobApplication;
import model.DatabaseHelper;
import view.MainView;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JOptionPane;

public class MainViewController {
    private MainView mainView;
    private MainController mainController;

    public MainViewController(MainView mainView, MainController mainController) {
        this.mainView = mainView;
        this.mainController = mainController;

        // Load all job applications when the application starts
        loadJobApplications();

        // Set up action listener for the Add Job button
        this.mainView.addAddButtonListener((ActionEvent e) -> handleAddJobApplication());

        // Set up action listener for the Edit Job button
        this.mainView.addEditButtonListener((ActionEvent e) -> handleEditJobApplication());

        // Set up action listener for the Delete Job button
        this.mainView.addDeleteButtonListener((ActionEvent e) -> handleDeleteJobApplication());

        // Set up action listener for the Log Out button
        this.mainView.addLogOutButtonListener((ActionEvent e) -> handleLogOut());

        // Handle in-line edits for job applications in the table
        mainView.addTableEditListener(e -> {
            if (e.getSource() instanceof JobApplication) {
                JobApplication updatedJob = (JobApplication) e.getSource();
                try {
                    DatabaseHelper.updateJobApplication(updatedJob);
                } catch (Exception ex) {
                    mainView.showMessage(mainView.getFrame(), "Failed to update the database.");
                }
            }
        });
    }

    /**
     * Loads job applications from the database and displays them in the view.
     */
    public void loadJobApplications() {
        try {
            List<JobApplication> applications =
                    DatabaseHelper.getJobApplicationsByUser(this.mainController.getLoggedUserId());
            if (applications == null) {
                applications = List.of();
            }
            mainView.setJobApplications(applications);
        } catch (Exception e) {
            this.mainView.showMessage(mainView.getFrame(), "Error loading job applications.");
        }
    }

    /**
     * Opens the view to add a new job application.
     */
    private void handleAddJobApplication() {
        mainController.showAddJobApplication();
    }

    /**
     * Opens the view to edit the selected job application.
     */
    private void handleEditJobApplication() {
        int selectedRow = mainView.getSelectedRow();
        if (selectedRow >= 0) {
            JobApplication selectedJob = mainView.getSelectedJobApplication();
            mainController.showEditJobApplication(selectedJob);
        } else {
            this.mainView.showMessage(null, "Please select a job application to edit.");
        }
    }

    /**
     * Deletes the selected job application after user confirmation.
     */
    private void handleDeleteJobApplication() {
        int selectedRow = mainView.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    mainView.getFrame(),
                    "Are you sure you want to delete the selected job application?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                JobApplication jobToDelete = mainView.getSelectedJobApplication();
                if (jobToDelete != null) {
                    DatabaseHelper.deleteJobApplication(jobToDelete);
                    loadJobApplications();
                }
            }
        } else {
            this.mainView.showMessage(null, "Please select a job application to delete.");
        }
    }

    /**
     * Logs out the current user and navigates to the login view.
     */
    private void handleLogOut() {
        this.mainController.logOutUser();
        this.mainController.showLoginView();
    }
}
