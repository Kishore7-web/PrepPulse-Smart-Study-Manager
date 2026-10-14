package com.preppulse.dto;
import jakarta.validation.constraints.Max; import jakarta.validation.constraints.Min;
public class FocusSessionRequest {
    @Min(1) @Max(720) private int durationMinutes; private Long taskId; private String sessionType="FOCUS"; private String note;
    public int getDurationMinutes(){return durationMinutes;} public void setDurationMinutes(int v){durationMinutes=v;}
    public Long getTaskId(){return taskId;} public void setTaskId(Long v){taskId=v;}
    public String getSessionType(){return sessionType;} public void setSessionType(String v){sessionType=v;}
    public String getNote(){return note;} public void setNote(String v){note=v;}
}
