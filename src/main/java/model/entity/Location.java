package com.findit.model.entity;

import jakarta.persistence.Embeddable;

/**
 * Represents a physical location on campus.
 *
 * OOP Concept: COMPOSITION — an Item HAS-A Location (not IS-A).
 * @Embeddable means this class doesn't get its own table.
 * Instead, its fields become columns of whatever entity uses it.
 */
@Embeddable
public class Location {

    private String building;
    private String floor;
    private String campus;

    // ==================== Constructors ====================
    public Location() {
    }

    public Location(String building, String floor, String campus) {
        this.building = building;
        this.floor = floor;
        this.campus = campus;
    }

    // ==================== Business Logic Methods ====================
    /**
     * Check if two locations are in the same building.
     * Used later by the matching algorithm.
     */
    public boolean sameBuilding(Location other) {
        if (other == null || this.building == null) return false;
        return this.building.equalsIgnoreCase(other.building);
    }

    /**
     * Check if two locations are on the same campus.
     */
    public boolean sameCampus(Location other) {
        if (other == null || this.campus == null) return false;
        return this.campus.equalsIgnoreCase(other.campus);
    }

    // ==================== Getters & Setters ====================
    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    @Override
    public String toString() {
        return building + ", Floor " + floor + ", " + campus;
    }
}