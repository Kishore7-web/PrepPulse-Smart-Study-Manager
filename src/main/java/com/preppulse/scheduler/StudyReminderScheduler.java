package com.preppulse.scheduler;
import com.preppulse.service.ReminderService; import org.springframework.scheduling.annotation.Scheduled; import org.springframework.stereotype.Component;
@Component public class StudyReminderScheduler {
 private final ReminderService reminders; public StudyReminderScheduler(ReminderService r){reminders=r;}
 // The app must be running for scheduled reminders to be processed.
 @Scheduled(fixedDelay=60000, initialDelay=15000) public void checkReminders(){reminders.processDueReminders();}
}
