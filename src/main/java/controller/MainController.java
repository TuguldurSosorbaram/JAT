package controller;

import javax.swing.JOptionPane;
import view.JobApplicationView;
import view.LoginView;
import view.RegistrationView;
import view.AddJobApplicationView;


public class MainController {
    private LoginView loginView;
    private AuthenticationController authController;
    
    private RegistrationView registrationView;
    private RegistrationController registrationController;
    
    private JobApplicationView jobApplicationView;
    private JobApplicationController jobApplicationController;
    
    private AddJobApplicationView addJAView;
    private AddJobApplicationController addJAController;

    // Constructor initializes the main flow by showing the login view
    public MainController() {
        showLoginView();
    }

    // Method to show login view
    public void showLoginView() {
        // Dispose any existing view before switching
        disposeCurrentView();

        // Create login view and controller
        loginView = new LoginView();
        authController = new AuthenticationController(loginView, this);
        loginView.showView();
    }

    // Method to show registration view
    public void showRegistrationView() {
        disposeCurrentView();  // Dispose current view (loginView in this case)

        // Create registration view and controller
        registrationView = new RegistrationView();
        registrationController = new RegistrationController(registrationView, this);
        registrationView.showView();
    }

    // Method to show job application tracking view (after login)
    public void showJobApplicationView() {
        disposeCurrentView();  // Dispose current view (loginView in this case)

        // Create the job application view and controller
        jobApplicationView = new JobApplicationView();
        jobApplicationController = new JobApplicationController(jobApplicationView, this);
        jobApplicationView.showView();
    }
    public void showAddJobApplication(){
        addJAView = new AddJobApplicationView(jobApplicationView.getFrame());
        
        addJAController = new AddJobApplicationController(addJAView, this);
        addJAView.showView();
        
    }

    // Helper method to dispose of the current view if it exists
    private void disposeCurrentView() {
        if (loginView != null) {
            loginView.disposeView();
            loginView = null;
            authController = null;
        }

        if (registrationView != null) {
            registrationView.disposeView();
            registrationView = null;
            registrationController = null;
        }

        if (jobApplicationView != null) {
            jobApplicationView.disposeView();
            jobApplicationView = null;
            jobApplicationController = null;
        }
    }
    public void disposeAddView(){
        if (addJAView != null) {
            addJAView.disposeView();
            addJAView = null;
            addJAController = null;
        }
        jobApplicationController.loadJobApplications();
    }

    // Additional utility methods like logging errors or notifications
    protected void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}

