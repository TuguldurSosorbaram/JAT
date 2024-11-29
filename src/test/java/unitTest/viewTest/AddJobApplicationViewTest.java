package unitTest.viewTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import view.AddJobApplicationView;

public class AddJobApplicationViewTest {
    private AddJobApplicationView view;

    @BeforeEach
    public void setUp() {
        view = new AddJobApplicationView(new JFrame());
    }

    @Test
    public void testGetPosition() {
        JTextField positionField = (JTextField) TestUtils.getPrivateField(view, "positionField");
        positionField.setText("Software Engineer");
        assertEquals("Software Engineer", view.getPosition());
    }

    @Test
    public void testGetCompanyName() {
        JTextField companyNameField = (JTextField) TestUtils.getPrivateField(view, "companyNameField");
        companyNameField.setText("TechCorp");
        assertEquals("TechCorp", view.getCompanyName());
    }

    @Test
    public void testGetSalaryApproximation_Valid() {
        JTextField salaryField = (JTextField) TestUtils.getPrivateField(view, "salaryApproxField");
        salaryField.setText("75000");
        assertEquals(75000, view.getSalaryApproximation());
    }

    @Test
    public void testGetSalaryApproximation_Invalid() {
        JTextField salaryField = (JTextField) TestUtils.getPrivateField(view, "salaryApproxField");
        salaryField.setText("invalid");
        assertEquals(0, view.getSalaryApproximation(), "Invalid salary input should return 0");
    }

    @Test
    public void testGetStatus() {
        JComboBox<String> statusComboBox = (JComboBox<String>) TestUtils.getPrivateField(view, "statusComboBox");
        statusComboBox.setSelectedItem("Applied");
        assertEquals("Applied", view.getStatus());
    }

    @Test
    public void testGetDeadline() {
        JSpinner deadlineSpinner = (JSpinner) TestUtils.getPrivateField(view, "deadlineSpinner");
        Date testDate = new Date();
        deadlineSpinner.setValue(testDate);
        assertEquals(new java.sql.Date(testDate.getTime()), view.getDeadline());
    }

    @Test
    public void testGetExcitement() {
        JSpinner excitementSpinner = (JSpinner) TestUtils.getPrivateField(view, "excitementSpinner");
        excitementSpinner.setValue(4);
        assertEquals(4, view.getExcitement());
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
