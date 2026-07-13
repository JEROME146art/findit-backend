package com.findit.model.entity;

import jakarta.persistence.*;

/**
 * Represents an item that has been FOUND by someone.
 * Inherits from Item + adds currentLocation and claimed status.
 */
@Entity
@DiscriminatorValue("FOUND")
public class FoundItem extends Item {

    @Column(name = "current_location")
    private String currentLocation;  // e.g., "Security Office"

    @Column
    private Boolean claimed = false;  // Changed to Boolean (nullable object) instead of boolean (primitive)

    // ==================== Constructors ====================
    public FoundItem() {
        super();
    }

    // ==================== Polymorphism ====================
    @Override
    public String getType() {
        return "FOUND";
    }

    // ==================== Getters & Setters ====================
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public boolean isClaimed() { return claimed; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }
}