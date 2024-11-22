package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.JobApplication;
import model.DatabaseHelper;
import view.AddJobApplicationView;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AddJobApplicationController {
    private AddJobApplicationView addJobView;
    private MainController mainController;

    public AddJobApplicationController(AddJobApplicationView addJobView, MainController mainController) {
        this.addJobView = addJobView;
        this.mainController =  mainController;
        
        // Add action listener for the Save button
        addJobView.addSaveButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveJobApplication();
            }
        });

        // Add action listener for the Cancel button
        addJobView.addCancelButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddJobApplicationController.this.mainController.disposeAddView();
            }
        });
    }

    private void handleSaveJobApplication() {
        JobApplication newJob = new JobApplication();
        newJob.setPosition(addJobView.getPosition());
        newJob.setCompanyName(addJobView.getCompanyName());
        newJob.setSalaryApproximation(addJobView.getSalaryApproximation());
        newJob.setLocation(addJobView.getLocation());
        newJob.setStatus(addJobView.getStatus());
        newJob.setDateSaved(new java.sql.Date(addJobView.getDateSaved().getTime()));
        newJob.setDeadline(new java.sql.Date(addJobView.getDeadline().getTime()));
        newJob.setDateApplied(new java.sql.Date(addJobView.getDateApplied().getTime()));
        newJob.setFollowUpDate(new java.sql.Date(addJobView.getFollowUpDate().getTime()));
        newJob.setExcitement(addJobView.getExcitement());

        try {
            DatabaseHelper.addJobApplication(newJob); // Save to database
            this.mainController.disposeAddView(); // Close the add form
        } catch (SQLException ex) {
            Logger.getLogger(AddJobApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error saving job application.");
        }
    }
}
