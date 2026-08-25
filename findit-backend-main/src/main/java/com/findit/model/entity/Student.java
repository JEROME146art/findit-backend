package com.findit.model.entity;

import jakarta.persistence.*;

/**
 * Student is a User with additional fields (rollNumber, department).
 *
 * OOP Concepts:
 * - Inheritance: extends User (inherits id, email, password, fullName, phone)
 * - Polymorphism: overrides getRole() to return "STUDENT"
 */
@Entity
@DiscriminatorValue("STUDENT")  // In the DB, user_type = "STUDENT" for these rows
public class Student extends User {

    @Column(name = "roll_number", unique = true)
    private String rollNumber;

    @Column(name = "department")
    private String department;

    // ==================== Constructors ====================
    public Student() {
        super(); // Call User's constructor
    }

    public Student(String email, String password, String fullName, String phone,
                   String rollNumber, String department) {
        super(email, password, fullName, phone); // Call parent constructor
        this.rollNumber = rollNumber;
        this.department = department;
    }

    // ==================== Polymorphism: Override abstract method ====================
    @Override
    public String getRole() {
        return "STUDENT";
    }

    // ==================== Getters & Setters ====================
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}