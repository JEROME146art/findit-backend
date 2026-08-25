package com.findit.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for calculating text similarity.
 * Uses Jaccard similarity: |A ∩ B| / |A ∪ B|
 *
 * Example:
 *   Text A = "black iphone cracked screen"
 *   Text B = "black phone screen broken"
 *   Common words: {black, screen} → 2
 *   Total unique words: {black, iphone, cracked, screen, phone, broken} → 6
 *   Similarity = 2/6 = 0.33 (33%)
 */
public class TextSimilarity {

    /**
     * Calculate Jaccard similarity between two strings.
     * @return value between 0.0 (no match) and 1.0 (identical)
     */
    public static double jaccard(String a, String b) {
        if (a == null || b == null) return 0.0;

        Set<String> setA = tokenize(a);
        Set<String> setB = tokenize(b);

        if (setA.isEmpty() && setB.isEmpty()) return 0.0;

        // Intersection: words in BOTH
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        // Union: words in EITHER
        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        return (double) intersection.size() / union.size();
    }

    /**
     * Break a string into a set of meaningful words (ignore short words).
     */
    private static Set<String> tokenize(String text) {
        Set<String> tokens = new HashSet<>();
        // Split on any non-word character, convert to lowercase
        for (String word : text.toLowerCase().split("\\W+")) {
            // Ignore words shorter than 3 characters (like "a", "is", "in")
            if (word.length() > 2) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}