package com.findit.model.entity;

import jakarta.persistence.*;

/**
 * Abstract base class for all users in the system.
 * Uses SINGLE_TABLE inheritance strategy - one table for User + all subclasses.
 * The "user_type" column distinguishes between Student, Admin, etc.
 *
 * OOP Concepts Demonstrated:
 * - Abstraction (abstract class, cannot be instantiated directly)
 * - Encapsulation (private fields with getters/setters)
 * - Inheritance (Student and Admin will extend this class)
 * - Polymorphism (getRole() is overridden by subclasses)
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(length = 15)
    private String phone;

    // Abstract method - MUST be implemented by every subclass
    // This is POLYMORPHISM in action
    public abstract String getRole();

    // ==================== Constructors ====================
    public User() {
    }

    public User(String email, String password, String fullName, String phone) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
    }

    // ==================== Getters & Setters (Encapsulation) ====================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // ==================== toString for debugging ====================
    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', role='" + getRole() + "'}";
    }
}