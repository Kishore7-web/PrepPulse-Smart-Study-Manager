package com.preppulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "study_tasks")
public class StudyTask {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Task title is required")
    @Size(max = 120, message = "Title must be 120 characters or fewer")
    @Column(nullable = false, length = 120)
    private String title;

    @NotBlank(message = "Subject is required")
    @Size(max = 80) @Column(nullable = false, length = 80)
    private String subject;

    @NotNull @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12)
    private Priority priority = Priority.MEDIUM;

    @NotNull(message = "Please choose a due date") @Column(nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "Please choose a due time") @Column(nullable = false)
    private LocalTime dueTime = LocalTime.of(10, 0);

    @Min(value = 5, message = "Study time must be at least 5 minutes")
    @Max(value = 720, message = "Study time cannot exceed 720 minutes")
    @Column(nullable = false)
    private int estimatedMinutes = 30;

    @Size(max = 1000, message = "Notes must be 1000 characters or fewer") @Column(length = 1000) private String notes;
    @Column(nullable = false) private boolean completed = false;
    @Column(nullable = false) private boolean reminderEnabled = true;
    @Column(nullable = false, length = 40) private String reminderIntervals = "1440,60,15";
    @Column(nullable = false) private int postponedCount = 0;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    private LocalDateTime lastPostponedAt;

    @PrePersist void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (priority == null) priority = Priority.MEDIUM;
        if (dueTime == null) dueTime = LocalTime.of(10, 0);
        if (reminderIntervals == null || reminderIntervals.isBlank()) reminderIntervals = "1440,60,15";
    }

    public LocalDateTime getDueAt() { return LocalDateTime.of(dueDate, dueTime == null ? LocalTime.of(10, 0) : dueTime); }
    public enum Priority { LOW, MEDIUM, HIGH }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalTime getDueTime() { return dueTime; }
    public void setDueTime(LocalTime dueTime) { this.dueTime = dueTime; }
    public int getEstimatedMinutes() { return estimatedMinutes; }
    public void setEstimatedMinutes(int estimatedMinutes) { this.estimatedMinutes = estimatedMinutes; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public boolean isReminderEnabled() { return reminderEnabled; }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }
    public String getReminderIntervals() { return reminderIntervals; }
    public void setReminderIntervals(String reminderIntervals) { this.reminderIntervals = reminderIntervals; }
    public int getPostponedCount() { return postponedCount; }
    public void setPostponedCount(int postponedCount) { this.postponedCount = postponedCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastPostponedAt() { return lastPostponedAt; }
    public void setLastPostponedAt(LocalDateTime lastPostponedAt) { this.lastPostponedAt = lastPostponedAt; }
}
