package controller;

import model.DatabaseHelper;
import model.JobApplication;
import view.EditJobApplicationView;

public class EditJobApplicationController {
    private MainController mainController;
    private EditJobApplicationView editView;
    private JobApplication originalJob;

    public EditJobApplicationController(MainController mainController, EditJobApplicationView editView, JobApplication jobApplication) {
        this.mainController = mainController;
        this.originalJob = jobApplication; // Store the original job application
        this.editView = editView;

        // Populate fields with the details of the selected job application
        editView.setJobApplication(originalJob);

        // Set up listeners for Save and Cancel buttons
        editView.addSaveButtonListener(e -> handleSave());
        editView.addCancelButtonListener(e -> mainController.disposeEditView());
    }

    /**
     * Handles saving changes to the job application.
     */
    private void handleSave() {
        try {
            // Retrieve updated details from the view
            JobApplication updatedJob = editView.getUpdatedJobApplication();

            // Retain original immutable fields
            updatedJob.setId(originalJob.getId());
            updatedJob.setDateSaved(originalJob.getDateSaved());
            updatedJob.setUserId(originalJob.getUserId());

            // Update the database with the modified job application
            DatabaseHelper.updateJobApplication(updatedJob);

            // Notify user and close the edit view
            mainController.disposeEditView();
            editView.showDialog("Job application updated successfully.");
        } catch (Exception ex) {
            editView.showDialog("Error updating job application.");
        }
    }
}
