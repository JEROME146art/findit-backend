package com.findit.model.entity;

import jakarta.persistence.*;

/**
 * Admin is a User with employeeId field.
 * Demonstrates that multiple classes can inherit from the same parent.
 */
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    // ==================== Constructors ====================
    public Admin() {
        super();
    }

    public Admin(String email, String password, String fullName, String phone,
                 String employeeId) {
        super(email, password, fullName, phone);
        this.employeeId = employeeId;
    }

    // ==================== Polymorphism ====================
    @Override
    public String getRole() {
        return "ADMIN";
    }

    // ==================== Getters & Setters ====================
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
}