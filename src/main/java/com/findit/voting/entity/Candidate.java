package com.findit.voting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "voting_candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 300)
    private String slogan;

    @Column(length = 1500)
    private String bio;

    @Column(length = 1500)
    private String academicPolicy;

    @Column(length = 1500)
    private String sustainabilityPolicy;

    @Column(length = 1500)
    private String housingPolicy;

    private Integer voteCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "election_id", nullable = false)
    @JsonBackReference
    private Election election;

    public Candidate() {}

    public Candidate(String name, String slogan, String bio, String academicPolicy, String sustainabilityPolicy, String housingPolicy, Integer voteCount) {
        this.name = name;
        this.slogan = slogan;
        this.bio = bio;
        this.academicPolicy = academicPolicy;
        this.sustainabilityPolicy = sustainabilityPolicy;
        this.housingPolicy = housingPolicy;
        this.voteCount = voteCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getAcademicPolicy() { return academicPolicy; }
    public void setAcademicPolicy(String academicPolicy) { this.academicPolicy = academicPolicy; }

    public String getSustainabilityPolicy() { return sustainabilityPolicy; }
    public void setSustainabilityPolicy(String sustainabilityPolicy) { this.sustainabilityPolicy = sustainabilityPolicy; }

    public String getHousingPolicy() { return housingPolicy; }
    public void setHousingPolicy(String housingPolicy) { this.housingPolicy = housingPolicy; }

    public Integer getVoteCount() { return voteCount; }
    public void setVoteCount(Integer voteCount) { this.voteCount = voteCount; }

    public Election getElection() { return election; }
    public void setElection(Election election) { this.election = election; }
}
