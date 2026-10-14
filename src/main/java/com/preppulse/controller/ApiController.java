package com.preppulse.controller;
import com.preppulse.dto.*; import com.preppulse.model.*; import com.preppulse.service.*; import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*; import java.time.LocalDateTime; import java.util.List;
@RestController @RequestMapping("/api") public class ApiController {
 private final StudyTaskService tasks; private final SmartPriorityService priority; private final RescuePlanService rescue; private final ReminderService reminders;
 public ApiController(StudyTaskService t,SmartPriorityService p,RescuePlanService r,ReminderService m){tasks=t;priority=p;rescue=r;reminders=m;}
 @GetMapping("/tasks") public List<StudyTask> tasks(){return tasks.getAllTasks();}
 @GetMapping("/stats") public DashboardStats stats(){return tasks.getStats();}
 @GetMapping("/recommended") public List<PriorityRecommendation> recommended(){return priority.rank(tasks.getPendingTasks(),LocalDateTime.now()).stream().limit(10).toList();}
 @PostMapping("/tasks") public StudyTask create(@Valid @RequestBody StudyTask task){return tasks.create(task);}
 @PutMapping("/tasks/{id}") public StudyTask update(@PathVariable Long id,@Valid @RequestBody StudyTask task){return tasks.update(id,task);}
 @PatchMapping("/tasks/{id}/toggle") public void toggle(@PathVariable Long id){tasks.toggleCompleted(id);}
 @DeleteMapping("/tasks/{id}") public void delete(@PathVariable Long id){tasks.delete(id);}
 @PostMapping("/rescue-plan") public RescuePlanResult rescuePlan(@RequestBody RescueRequest request){return rescue.createPlan(request.getAvailableMinutes(),tasks.getPendingTasks(),LocalDateTime.now());}
 @GetMapping("/reminders/recent") public List<ReminderDelivery> recentReminders(@RequestParam(defaultValue="0") long after){return after>0?reminders.after(after):reminders.recent();}
 @GetMapping("/reminders/latest-id") public long latestReminderId(){return reminders.latestId();}
}
