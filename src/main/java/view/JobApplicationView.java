package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import model.JobApplication;

public class JobApplicationView {
    private JFrame frame;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JScrollPane scrollPane;

    public JobApplicationView() {
        frame = new JFrame("Job Application Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Position", "Company Name", "Salary Approximation", "Location", "Status", "Date Saved", "Deadline", "Date Applied", "Follow-Up", "Excitement"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only allow editing of 'excitement' and 'status'
                return column == 4 || column == 9;
            }
        };

        jobTable = new JTable(tableModel);
        jobTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jobTable.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(jobTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Add Job Application");
        editButton = new JButton("Edit Selected Application");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // Listen for changes in the editable cells and update the database accordingly
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    String updatedValue = (String) tableModel.getValueAt(row, column);
                    JobApplication updatedJob = getJobApplicationFromRow(row);

                    // Update database through controller (to be added in JobApplicationController)
                    // Assume JobApplicationController.updateJobApplicationField(jobId, fieldName, updatedValue);
                    // JobApplicationController.updateJobApplicationField(updatedJob.getId(), columnNames[column], updatedValue);
                }
            }
        });
    }

    // Display job applications in JTable
    public void displayJobApplications(List<JobApplication> applications) {
        tableModel.setRowCount(0);  // Clear table
        for (JobApplication job : applications) {
            Object[] rowData = new Object[]{
                    job.getPosition(),
                    job.getCompanyName(),
                    job.getSalaryApproximation(),
                    job.getLocation(),
                    job.getStatus(),
                    job.getDateSaved(),
                    job.getDeadline(),
                    job.getDateApplied(),
                    job.getFollowUpDate(),
                    job.getExcitement()
            };
            tableModel.addRow(rowData);
        }
    }

    // Helper method to get JobApplication object from row index
    public JobApplication getJobApplicationFromRow(int row) {
        JobApplication job = new JobApplication();
        job.setPosition((String) tableModel.getValueAt(row, 0));
        job.setCompanyName((String) tableModel.getValueAt(row, 1));
        job.setSalaryApproximation((tableModel.getValueAt(row, 2) instanceof Double) ? (Double) tableModel.getValueAt(row, 2) : null);
        job.setLocation((String) tableModel.getValueAt(row, 3));
        job.setStatus((String) tableModel.getValueAt(row, 4));
        job.setDateSaved((java.sql.Date) tableModel.getValueAt(row, 5));
        job.setDeadline((java.sql.Date) tableModel.getValueAt(row, 6));
        job.setDateApplied((java.sql.Date) tableModel.getValueAt(row, 7));
        job.setFollowUpDate((java.sql.Date) tableModel.getValueAt(row, 8));
        job.setExcitement((tableModel.getValueAt(row, 9) instanceof Integer) ? (Integer) tableModel.getValueAt(row, 9) : null);
        return job;
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }

    public int getSelectedRow() {
        return jobTable.getSelectedRow();
    }

    public void showView() {
        frame.setVisible(true);
    }

    public void hideView() {
        frame.setVisible(false);
    }

    public void disposeView() {
        frame.dispose();
    }
    public JFrame getFrame(){
        return this.frame;
    }
}
