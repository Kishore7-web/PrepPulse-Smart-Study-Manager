package com.preppulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Email;

@Entity @Table(name = "study_settings")
public class StudySettings {
    @Id private Long id = 1L;
    @Min(15) @Max(960) @Column(nullable = false) private int dailyAvailableMinutes = 180;
    @Email @Column(length = 180) private String studentEmail;
    @Email @Column(length = 180) private String trustedPartnerEmail;
    @Column(nullable = false) private boolean remindersEnabled = true;
    @Column(nullable = false) private boolean trustedPartnerEnabled = false;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public int getDailyAvailableMinutes(){return dailyAvailableMinutes;} public void setDailyAvailableMinutes(int v){dailyAvailableMinutes=v;}
    public String getStudentEmail(){return studentEmail;} public void setStudentEmail(String v){studentEmail=v;}
    public String getTrustedPartnerEmail(){return trustedPartnerEmail;} public void setTrustedPartnerEmail(String v){trustedPartnerEmail=v;}
    public boolean isRemindersEnabled(){return remindersEnabled;} public void setRemindersEnabled(boolean v){remindersEnabled=v;}
    public boolean isTrustedPartnerEnabled(){return trustedPartnerEnabled;} public void setTrustedPartnerEnabled(boolean v){trustedPartnerEnabled=v;}
}
