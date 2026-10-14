package com.preppulse.service;

import com.preppulse.model.ReminderDelivery;
import com.preppulse.model.StudySettings;
import com.preppulse.model.StudyTask;
import com.preppulse.repository.ReminderDeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {
    private static final Logger log = LoggerFactory.getLogger(ReminderService.class);
    private static final List<Integer> DEFAULT_OFFSETS = List.of(1440, 60, 15);

    private final ReminderDeliveryRepository deliveryRepo;
    private final StudyTaskService taskService;
    private final StudySettingsService settingsService;
    private final ObjectProvider<JavaMailSender> mailProvider;
    private final Environment env;

    public ReminderService(ReminderDeliveryRepository deliveryRepo,
                           StudyTaskService taskService,
                           StudySettingsService settingsService,
                           ObjectProvider<JavaMailSender> mailProvider,
                           Environment env) {
        this.deliveryRepo = deliveryRepo;
        this.taskService = taskService;
        this.settingsService = settingsService;
        this.mailProvider = mailProvider;
        this.env = env;
    }

    /** Runs periodically while the application is running. Each scheduled alert is recorded once. */
    @Transactional
    public void processDueReminders() {
        StudySettings settings = settingsService.getOrCreate();
        if (!settings.isRemindersEnabled()) return;

        LocalDateTime now = LocalDateTime.now();
        for (StudyTask task : taskService.getPendingTasks()) {
            if (!task.isReminderEnabled() || task.getDueDate() == null) continue;

            LocalDateTime dueAt = task.getDueAt();
            if (!dueAt.isAfter(now)) {
                // One overdue alert per due-date revision, avoiding repeated notifications every minute.
                if (!deliveryRepo.existsByTaskIdAndLeadMinutesAndScheduledFor(task.getId(), 0, dueAt)) {
                    deliver(task, 0, dueAt, now, overdueMessage(task));
                }
                continue;
            }

            List<Integer> triggered = parseOffsets(task.getReminderIntervals()).stream()
                    .filter(minutes -> now.isAfter(dueAt.minusMinutes(minutes)))
                    .filter(minutes -> !deliveryRepo.existsByTaskIdAndLeadMinutesAndScheduledFor(
                            task.getId(), minutes, dueAt.minusMinutes(minutes)))
                    .sorted()
                    .toList();

            // Deliver one at a time. Subsequent configured alerts are picked up by the next poll.
            if (!triggered.isEmpty()) {
                int leadMinutes = triggered.get(0);
                LocalDateTime scheduledFor = dueAt.minusMinutes(leadMinutes);
                deliver(task, leadMinutes, scheduledFor, now, upcomingMessage(task, leadMinutes));
            }
        }
    }

    private void deliver(StudyTask task, int leadMinutes, LocalDateTime scheduledFor,
                         LocalDateTime sentAt, String message) {
        String channel = "IN_APP";
        boolean emailEnabled = env.getProperty("preppulse.email.enabled", Boolean.class, false);
        JavaMailSender mail = mailProvider.getIfAvailable();
        String recipient = clean(settingsService.getOrCreate().getStudentEmail());

        if (emailEnabled && mail != null && recipient != null) {
            try {
                send(mail, recipient, task, message);
                StudySettings settings = settingsService.getOrCreate();
                String partner = clean(settings.getTrustedPartnerEmail());
                if (settings.isTrustedPartnerEnabled() && partner != null) {
                    send(mail, partner, task, "Study-partner reminder: " + message);
                }
                channel = "EMAIL_SENT";
            } catch (Exception ex) {
                channel = "EMAIL_FAILED";
                log.warn("Could not send reminder email for task {}: {}", task.getId(), ex.getMessage());
            }
        }

        ReminderDelivery delivery = new ReminderDelivery();
        delivery.setTaskId(task.getId());
        delivery.setTaskTitle(task.getTitle());
        delivery.setLeadMinutes(leadMinutes);
        delivery.setScheduledFor(scheduledFor);
        delivery.setSentAt(sentAt);
        delivery.setChannel(channel);
        delivery.setMessage(message);
        deliveryRepo.save(delivery);
        log.info("PrepPulse reminder recorded for task {} ({})", task.getId(), channel);
    }

    private List<Integer> parseOffsets(String value) {
        if (value == null || value.isBlank()) return DEFAULT_OFFSETS;
        List<Integer> values = new ArrayList<>();
        for (String part : value.split(",")) {
            try {
                int minutes = Integer.parseInt(part.trim());
                if (minutes > 0 && minutes <= 10080 && !values.contains(minutes)) values.add(minutes);
            } catch (NumberFormatException ignored) {
                // Invalid values are ignored. The task service also normalizes these on save.
            }
        }
        return values.isEmpty() ? DEFAULT_OFFSETS : values;
    }

    private String upcomingMessage(StudyTask task, int leadMinutes) {
        String when = leadMinutes >= 1440
                ? "in about " + (leadMinutes / 1440) + " day(s)"
                : leadMinutes >= 60
                    ? "in about " + (leadMinutes / 60) + " hour(s)"
                    : "in about " + leadMinutes + " minute(s)";
        return "Reminder: \"" + task.getTitle() + "\" for " + task.getSubject() + " is due " + when + ".";
    }

    private String overdueMessage(StudyTask task) {
        return "Overdue reminder: \"" + task.getTitle() + "\" for " + task.getSubject()
                + " has passed its deadline. Reschedule it if needed.";
    }

    private void send(JavaMailSender sender, String to, StudyTask task, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        String from = clean(env.getProperty("preppulse.email.from"));
        if (from != null) message.setFrom(from);
        message.setSubject("PrepPulse reminder: " + task.getTitle());
        message.setText(text + "\n\nPrepPulse — Plan clearly. Study steadily. Grow daily.");
        sender.send(message);
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    @Transactional(readOnly = true)
    public List<ReminderDelivery> recent() {
        return deliveryRepo.findTop20ByOrderBySentAtDesc();
    }

    @Transactional(readOnly = true)
    public List<ReminderDelivery> after(long id) {
        return deliveryRepo.findTop20ByIdGreaterThanOrderByIdAsc(Math.max(0, id));
    }

    @Transactional(readOnly = true)
    public long latestId() {
        ReminderDelivery delivery = deliveryRepo.findTopByOrderByIdDesc();
        return delivery == null ? 0 : delivery.getId();
    }
}
