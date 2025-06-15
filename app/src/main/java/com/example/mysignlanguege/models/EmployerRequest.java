package com.example.mysignlanguege.models;

public  class EmployerRequest {
    public String userId;
    public String fullName;
    public String reason;

    public EmployerRequest() {}

    public EmployerRequest(String userId, String fullName, String reason) {
        this.userId = userId;
        this.fullName = fullName;
        this.reason = reason;
    }
    
}
