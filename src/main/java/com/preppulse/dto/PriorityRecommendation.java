package com.preppulse.dto;
import com.preppulse.model.StudyTask; import java.util.List;
public class PriorityRecommendation {
    private final StudyTask task; private final int score; private final List<String> reasons;
    public PriorityRecommendation(StudyTask task,int score,List<String> reasons){this.task=task;this.score=score;this.reasons=List.copyOf(reasons);}
    public StudyTask getTask(){return task;} public int getScore(){return score;} public List<String> getReasons(){return reasons;}
}
