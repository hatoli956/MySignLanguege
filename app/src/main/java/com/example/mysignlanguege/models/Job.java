package com.example.mysignlanguege.models;

public class Job {

    protected String id;
    protected String category;
    protected String salary;
    protected String businessId;
    protected String jobTitle;
    protected String email;
    protected String employerName;

    public Job(String businessId, String id, String jobTitle, String category, String salary, String email, String employerName) {
        this.businessId = businessId;
        this.id = id;
        this.jobTitle = jobTitle;
        this.category = category;
        this.salary = salary;
        this.email = email;
        this.employerName = employerName;
    }

    public Job() {
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", salary='" + salary + '\'' +
                ", businessId='" + businessId + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", email='" + email + '\'' +
                ", employerName='" + employerName + '\'' +
                '}';
    }
}
