package com.preppulse.service;
import com.preppulse.dto.*; import com.preppulse.model.StudyTask; import org.springframework.stereotype.Service;
import java.time.LocalDateTime; import java.util.*;
@Service public class RescuePlanService {
 private final SmartPriorityService priority; public RescuePlanService(SmartPriorityService p){priority=p;}
 public RescuePlanResult createPlan(int requestedMinutes,List<StudyTask> tasks,LocalDateTime now){
  int available=Math.max(5,Math.min(720,requestedMinutes));List<PlanBlock> blocks=new ArrayList<>();List<PriorityRecommendation> ranked=priority.rank(tasks,now);Map<Long,Integer> remaining=new LinkedHashMap<>();for(var r:ranked)remaining.put(r.getTask().getId(),r.getTask().getEstimatedMinutes());int used=0,focusSinceBreak=0;Set<Long> covered=new HashSet<>();
  outer: for(var r:ranked){StudyTask t=r.getTask();int todo=remaining.getOrDefault(t.getId(),0);while(todo>0){if(used>=available)break outer;if(focusSinceBreak>=50&&available-used>=10){blocks.add(new PlanBlock("BREAK","Short reset","Break",5,"Stand up, stretch, and drink water."));used+=5;focusSinceBreak=0;if(used>=available)break outer;}
    int capacity=available-used;int chunk=Math.min(25,Math.min(todo,capacity));if(chunk<=0)break outer;blocks.add(new PlanBlock("STUDY",t.getTitle(),t.getSubject(),chunk,reasonLine(r)));used+=chunk;focusSinceBreak+=chunk;todo-=chunk;remaining.put(t.getId(),todo);covered.add(t.getId());
   }}
  int left=(int)remaining.values().stream().filter(v->v>0).count();String message=ranked.isEmpty()?"Nothing is overdue or pending. Enjoy your breathing room.":left==0?"Your highest-priority work fits into this plan. Take it one block at a time.":"This is a realistic first pass. Some work will remain; keep the urgent items first.";
  return new RescuePlanResult(available,used,covered.size(),left,blocks,message);
 }
 private String reasonLine(PriorityRecommendation r){return "Priority score "+r.getScore()+" · "+String.join(", ",r.getReasons());}
}
