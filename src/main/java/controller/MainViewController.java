package controller;

import model.JobApplication;
import model.DatabaseHelper;
import view.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        this.mainView.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle adding a new job application
                handleAddJobApplication();
            }
        });

        // Add action listener for the Edit Job button
        this.mainView.addEditButtonListener(new ActionListener() {
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
            mainView.setJobApplications(applications);
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
        int selectedRow = mainView.getSelectedRow();
        if (selectedRow >= 0) {
            loadJobApplications();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a job application to edit.");
        }
    }
}
