package com.findit.model.entity;

/**
 * Enum representing categories of lost/found items.
 * Enums are a special form of class in Java — great for fixed sets of values.
 */
public enum Category {
    ELECTRONICS("Electronics"),
    BOOKS("Books & Notes"),
    CLOTHING("Clothing"),
    ACCESSORIES("Accessories"),
    ID_CARDS("ID Cards"),
    KEYS("Keys"),
    BAGS("Bags & Backpacks"),
    DOCUMENTS("Documents"),
    STATIONERY("Stationery"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}