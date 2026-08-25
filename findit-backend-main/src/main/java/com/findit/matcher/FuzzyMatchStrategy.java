package com.findit.matcher;

import com.findit.model.entity.FoundItem;
import com.findit.model.entity.LostItem;
import com.findit.util.TextSimilarity;
import org.springframework.stereotype.Component;

/**
 * Fuzzy Match Strategy — a simpler alternative that ONLY uses text similarity.
 * Useful when category/location/date are unreliable.
 *
 * Demonstrates POLYMORPHISM: same interface, different behavior.
 */
@Component
public class FuzzyMatchStrategy implements MatchStrategy {

    @Override
    public double score(LostItem lost, FoundItem found) {
        String lostText = (lost.getTitle() + " " + lost.getDescription());
        String foundText = (found.getTitle() + " " + found.getDescription());
        double similarity = TextSimilarity.jaccard(lostText, foundText);
        return similarity * 100.0;  // Scale to 0-100
    }

    @Override
    public String getName() {
        return "FUZZY";
    }
}