package com.preppulse.service;
import com.preppulse.dto.PriorityRecommendation; import com.preppulse.model.StudyTask;
import org.springframework.stereotype.Service; import java.time.LocalDateTime; import java.time.temporal.ChronoUnit; import java.util.*;
@Service public class SmartPriorityService {
 public List<PriorityRecommendation> rank(List<StudyTask> tasks, LocalDateTime now){
  return tasks.stream().filter(t->!t.isCompleted()).map(t->score(t,now)).sorted(Comparator.comparingInt(PriorityRecommendation::getScore).reversed().thenComparing(r->r.getTask().getDueAt())).toList();
 }
 public PriorityRecommendation score(StudyTask t,LocalDateTime now){
  int score=switch(t.getPriority()==null?StudyTask.Priority.MEDIUM:t.getPriority()){case HIGH->35;case MEDIUM->20;case LOW->8;};
  List<String> why=new ArrayList<>();
  if(t.getPriority()==StudyTask.Priority.HIGH)why.add("High importance");
  long mins=ChronoUnit.MINUTES.between(now,t.getDueAt());
  if(mins<0){score+=100+(int)Math.min(100,Math.abs(mins)/1440*8);why.add("Overdue");}
  else if(mins<=1440){score+=75;why.add("Due within 24 hours");}
  else if(mins<=3*1440){score+=50;why.add("Due in the next 3 days");}
  else if(mins<=7*1440){score+=25;why.add("Due this week");}
  int durationBonus=Math.min(20,Math.max(0,t.getEstimatedMinutes()/30)); score+=durationBonus;
  if(t.getEstimatedMinutes()>=60)why.add("Needs a longer study session");
  if(t.getPostponedCount()>=2){score+=10;why.add("Postponed more than once");}
  if(why.isEmpty())why.add("Fits your remaining study plan");
  return new PriorityRecommendation(t,score,why);
 }
}
