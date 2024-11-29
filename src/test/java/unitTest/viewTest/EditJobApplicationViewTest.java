package unitTest.viewTest;

import model.JobApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import view.EditJobApplicationView;

public class EditJobApplicationViewTest {
    private EditJobApplicationView view;

    @BeforeEach
    public void setUp() {
        view = new EditJobApplicationView(new JFrame());
        view.hideView(); 
    }

    @Test
    public void testSetJobApplication() {
        JobApplication job = new JobApplication(
                "Software Engineer",
                "TechCorp",
                80000,
                "New York",
                "Applied",
                Date.valueOf("2024-12-31"),
                Date.valueOf("2024-11-29"),
                Date.valueOf("2025-01-15"),
                4,
                -1
        );

        view.setJobApplication(job);

        JTextField positionField = (JTextField) TestUtils.getPrivateField(view, "positionField");
        JTextField companyNameField = (JTextField) TestUtils.getPrivateField(view, "companyNameField");
        JTextField salaryField = (JTextField) TestUtils.getPrivateField(view, "salaryApproxField");
        JTextField locationField = (JTextField) TestUtils.getPrivateField(view, "locationField");
        JComboBox<String> statusComboBox = (JComboBox<String>) TestUtils.getPrivateField(view, "statusComboBox");
        JSpinner deadlineSpinner = (JSpinner) TestUtils.getPrivateField(view, "deadlineSpinner");
        JSpinner dateAppliedSpinner = (JSpinner) TestUtils.getPrivateField(view, "dateAppliedSpinner");
        JSpinner followUpSpinner = (JSpinner) TestUtils.getPrivateField(view, "followUpDateSpinner");
        JSpinner excitementSpinner = (JSpinner) TestUtils.getPrivateField(view, "excitementSpinner");

        assertEquals("Software Engineer", positionField.getText());
        assertEquals("TechCorp", companyNameField.getText());
        assertEquals("80000", salaryField.getText());
        assertEquals("New York", locationField.getText());
        assertEquals("Applied", statusComboBox.getSelectedItem());
        assertEquals(Date.valueOf("2024-12-31"), deadlineSpinner.getValue());
        assertEquals(Date.valueOf("2024-11-29"), dateAppliedSpinner.getValue());
        assertEquals(Date.valueOf("2025-01-15"), followUpSpinner.getValue());
        assertEquals(4, excitementSpinner.getValue());
    }

    @Test
    public void testGetUpdatedJobApplication() {
        JTextField positionField = (JTextField) TestUtils.getPrivateField(view, "positionField");
        JTextField companyNameField = (JTextField) TestUtils.getPrivateField(view, "companyNameField");
        JTextField salaryField = (JTextField) TestUtils.getPrivateField(view, "salaryApproxField");
        JTextField locationField = (JTextField) TestUtils.getPrivateField(view, "locationField");
        JComboBox<String> statusComboBox = (JComboBox<String>) TestUtils.getPrivateField(view, "statusComboBox");
        JSpinner deadlineSpinner = (JSpinner) TestUtils.getPrivateField(view, "deadlineSpinner");
        JSpinner dateAppliedSpinner = (JSpinner) TestUtils.getPrivateField(view, "dateAppliedSpinner");
        JSpinner followUpSpinner = (JSpinner) TestUtils.getPrivateField(view, "followUpDateSpinner");
        JSpinner excitementSpinner = (JSpinner) TestUtils.getPrivateField(view, "excitementSpinner");

        positionField.setText("Manager");
        companyNameField.setText("ExampleCorp");
        salaryField.setText("70000");
        locationField.setText("Boston");
        statusComboBox.setSelectedItem("Interviewing");
        deadlineSpinner.setValue(Date.valueOf("2024-12-01"));
        dateAppliedSpinner.setValue(Date.valueOf("2024-11-10"));
        followUpSpinner.setValue(Date.valueOf("2024-12-05"));
        excitementSpinner.setValue(5);

        JobApplication updatedJob = view.getUpdatedJobApplication();

        assertEquals("Manager", updatedJob.getPosition());
        assertEquals("ExampleCorp", updatedJob.getCompanyName());
        assertEquals(70000, updatedJob.getSalaryApproximation());
        assertEquals("Boston", updatedJob.getLocation());
        assertEquals("Interviewing", updatedJob.getStatus());
        assertEquals(Date.valueOf("2024-12-01"), updatedJob.getDeadline());
        assertEquals(Date.valueOf("2024-11-10"), updatedJob.getDateApplied());
        assertEquals(Date.valueOf("2024-12-05"), updatedJob.getFollowUpDate());
        assertEquals(5, updatedJob.getExcitement());
    }

    @Test
    public void testAddSaveButtonListener() {
        ActionListener mockListener = mock(ActionListener.class);
        view.addSaveButtonListener(mockListener);

        JButton saveButton = (JButton) TestUtils.getPrivateField(view, "saveButton");
        for (ActionListener listener : saveButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Save"));
        }

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testAddCancelButtonListener() {
        ActionListener mockListener = mock(ActionListener.class);
        view.addCancelButtonListener(mockListener);

        JButton cancelButton = (JButton) TestUtils.getPrivateField(view, "cancelButton");
        for (ActionListener listener : cancelButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Cancel"));
        }

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testViewLifecycleControls() {
        JDialog dialog = (JDialog) TestUtils.getPrivateField(view, "dialog");
        
        view.hideView();
        assertFalse(dialog.isVisible(), "Dialog should not be visible after calling hideView()");

        view.disposeView();
        assertFalse(dialog.isDisplayable(), "Dialog should no longer be displayable after calling disposeView()");
    }

}

