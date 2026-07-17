package com.findit.voting.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voting_elections")
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String electionCode; // e.g. "SGA-2026", "ENG-2026", "REF-104"

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 50)
    private String category; // e.g. "SGA", "SENATE", "REFERENDUM"

    @Column(nullable = false, length = 50)
    private String votingStrategy; // e.g. "RCV", "PLURALITY", "REFERENDUM"

    @Column(length = 200)
    private String eligibilityRule; // e.g. "All Enrolled Students"

    @Column(length = 1000)
    private String propositionText;

    @Column(nullable = false)
    private String status = "Live";

    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer totalVotes = 0;

    @OneToMany(mappedBy = "election", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Candidate> candidates = new ArrayList<>();

    public Election() {}

    public Election(String electionCode, String title, String category, String votingStrategy, String eligibilityRule, String propositionText) {
        this.electionCode = electionCode;
        this.title = title;
        this.category = category;
        this.votingStrategy = votingStrategy;
        this.eligibilityRule = eligibilityRule;
        this.propositionText = propositionText;
        this.status = "Live";
        this.totalVotes = 0;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getElectionCode() { return electionCode; }
    public void setElectionCode(String electionCode) { this.electionCode = electionCode; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getVotingStrategy() { return votingStrategy; }
    public void setVotingStrategy(String votingStrategy) { this.votingStrategy = votingStrategy; }

    public String getEligibilityRule() { return eligibilityRule; }
    public void setEligibilityRule(String eligibilityRule) { this.eligibilityRule = eligibilityRule; }

    public String getPropositionText() { return propositionText; }
    public void setPropositionText(String propositionText) { this.propositionText = propositionText; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getTotalVotes() { return totalVotes; }
    public void setTotalVotes(Integer totalVotes) { this.totalVotes = totalVotes; }

    public List<Candidate> getCandidates() { return candidates; }
    public void setCandidates(List<Candidate> candidates) { this.candidates = candidates; }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        candidate.setElection(this);
    }
}
