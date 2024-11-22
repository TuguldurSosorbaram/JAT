package view;

import view.model.JATableModel;
import model.JobApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MainView {
    private JFrame frame;
    private JTable jobTable;
    private JATableModel tableModel;
    private JButton addButton;
    private JButton editButton;
    private JScrollPane scrollPane;

    public MainView() {
        frame = new JFrame("Job Application Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        jobTable = new JTable(); // We'll set the model later
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
    }

    // Set the table model
    public void setJobApplications(List<JobApplication> applications) {
        tableModel = new JATableModel(applications);
        jobTable.setModel(tableModel);
    }

    public int getSelectedRow() {
        return jobTable.getSelectedRow();
    }

    public JobApplication getSelectedJobApplication() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getJobAt(selectedRow);
        }
        return null;
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
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
