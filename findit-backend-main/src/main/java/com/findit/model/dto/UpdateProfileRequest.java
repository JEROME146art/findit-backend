package com.findit.model.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phone;
    private String rollNumber;
    private String department;

    public UpdateProfileRequest() {}

    public UpdateProfileRequest(String fullName, String phone, String rollNumber, String department) {
        this.fullName = fullName;
        this.phone = phone;
        this.rollNumber = rollNumber;
        this.department = department;
    }

    // ==================== Getters & Setters ====================
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
