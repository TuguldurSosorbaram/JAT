package unitTest.viewTest;

import model.JobApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import view.MainView;
import view.model.JATableModel;

public class MainViewTest {
    private MainView mainView;

    @BeforeEach
    public void setUp() {
        mainView = new MainView();
        mainView.hideView(); // Ensure the UI doesn't appear during tests
    }

    @Test
    public void testGetSelectedRow_NoSelection() {
        assertEquals(-1, mainView.getSelectedRow(), "Selected row should be -1 when no row is selected");
    }

    @Test
    public void testGetSelectedJobApplication_NoSelection() {
        assertNull(mainView.getSelectedJobApplication(), "Selected job application should be null when no row is selected");
    }

    @Test
    public void testSetJobApplications() {
        List<JobApplication> jobApplications = new ArrayList<>();
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        JobApplication job1 = new JobApplication("Developer", "Company A", 60000, "New York", "Applied",
                                date,date,date,3,1);
        JobApplication job2 = new JobApplication("Tester", "Company B", 50000, "Boston", "Interviewing",
                        date,date,date,3,1);
        jobApplications.add(job1);
        jobApplications.add(job2);

        mainView.setJobApplications(jobApplications);

        JATableModel tableModel = (JATableModel) TestUtils.getPrivateField(mainView, "tableModel");

        assertEquals(2, tableModel.getRowCount(), "Table should have 2 rows");
        assertEquals(job1, tableModel.getJobAt(0), "First job should match the first application");
        assertEquals(job2, tableModel.getJobAt(1), "Second job should match the second application");
    }

    @Test
    public void testAddAddButtonListener() {
        ActionListener mockListener = mock(ActionListener.class);
        mainView.addAddButtonListener(mockListener);

        JButton addButton = (JButton) TestUtils.getPrivateField(mainView, "addButton");
        for (ActionListener listener : addButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Add"));
        }

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testAddEditButtonListener() {
        ActionListener mockListener = mock(ActionListener.class);
        mainView.addEditButtonListener(mockListener);

        JButton editButton = (JButton) TestUtils.getPrivateField(mainView, "editButton");
        for (ActionListener listener : editButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Edit"));
        }

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testAddLogOutButtonListener() {
        ActionListener mockListener = mock(ActionListener.class);
        mainView.addLogOutButtonListener(mockListener);

        JButton logOutButton = (JButton) TestUtils.getPrivateField(mainView, "logOutButton");
        for (ActionListener listener : logOutButton.getActionListeners()) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "LogOut"));
        }

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testAddTableEditListenerAndTrigger() {
        ActionListener mockListener = mock(ActionListener.class);
        mainView.addTableEditListener(mockListener);

        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        JobApplication job = new JobApplication("Developer", "Company A", 60000, "New York", "Applied",
                                    date,date,date,3,1);
        mainView.triggerTableEditListener(job);

        verify(mockListener, times(1)).actionPerformed(any(ActionEvent.class));
    }


    @Test
    public void testViewVisibilityControls() {
        JFrame frame = mainView.getFrame();

        mainView.hideView();
        assertFalse(frame.isVisible(), "Frame should not be visible after calling hideView()");

        mainView.disposeView();
        assertFalse(frame.isDisplayable(), "Frame should no longer be displayable after calling disposeView()");
    }
}
