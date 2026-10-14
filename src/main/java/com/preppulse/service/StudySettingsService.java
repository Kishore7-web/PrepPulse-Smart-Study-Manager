package com.preppulse.service;
import com.preppulse.model.StudySettings; import com.preppulse.repository.StudySettingsRepository;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class StudySettingsService {
 private final StudySettingsRepository repository; public StudySettingsService(StudySettingsRepository r){repository=r;}
 @Transactional public StudySettings getOrCreate(){return repository.findById(1L).orElseGet(()->{StudySettings s=new StudySettings();s.setId(1L);return repository.save(s);});}
 @Transactional public StudySettings update(StudySettings incoming){StudySettings current=getOrCreate();current.setDailyAvailableMinutes(Math.max(15,Math.min(960,incoming.getDailyAvailableMinutes())));current.setStudentEmail(clean(incoming.getStudentEmail()));current.setTrustedPartnerEmail(clean(incoming.getTrustedPartnerEmail()));current.setRemindersEnabled(incoming.isRemindersEnabled());current.setTrustedPartnerEnabled(incoming.isTrustedPartnerEnabled() && current.getTrustedPartnerEmail()!=null);return repository.save(current);}
 private String clean(String s){return s==null||s.isBlank()?null:s.trim();}
}
