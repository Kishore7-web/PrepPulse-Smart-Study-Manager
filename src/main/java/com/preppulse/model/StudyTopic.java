package com.preppulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "study_topics")
public class StudyTopic {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(max = 120) @Column(nullable = false, length = 120) private String title;
    @NotBlank @Size(max = 80) @Column(nullable = false, length = 80) private String subject;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12) private Difficulty difficulty = Difficulty.MEDIUM;
    @Column(nullable = false) private LocalDate lastReviewedDate;
    @Column(nullable = false) private LocalDate nextReviewDate;
    @Column(nullable = false) private int intervalDays = 1;
    @Column(nullable = false) private int reviewCount = 0;
    @Column(nullable = false) private boolean archived = false;
    @PrePersist void onCreate() {
        if (difficulty == null) difficulty = Difficulty.MEDIUM;
        if (lastReviewedDate == null) lastReviewedDate = LocalDate.now();
        if (nextReviewDate == null) nextReviewDate = LocalDate.now().plusDays(1);
        if (intervalDays < 1) intervalDays = 1;
    }
    public enum Difficulty { EASY, MEDIUM, HARD }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getTitle(){return title;} public void setTitle(String title){this.title=title;}
    public String getSubject(){return subject;} public void setSubject(String subject){this.subject=subject;}
    public Difficulty getDifficulty(){return difficulty;} public void setDifficulty(Difficulty difficulty){this.difficulty=difficulty;}
    public LocalDate getLastReviewedDate(){return lastReviewedDate;} public void setLastReviewedDate(LocalDate d){lastReviewedDate=d;}
    public LocalDate getNextReviewDate(){return nextReviewDate;} public void setNextReviewDate(LocalDate d){nextReviewDate=d;}
    public int getIntervalDays(){return intervalDays;} public void setIntervalDays(int d){intervalDays=d;}
    public int getReviewCount(){return reviewCount;} public void setReviewCount(int c){reviewCount=c;}
    public boolean isArchived(){return archived;} public void setArchived(boolean v){archived=v;}
}
