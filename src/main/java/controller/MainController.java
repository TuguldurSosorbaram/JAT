package controller;

import javax.swing.JOptionPane;
import model.JobApplication;
import view.MainView;
import view.LoginView;
import view.RegistrationView;
import view.AddJobApplicationView;
import view.EditJobApplicationView;

public class MainController {
    private int loggedUserID = -1;

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

    /**
     * Initializes the application by showing the login view.
     */
    public MainController() {
        showLoginView();
    }

    public int getLoggedUserId() {
        return loggedUserID;
    }

    public void logOutUser() {
        loggedUserID = -1;
    }

    /**
     * Displays the login view and initializes its controller.
     */
    public void showLoginView() {
        disposeCurrentView();

        loginView = new LoginView();
        authController = new AuthenticationController(loginView, this);
        loginView.showView();
    }

    /**
     * Displays the registration view and initializes its controller.
     */
    public void showRegistrationView() {
        disposeCurrentView();

        registrationView = new RegistrationView();
        registrationController = new RegistrationController(registrationView, this);
        registrationView.showView();
    }

    /**
     * Displays the main job tracking view after successful login.
     */
    public void showMainView(int user_id) {
        loggedUserID = user_id;
        disposeCurrentView();

        mainView = new MainView();
        mainViewController = new MainViewController(mainView, this);
        mainView.showView();
    }

    /**
     * Displays the view for adding a new job application.
     */
    public void showAddJobApplication() {
        addJAView = new AddJobApplicationView(mainView.getFrame());
        addJAController = new AddJobApplicationController(addJAView, this);
        addJAView.showView();
    }

    /**
     * Displays the view for editing an existing job application.
     */
    public void showEditJobApplication(JobApplication job) {
        editView = new EditJobApplicationView(mainView.getFrame());
        editJAController = new EditJobApplicationController(this, editView, job);
        editView.showView();
    }

    /**
     * Disposes the current active view, if any, and cleans up associated resources.
     */
    private void disposeCurrentView() {
        disposeAddView();
        disposeEditView();
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

    /**
     * Disposes the add job application view and reloads job applications in the main view.
     */
    public void disposeAddView() {
        if (addJAView != null) {
            addJAView.disposeView();
            addJAView = null;
            addJAController = null;
            mainViewController.loadJobApplications();
        }
    }

    /**
     * Disposes the edit job application view and reloads job applications in the main view.
     */
    public void disposeEditView() {
        if (editView != null) {
            editView.disposeView();
            editView = null;
            editJAController = null;
            mainViewController.loadJobApplications();
        }
    }

    /**
     * Displays an error message dialog.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a success message dialog.
     */
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
