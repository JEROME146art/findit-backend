package com.findit.model.interfaces;

/**
 * Interface for anything that can be scored for similarity.
 * OOP: Abstraction — different classes can implement this differently.
 */
public interface Scorable {
    double calculateScore(Object other);
}