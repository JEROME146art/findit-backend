package com.findit.model.dto;

/**
 * DTO returned when we find a match.
 * Contains the found item's details + the match score.
 */
public class MatchResponse {

    private Long lostItemId;
    private Long foundItemId;
    private String foundTitle;
    private String foundDescription;
    private String foundLocation;
    private double score;
    private String matchLevel;  // "STRONG", "MODERATE", "WEAK"

    // ==================== Constructors ====================
    public MatchResponse() {
    }

    public MatchResponse(Long lostItemId, Long foundItemId, String foundTitle,
                         String foundDescription, String foundLocation, double score) {
        this.lostItemId = lostItemId;
        this.foundItemId = foundItemId;
        this.foundTitle = foundTitle;
        this.foundDescription = foundDescription;
        this.foundLocation = foundLocation;
        this.score = score;
        this.matchLevel = calculateMatchLevel(score);
    }

    private String calculateMatchLevel(double score) {
        if (score >= 75) return "STRONG";
        if (score >= 50) return "MODERATE";
        return "WEAK";
    }

    // ==================== Getters & Setters ====================
    public Long getLostItemId() { return lostItemId; }
    public void setLostItemId(Long lostItemId) { this.lostItemId = lostItemId; }

    public Long getFoundItemId() { return foundItemId; }
    public void setFoundItemId(Long foundItemId) { this.foundItemId = foundItemId; }

    public String getFoundTitle() { return foundTitle; }
    public void setFoundTitle(String foundTitle) { this.foundTitle = foundTitle; }

    public String getFoundDescription() { return foundDescription; }
    public void setFoundDescription(String foundDescription) { this.foundDescription = foundDescription; }

    public String getFoundLocation() { return foundLocation; }
    public void setFoundLocation(String foundLocation) { this.foundLocation = foundLocation; }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; this.matchLevel = calculateMatchLevel(score); }

    public String getMatchLevel() { return matchLevel; }
    public void setMatchLevel(String matchLevel) { this.matchLevel = matchLevel; }
}