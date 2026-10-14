package com.preppulse.service;
import com.preppulse.model.StudyTask; import org.junit.jupiter.api.Test; import java.time.*; import java.util.List; import static org.junit.jupiter.api.Assertions.*;
class RescuePlanServiceTest {
 @Test void planNeverExceedsAvailableTime(){SmartPriorityService p=new SmartPriorityService();RescuePlanService s=new RescuePlanService(p);StudyTask t=new StudyTask();t.setId(1L);t.setTitle("Java");t.setSubject("JP");t.setPriority(StudyTask.Priority.HIGH);t.setDueDate(LocalDate.now().plusDays(1));t.setDueTime(LocalTime.of(10,0));t.setEstimatedMinutes(120);var plan=s.createPlan(35,List.of(t),LocalDateTime.now());assertTrue(plan.getPlannedMinutes()<=35);assertTrue(plan.getPlannedMinutes()>0);}
}
