package com.findit.voting.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "voting_townhall_questions")
public class TownhallQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String targetCandidate;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 1000)
    private String questionText;

    @Column(nullable = false, length = 100)
    private String author;

    private Integer upvotes = 1;

    @Column(length = 2000)
    private String answerText;

    private LocalDateTime createdAt = LocalDateTime.now();

    public TownhallQuestion() {}

    public TownhallQuestion(String targetCandidate, String category, String questionText, String author, Integer upvotes, String answerText) {
        this.targetCandidate = targetCandidate;
        this.category = category;
        this.questionText = questionText;
        this.author = author;
        this.upvotes = upvotes;
        this.answerText = answerText;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTargetCandidate() { return targetCandidate; }
    public void setTargetCandidate(String targetCandidate) { this.targetCandidate = targetCandidate; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public Integer getUpvotes() { return upvotes; }
    public void setUpvotes(Integer upvotes) { this.upvotes = upvotes; }

    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
