package model;

import java.sql.Date;

public class JobApplication {
    private int id;
    private String position;
    private String companyName;
    private int salaryApproximation;  // Approximated salary as a numeric value
    private String location;
    private String status;  // Can be represented as an Enum if you prefer a specific set of status values
    private Date dateSaved;
    private Date deadline;
    private Date dateApplied;
    private Date followUpDate;
    private int excitement;  // Should be a value between 1 and 5

    // Constructor
    public JobApplication(){
        this.position = "";
        this.companyName = "";
        this.salaryApproximation = 0;
        this.location = "";
        this.status = "";
        this.dateSaved = null;
        this.deadline = null;
        this.dateApplied = null;
        this.followUpDate = null;
        setExcitement(0);  // Ensures excitement is within range
    }
    public JobApplication(String position, String companyName, int salaryApproximation, String location,
                          String status, Date dateSaved, Date deadline, Date dateApplied, Date followUpDate,
                          int excitement) {
        this.position = position;
        this.companyName = companyName;
        this.salaryApproximation = salaryApproximation;
        this.location = location;
        this.status = status;
        this.dateSaved = dateSaved;
        this.deadline = deadline;
        this.dateApplied = dateApplied;
        this.followUpDate = followUpDate;
        setExcitement(excitement);  // Ensures excitement is within range
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getSalaryApproximation() {
        return salaryApproximation;
    }

    public void setSalaryApproximation(int salaryApproximation) {
        this.salaryApproximation = salaryApproximation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateSaved() {
        return dateSaved;
    }

    public void setDateSaved(Date dateSaved) {
        this.dateSaved = dateSaved;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public int getExcitement() {
        return excitement;
    }

    public void setExcitement(int excitement) {
        // Ensuring excitement rating is between 0 and 5
        if (excitement < 0 || excitement > 5) {
            throw new IllegalArgumentException("Excitement rating must be between 0 and 5.");
        }
        this.excitement = excitement;
    }
}
