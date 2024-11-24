package controller;

import model.DatabaseHelper;
import model.JobApplication;
import view.EditJobApplicationView;

import javax.swing.*;

public class EditJobApplicationController {
    private MainController mainController;
    private EditJobApplicationView editView;
    private JobApplication originalJob;

    public EditJobApplicationController(MainController mainController, EditJobApplicationView editView, JobApplication jobApplication) {
        this.mainController = mainController;
        this.originalJob = jobApplication; // Save the original job application
        this.editView = editView;

        // Pre-fill the fields in the view with the selected job's details
        editView.setJobApplication(originalJob);

        // Add listeners for save and cancel buttons
        editView.addSaveButtonListener(e -> handleSave());
        editView.addCancelButtonListener(e -> this.mainController.disposeEditView());
    }

    private void handleSave() {
        try {
            // Get the updated job application details from the view
            JobApplication updatedJob = editView.getUpdatedJobApplication();

            // Preserve the original ID
            updatedJob.setId(originalJob.getId());
            updatedJob.setDateSaved(originalJob.getDateSaved());

            // Update the database
            DatabaseHelper.updateJobApplication(updatedJob);

            // Close the view after saving
            this.mainController.disposeEditView();
            JOptionPane.showMessageDialog(null, "Job application updated successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
            //JOptionPane.showMessageDialog(editView.getDialog(), "Error updating job application: " + ex.getMessage());
        }
    }
}
