package com.findit.matcher;

import com.findit.model.entity.FoundItem;
import com.findit.model.entity.LostItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MatchEngine uses a MatchStrategy to find the best matches for a lost item.
 *
 * OOP Concepts:
 * - Composition: MatchEngine HAS-A MatchStrategy
 * - Strategy Pattern: strategy can be changed at runtime
 * - Dependency Injection: Spring auto-injects the default strategy
 */
@Component
public class MatchEngine {

    private MatchStrategy strategy;

    /**
     * Constructor Injection: Spring gives us the WeightedMatchStrategy by default.
     */
    public MatchEngine(WeightedMatchStrategy defaultStrategy) {
        this.strategy = defaultStrategy;
    }

    /**
     * Change the matching strategy at runtime (Strategy Pattern flexibility!).
     */
    public void setStrategy(MatchStrategy strategy) {
        this.strategy = strategy;
    }

    public MatchStrategy getStrategy() {
        return strategy;
    }

    /**
     * Find all found items that match the lost item above a threshold.
     * Results are sorted by score (highest first).
     */
    public List<ScoredMatch> findMatches(LostItem lost, List<FoundItem> founds, double threshold) {
        List<ScoredMatch> results = new ArrayList<>();

        for (FoundItem found : founds) {
            // Skip items that are already claimed or resolved
            if (found.isClaimed() || found.isResolved()) continue;

            double score = strategy.score(lost, found);
            if (score >= threshold) {
                results.add(new ScoredMatch(found, score));
            }
        }

        // Sort by score descending (best matches first)
        results.sort(Comparator.comparingDouble(ScoredMatch::score).reversed());

        return results;
    }

    /**
     * Inner record: a matched found item along with its score.
     * Java 17 records = concise immutable data classes.
     */
    public record ScoredMatch(FoundItem foundItem, double score) {}
}