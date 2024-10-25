package model;

import java.util.Date;

public class JobApplication {
    private String companyName;
    private String position;
    private Date dateApplied;

    public JobApplication(String companyName, String position, Date dateApplied) {
        this.companyName = companyName;
        this.position = position;
        this.dateApplied = dateApplied;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPosition() {
        return position;
    }

    public Date getDateApplied() {
        return dateApplied;
    }
}