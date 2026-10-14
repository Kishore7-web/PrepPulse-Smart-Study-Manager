package com.preppulse.dto;
public class PlanBlock {
    private final String type; private final String title; private final String subject; private final int durationMinutes; private final String note;
    public PlanBlock(String type,String title,String subject,int durationMinutes,String note){this.type=type;this.title=title;this.subject=subject;this.durationMinutes=durationMinutes;this.note=note;}
    public String getType(){return type;} public String getTitle(){return title;} public String getSubject(){return subject;} public int getDurationMinutes(){return durationMinutes;} public String getNote(){return note;}
}
