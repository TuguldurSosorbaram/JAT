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

        // Display all job applications on startup
        loadJobApplications();

        // Add action listener for the Add Job button
        this.mainView.addAddButtonListener((ActionEvent e) -> {
            handleAddJobApplication();
        });

        // Add action listener for the Edit Job button
        this.mainView.addEditButtonListener((ActionEvent e) -> {
            handleEditJobApplication();
        });
        this.mainView.addLogOutButtonListener((ActionEvent e) -> {
            handleLogOut();
        });
        
        
        mainView.addTableEditListener(e -> {
            if (e.getSource() instanceof JobApplication) {
                JobApplication updatedJob = (JobApplication) e.getSource();
                try {
                    DatabaseHelper.updateJobApplication(updatedJob);
                } catch (Exception ex) {
                    mainView.showMessage(mainView.getFrame(), "Failed to update excitement in the database.");
                }
            }
        });
    }

    // Load job applications from the database and display them in the view
    public void loadJobApplications() {
        try {
            List<JobApplication> applications = 
                    DatabaseHelper.getJobApplicationsByUser(this.mainController.getLoggedUserId());
            if (applications == null) {
                applications = List.of(); 
            }
            mainView.setJobApplications(applications);
        }catch (Exception e) {
            this.mainView.showMessage(mainView.getFrame(), "Error loading job applications.");
        }
    }

    // Handle adding a new job application
    private void handleAddJobApplication() {
        mainController.showAddJobApplication();
    }


    // Handle editing the selected job application
    private void handleEditJobApplication() {
        int selectedRow = mainView.getSelectedRow();
        if (selectedRow >= 0) {
            JobApplication selectedJob = mainView.getSelectedJobApplication();
            mainController.showEditJobApplication(selectedJob);
        } else {
            this.mainView.showMessage(null, "Please select a job application to edit.");
        }
    }
    private void handleLogOut(){
        this.mainController.logOutUser();
        this.mainController.showLoginView();
    }
}
