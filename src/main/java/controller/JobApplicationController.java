package controller;

import model.JobApplication;
import model.DatabaseHelper;
import view.JobApplicationView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class JobApplicationController {
    private JobApplicationView jobView;

    public JobApplicationController(JobApplicationView jobView) {
        this.jobView = jobView;

        // Add action listener for the Add Job button
        this.jobView.addAddButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //handleAddJobApplication();
            }
        });
    }

//    private void handleAddJobApplication() {
//        // Dummy code for adding a job
//        JobApplication newJob = new JobApplication("Dummy Company", "Software Engineer", new Date());
//        try {
//            DatabaseHelper.addJobApplication(newJob);
//            List<JobApplication> applications = DatabaseHelper.getAllApplications();
//            jobView.displayJobApplications(applications);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
