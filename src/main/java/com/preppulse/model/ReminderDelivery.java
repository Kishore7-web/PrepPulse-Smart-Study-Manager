package com.preppulse.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminder_deliveries", uniqueConstraints = @UniqueConstraint(name = "uk_reminder_task_lead_time", columnNames = {"task_id", "lead_minutes", "scheduled_for"}))
public class ReminderDelivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name="task_id", nullable=false) private Long taskId;
    @Column(nullable=false, length=120) private String taskTitle;
    @Column(name="lead_minutes", nullable=false) private int leadMinutes;
    @Column(name="scheduled_for", nullable=false) private LocalDateTime scheduledFor;
    @Column(name="sent_at", nullable=false) private LocalDateTime sentAt;
    @Column(nullable=false, length=20) private String channel = "IN_APP";
    @Column(nullable=false, length=400) private String message;
    @PrePersist void onCreate(){if(sentAt==null)sentAt=LocalDateTime.now();}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getTaskId(){return taskId;} public void setTaskId(Long v){taskId=v;}
    public String getTaskTitle(){return taskTitle;} public void setTaskTitle(String v){taskTitle=v;}
    public int getLeadMinutes(){return leadMinutes;} public void setLeadMinutes(int v){leadMinutes=v;}
    public LocalDateTime getScheduledFor(){return scheduledFor;} public void setScheduledFor(LocalDateTime v){scheduledFor=v;}
    public LocalDateTime getSentAt(){return sentAt;} public void setSentAt(LocalDateTime v){sentAt=v;}
    public String getChannel(){return channel;} public void setChannel(String v){channel=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
}
