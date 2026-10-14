package com.preppulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "focus_sessions")
public class FocusSession {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotNull @Column(nullable = false) private LocalDateTime startedAt;
    @NotNull @Column(nullable = false) private LocalDateTime endedAt;
    @Min(1) @Max(720) @Column(nullable = false) private int durationMinutes;
    @Column(nullable = false, length = 12) private String sessionType = "FOCUS";
    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "task_id") private StudyTask task;
    @Column(length = 200) private String note;
    @PrePersist void onCreate(){ if(startedAt==null) startedAt=LocalDateTime.now().minusMinutes(durationMinutes); if(endedAt==null) endedAt=LocalDateTime.now(); if(sessionType==null) sessionType="FOCUS"; }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public LocalDateTime getStartedAt(){return startedAt;} public void setStartedAt(LocalDateTime v){startedAt=v;}
    public LocalDateTime getEndedAt(){return endedAt;} public void setEndedAt(LocalDateTime v){endedAt=v;}
    public int getDurationMinutes(){return durationMinutes;} public void setDurationMinutes(int v){durationMinutes=v;}
    public String getSessionType(){return sessionType;} public void setSessionType(String v){sessionType=v;}
    public StudyTask getTask(){return task;} public void setTask(StudyTask v){task=v;}
    public String getNote(){return note;} public void setNote(String v){note=v;}
}
