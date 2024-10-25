package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import model.JobApplication;

public class JobApplicationView {
    private JFrame frame;
    private JTextArea jobListTextArea;
    private JButton addButton;

    public JobApplicationView() {
        frame = new JFrame("Job Application Tracker");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        jobListTextArea = new JTextArea();
        panel.add(new JScrollPane(jobListTextArea), BorderLayout.CENTER);

        addButton = new JButton("Add Job Application");
        panel.add(addButton, BorderLayout.SOUTH);
        
        frame.add(panel);
        frame.setVisible(true);
    }

    public void displayJobApplications(List<JobApplication> applications) {
        jobListTextArea.setText("");  // Clear text area
        for (JobApplication job : applications) {
            jobListTextArea.append(job.getCompanyName() + " - " + job.getPosition() + " (" + job.getDateApplied() + ")\n");
        }
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void showView() {
        frame.setVisible(true);
    }
    public void hideView(){
        frame.setVisible(false);
    }
    public void disposeView() {
        frame.dispose();
    }
}
