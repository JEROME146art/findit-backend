package com.findit.model.dto;

public class UserProfileResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String rollNumber;
    private String department;
    private String role;

    public UserProfileResponse() {}

    public UserProfileResponse(Long id, String email, String fullName, String phone, String rollNumber, String department, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.rollNumber = rollNumber;
        this.department = department;
        this.role = role;
    }

    // ==================== Getters & Setters ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
