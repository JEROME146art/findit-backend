package com.findit.matcher;

import com.findit.model.entity.FoundItem;
import com.findit.model.entity.LostItem;

/**
 * Strategy Pattern: Defines the contract for any matching algorithm.
 *
 * OOP Concept: Abstraction via interface.
 * Different strategies (Weighted, Fuzzy, etc.) can be swapped at runtime
 * without changing the code that uses them.
 */
public interface MatchStrategy {

    /**
     * Calculate how likely a lost item matches a found item.
     * @return a score from 0 (no match) to 100 (perfect match)
     */
    double score(LostItem lost, FoundItem found);

    /**
     * Return the name of this strategy (for logging/UI).
     */
    String getName();
}