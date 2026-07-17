package com.findit.voting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voting_ballots")
public class BallotRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String receiptToken;

    @Column(nullable = false, length = 100)
    private String electionCode;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 500)
    private String choiceSummary;

    private LocalDateTime castTimestamp = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "✅ Validated & Sealed";

    public BallotRecord() {}

    public BallotRecord(String receiptToken, String electionCode, String category, String choiceSummary) {
        this.receiptToken = receiptToken;
        this.electionCode = electionCode;
        this.category = category;
        this.choiceSummary = choiceSummary;
        this.castTimestamp = LocalDateTime.now();
        this.status = "✅ Validated & Sealed";
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReceiptToken() { return receiptToken; }
    public void setReceiptToken(String receiptToken) { this.receiptToken = receiptToken; }

    public String getElectionCode() { return electionCode; }
    public void setElectionCode(String electionCode) { this.electionCode = electionCode; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getChoiceSummary() { return choiceSummary; }
    public void setChoiceSummary(String choiceSummary) { this.choiceSummary = choiceSummary; }

    public LocalDateTime getCastTimestamp() { return castTimestamp; }
    public void setCastTimestamp(LocalDateTime castTimestamp) { this.castTimestamp = castTimestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
