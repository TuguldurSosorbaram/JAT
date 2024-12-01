package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.JobApplication;
import model.DatabaseHelper;
import view.AddJobApplicationView;

import java.sql.SQLException;

public class AddJobApplicationController {
    private AddJobApplicationView addJobView;
    private MainController mainController;

    public AddJobApplicationController(AddJobApplicationView addJobView, MainController mainController) {
        this.addJobView = addJobView;
        this.mainController = mainController;

        // Set up Save button action
        addJobView.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveJobApplication();
            }
        });

        // Set up Cancel button action
        addJobView.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainController.disposeAddView();
            }
        });
    }

    /**
     * Handles the process of saving a new job application.
     */
    private void handleSaveJobApplication() {
        JobApplication newJob = new JobApplication(
            addJobView.getPosition(),
            addJobView.getCompanyName(),
            addJobView.getSalaryApproximation(),
            addJobView.getLocation(),
            addJobView.getStatus(),
            new java.sql.Date(addJobView.getDeadline().getTime()),
            new java.sql.Date(addJobView.getDateApplied().getTime()),
            new java.sql.Date(addJobView.getFollowUpDate().getTime()),
            addJobView.getExcitement(),
            mainController.getLoggedUserId()
        );

        try {
            DatabaseHelper.addJobApplication(newJob); // Save the job application to the database
            mainController.disposeAddView(); // Close the form after saving
        } catch (SQLException ex) {
            addJobView.showErrorDialog("Error saving job application."); // Notify the user of an error
        }
    }
}
