package com.findit.matcher;

import com.findit.model.entity.FoundItem;
import com.findit.model.entity.LostItem;
import com.findit.util.TextSimilarity;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

/**
 * Weighted Match Strategy — the MAIN matching algorithm.
 *
 * Scoring breakdown (total 100 points):
 *   - Category match:       30 points (same category = 30, else 0)
 *   - Description similarity: 30 points (Jaccard × 30)
 *   - Location proximity:   20 points (same building = 20, same campus = 10)
 *   - Date proximity:       20 points (same day = 20, within 3 days = 12, within 7 days = 6)
 *
 * Design Pattern: Strategy Pattern implementation.
 */
@Component
public class WeightedMatchStrategy implements MatchStrategy {

    // Weights (in points, summing to 100)
    private static final double CATEGORY_WEIGHT = 30.0;
    private static final double DESCRIPTION_WEIGHT = 30.0;
    private static final double LOCATION_WEIGHT = 20.0;
    private static final double DATE_WEIGHT = 20.0;

    @Override
    public double score(LostItem lost, FoundItem found) {
        double total = 0.0;

        // 1️⃣ CATEGORY match (30 points)
        total += scoreCategory(lost, found);

        // 2️⃣ DESCRIPTION similarity (30 points)
        total += scoreDescription(lost, found);

        // 3️⃣ LOCATION proximity (20 points)
        total += scoreLocation(lost, found);

        // 4️⃣ DATE proximity (20 points)
        total += scoreDate(lost, found);

        // Cap at 100
        return Math.min(100.0, total);
    }

    /**
     * Category matching: full points if same category, else 0.
     */
    private double scoreCategory(LostItem lost, FoundItem found) {
        if (lost.getCategory() != null
                && lost.getCategory() == found.getCategory()) {
            return CATEGORY_WEIGHT;
        }
        return 0.0;
    }

    /**
     * Description similarity using Jaccard index.
     */
    private double scoreDescription(LostItem lost, FoundItem found) {
        String lostText = safeConcat(lost.getTitle(), lost.getDescription());
        String foundText = safeConcat(found.getTitle(), found.getDescription());
        double similarity = TextSimilarity.jaccard(lostText, foundText);
        return similarity * DESCRIPTION_WEIGHT;
    }

    /**
     * Location scoring: same building = full, same campus = half.
     */
    private double scoreLocation(LostItem lost, FoundItem found) {
        if (lost.getLocation() == null || found.getLocation() == null) {
            return 0.0;
        }
        if (lost.getLocation().sameBuilding(found.getLocation())) {
            return LOCATION_WEIGHT;
        }
        if (lost.getLocation().sameCampus(found.getLocation())) {
            return LOCATION_WEIGHT / 2;
        }
        return 0.0;
    }

    /**
     * Date proximity scoring.
     */
    private double scoreDate(LostItem lost, FoundItem found) {
        if (lost.getIncidentDate() == null || found.getIncidentDate() == null) {
            return 0.0;
        }
        long daysDiff = Math.abs(ChronoUnit.DAYS.between(
                lost.getIncidentDate(), found.getIncidentDate()));

        if (daysDiff == 0) return DATE_WEIGHT;         // Same day
        if (daysDiff <= 3) return DATE_WEIGHT * 0.6;   // Within 3 days
        if (daysDiff <= 7) return DATE_WEIGHT * 0.3;   // Within a week
        return 0.0;
    }

    /**
     * Helper to safely concatenate title and description (avoiding nulls).
     */
    private String safeConcat(String title, String description) {
        String t = title != null ? title : "";
        String d = description != null ? description : "";
        return (t + " " + d).trim();
    }

    @Override
    public String getName() {
        return "WEIGHTED";
    }
}