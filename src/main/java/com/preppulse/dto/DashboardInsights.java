package com.preppulse.dto;
import java.util.List;
public class DashboardInsights {
    private final int todayMinutes,weekMinutes,weekSessions,postponedTasks,dueReviews,overdueTasks;
    private final List<SubjectMinutes> subjectMinutes;
    public DashboardInsights(int today,int week,int sessions,int postponed,int reviews,int overdue,List<SubjectMinutes> subjects){todayMinutes=today;weekMinutes=week;weekSessions=sessions;postponedTasks=postponed;dueReviews=reviews;overdueTasks=overdue;subjectMinutes=List.copyOf(subjects);}
    public int getTodayMinutes(){return todayMinutes;} public int getWeekMinutes(){return weekMinutes;} public int getWeekSessions(){return weekSessions;} public int getPostponedTasks(){return postponedTasks;} public int getDueReviews(){return dueReviews;} public int getOverdueTasks(){return overdueTasks;} public List<SubjectMinutes> getSubjectMinutes(){return subjectMinutes;}
    public static class SubjectMinutes {private final String subject;private final int minutes;public SubjectMinutes(String s,int m){subject=s;minutes=m;}public String getSubject(){return subject;}public int getMinutes(){return minutes;}}
}
