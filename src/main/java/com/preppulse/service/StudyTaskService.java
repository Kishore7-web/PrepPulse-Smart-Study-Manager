package com.preppulse.service;
import com.preppulse.dto.DashboardStats; import com.preppulse.model.StudyTask; import com.preppulse.repository.StudyTaskRepository;
import jakarta.persistence.EntityNotFoundException; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate; import java.time.LocalDateTime; import java.time.LocalTime; import java.util.List;
@Service public class StudyTaskService {
 private final StudyTaskRepository repository;
 public StudyTaskService(StudyTaskRepository repository){this.repository=repository;}
 @Transactional(readOnly=true) public List<StudyTask> getAllTasks(){return repository.findAllByOrderByCompletedAscDueDateAsc();}
 @Transactional(readOnly=true) public StudyTask getTask(Long id){return repository.findById(id).orElseThrow(()->new EntityNotFoundException("Study task not found: "+id));}
 @Transactional public StudyTask create(StudyTask task){task.setId(null);task.setCompleted(false);normalize(task);return repository.save(task);}
 @Transactional public StudyTask update(Long id,StudyTask incoming){StudyTask task=getTask(id);task.setTitle(incoming.getTitle());task.setSubject(incoming.getSubject());task.setPriority(incoming.getPriority());task.setDueDate(incoming.getDueDate());task.setDueTime(incoming.getDueTime());task.setEstimatedMinutes(incoming.getEstimatedMinutes());task.setNotes(incoming.getNotes());task.setReminderEnabled(incoming.isReminderEnabled());task.setReminderIntervals(incoming.getReminderIntervals());normalize(task);return repository.save(task);}
 private void normalize(StudyTask task) {
  if (task.getTitle() == null || task.getTitle().isBlank()) {
   throw new IllegalArgumentException("Task title is required.");
  }
  if (task.getSubject() == null || task.getSubject().isBlank()) {
   throw new IllegalArgumentException("Subject is required.");
  }
  if (task.getDueDate() == null) {
   throw new IllegalArgumentException("Please choose a due date.");
  }
  if (task.getDueTime() == null) task.setDueTime(LocalTime.of(10, 0));
  if (task.getPriority() == null) task.setPriority(StudyTask.Priority.MEDIUM);
  if (task.getEstimatedMinutes() < 5 || task.getEstimatedMinutes() > 720) {
   throw new IllegalArgumentException("Study time must be between 5 and 720 minutes.");
  }

  // Keep reminder settings short and predictable, even when requests come from the REST API.
  String raw = task.getReminderIntervals();
  if (raw == null || raw.isBlank()) {
   task.setReminderIntervals("1440,60,15");
   return;
  }
  java.util.LinkedHashSet<Integer> offsets = new java.util.LinkedHashSet<>();
  for (String part : raw.split(",")) {
   try {
    int value = Integer.parseInt(part.trim());
    if (value > 0 && value <= 10080) offsets.add(value);
   } catch (NumberFormatException ignored) {
    // Ignore malformed reminder values rather than allowing them into the database.
   }
  }
  if (offsets.isEmpty()) offsets.add(15);
  task.setReminderIntervals(offsets.stream().map(String::valueOf).collect(java.util.stream.Collectors.joining(",")));
 }
 @Transactional public void toggleCompleted(Long id){StudyTask task=getTask(id);task.setCompleted(!task.isCompleted());repository.save(task);}
 @Transactional public void postpone(Long id,int days){if(days<1||days>30)throw new IllegalArgumentException("Choose a postponement between 1 and 30 days.");StudyTask task=getTask(id);task.setDueDate(task.getDueDate().plusDays(days));task.setPostponedCount(task.getPostponedCount()+1);task.setLastPostponedAt(LocalDateTime.now());repository.save(task);}
 @Transactional public void delete(Long id){if(!repository.existsById(id))throw new EntityNotFoundException("Study task not found: "+id);repository.deleteById(id);}
 @Transactional(readOnly=true) public DashboardStats getStats(){List<StudyTask> tasks=repository.findAll();long total=tasks.size();long completed=tasks.stream().filter(StudyTask::isCompleted).count();long pending=total-completed;int percent=total==0?0:(int)Math.round(completed*100.0/total);long overdue=tasks.stream().filter(t->!t.isCompleted()&&t.getDueAt()!=null&&t.getDueAt().isBefore(LocalDateTime.now())).count();int minutes=tasks.stream().filter(t->!t.isCompleted()).mapToInt(StudyTask::getEstimatedMinutes).sum();return new DashboardStats(total,completed,pending,percent,overdue,minutes);}
 @Transactional(readOnly=true) public List<StudyTask> getUpcomingTasks(){return repository.findByCompletedFalseAndDueDateLessThanEqualOrderByDueDateAsc(LocalDate.now().plusDays(7)).stream().limit(6).toList();}
 @Transactional(readOnly=true) public List<StudyTask> getPendingTasks(){return repository.findByCompletedFalse();}
}
