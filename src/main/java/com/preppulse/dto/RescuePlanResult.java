package com.preppulse.dto;
import java.util.List;
public class RescuePlanResult {
    private final int availableMinutes, plannedMinutes, tasksCovered, tasksStillNeedTime; private final List<PlanBlock> blocks; private final String message;
    public RescuePlanResult(int a,int p,int c,int r,List<PlanBlock>b,String m){availableMinutes=a;plannedMinutes=p;tasksCovered=c;tasksStillNeedTime=r;blocks=List.copyOf(b);message=m;}
    public int getAvailableMinutes(){return availableMinutes;} public int getPlannedMinutes(){return plannedMinutes;} public int getTasksCovered(){return tasksCovered;} public int getTasksStillNeedTime(){return tasksStillNeedTime;} public List<PlanBlock> getBlocks(){return blocks;} public String getMessage(){return message;}
}
