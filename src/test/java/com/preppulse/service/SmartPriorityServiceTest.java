package com.preppulse.service;
import com.preppulse.model.StudyTask; import org.junit.jupiter.api.Test; import java.time.*; import java.util.List; import static org.junit.jupiter.api.Assertions.*;
class SmartPriorityServiceTest {
 @Test void overdueHighPriorityRanksAheadOfFutureLowPriority(){SmartPriorityService s=new SmartPriorityService();StudyTask a=task("Overdue",StudyTask.Priority.HIGH,LocalDate.now().minusDays(1));StudyTask b=task("Future",StudyTask.Priority.LOW,LocalDate.now().plusDays(5));var ranked=s.rank(List.of(b,a),LocalDateTime.now());assertEquals("Overdue",ranked.get(0).getTask().getTitle());}
 private StudyTask task(String title,StudyTask.Priority priority,LocalDate date){StudyTask t=new StudyTask();t.setTitle(title);t.setSubject("Test");t.setPriority(priority);t.setDueDate(date);t.setDueTime(LocalTime.of(10,0));t.setEstimatedMinutes(30);return t;}
}
