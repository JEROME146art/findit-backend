package com.findit.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Represents an item that a user has LOST.
 * Inherits all fields from Item + adds lastSeenDate.
 */
@Entity
@DiscriminatorValue("LOST")
public class LostItem extends Item {

    @Column(name = "last_seen_date")
    private LocalDate lastSeenDate;

    // ==================== Constructors ====================
    public LostItem() {
        super();
    }

    // ==================== Polymorphism ====================
    @Override
    public String getType() {
        return "LOST";
    }

    // ==================== Getters & Setters ====================
    public LocalDate getLastSeenDate() { return lastSeenDate; }
    public void setLastSeenDate(LocalDate lastSeenDate) { this.lastSeenDate = lastSeenDate; }
}