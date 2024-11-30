package controller;

import javax.swing.JOptionPane;
import model.JobApplication;
import view.MainView;
import view.LoginView;
import view.RegistrationView;
import view.AddJobApplicationView;
import view.EditJobApplicationView;


public class MainController {
    int loggedUserID = -1;
    
    private LoginView loginView;
    private AuthenticationController authController;
    
    private RegistrationView registrationView;
    private RegistrationController registrationController;
    
    private MainView mainView;
    private MainViewController mainViewController;
    
    private AddJobApplicationView addJAView;
    private AddJobApplicationController addJAController;
    
    private EditJobApplicationView editView;
    private EditJobApplicationController editJAController;

    // Constructor initializes the main flow by showing the login view
    public MainController() {
        showLoginView();
    }
    public int getLoggedUserId(){
        return this.loggedUserID;
    }
    public void logOutUser(){
        this.loggedUserID = -1;
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
    public void showMainView(int user_id) {
        this.loggedUserID = user_id;
        disposeCurrentView();  // Dispose current view (loginView in this case)

        // Create the job application view and controller
        mainView = new MainView();
        mainViewController = new MainViewController(mainView, this);
        mainView.showView();
    }
    public void showAddJobApplication(){
        addJAView = new AddJobApplicationView(mainView.getFrame());
        
        addJAController = new AddJobApplicationController(addJAView, this);
        addJAView.showView();
    }
    
    public void showEditJobApplication(JobApplication job){
        editView = new EditJobApplicationView(mainView.getFrame());
        
        editJAController = new EditJobApplicationController(this, editView, job );
        editView.showView();
    }

    // Helper method to dispose of the current view if it exists
    private void disposeCurrentView() {
        this.disposeAddView();
        this.disposeEditView();
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

        if (mainView != null) {
            mainView.disposeView();
            mainView = null;
            mainViewController = null;
        }
    }
    public void disposeAddView(){
        if (addJAView != null) {
            addJAView.disposeView();
            addJAView = null;
            addJAController = null;
            mainViewController.loadJobApplications();
        }
       
    }
    public void disposeEditView(){
        if (editView != null) {
                editView.disposeView();
                editView = null;
                editJAController = null;
                mainViewController.loadJobApplications();
            }
       
    }
    

    // Additional utility methods like logging errors or notifications
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}

