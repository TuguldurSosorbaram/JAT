package view.model;

import model.JobApplication;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class JATableModel extends AbstractTableModel {
    private final String[] columnNames = {
            "Position", "Company Name", "Salary Approximation", "Location",
            "Status", "Date Saved", "Deadline", "Date Applied", "Follow-Up", "Excitement"
    };

    private final List<JobApplication> jobApplications;

    public JATableModel(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    @Override
    public int getRowCount() {
        return jobApplications.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        JobApplication job = jobApplications.get(rowIndex);
        switch (columnIndex) {
            case 0: return job.getPosition();
            case 1: return job.getCompanyName();
            case 2: return job.getSalaryApproximation();
            case 3: return job.getLocation();
            case 4: return job.getStatus();
            case 5: return job.getDateSaved();
            case 6: return job.getDeadline();
            case 7: return job.getDateApplied();
            case 8: return job.getFollowUpDate();
            case 9: return job.getExcitement();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Allow editing of 'status' and 'excitement' only
        return columnIndex == 4 || columnIndex == 9;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        JobApplication job = jobApplications.get(rowIndex);

        if (columnIndex == 4) {
            job.setStatus((String) aValue); // Assuming status is a String; adapt if it's an enum
        } else if (columnIndex == 9) {
            job.setExcitement(Integer.parseInt(aValue.toString()));
        }

        // Notify the table of the data change
        fireTableCellUpdated(rowIndex, columnIndex);

        // (Optional) Update the database immediately for editable fields
        // DatabaseHelper.updateJobApplicationField(job.getId(), columnNames[columnIndex], aValue);
    }

    public JobApplication getJobAt(int rowIndex) {
        return jobApplications.get(rowIndex);
    }
}
