package com.preppulse.service;
import com.preppulse.dto.FocusSessionRequest; import com.preppulse.model.FocusSession; import com.preppulse.model.StudyTask; import com.preppulse.repository.FocusSessionRepository; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDateTime; import java.util.List;
@Service public class FocusSessionService {
 private final FocusSessionRepository repository; private final StudyTaskService taskService;
 public FocusSessionService(FocusSessionRepository r,StudyTaskService t){repository=r;taskService=t;}
 @Transactional public FocusSession save(FocusSessionRequest request){if(request.getDurationMinutes()<1||request.getDurationMinutes()>720)throw new IllegalArgumentException("A study session must be between 1 and 720 minutes.");FocusSession s=new FocusSession();s.setDurationMinutes(request.getDurationMinutes());s.setEndedAt(LocalDateTime.now());s.setStartedAt(s.getEndedAt().minusMinutes(request.getDurationMinutes()));String type=request.getSessionType()==null?"FOCUS":request.getSessionType().toUpperCase();s.setSessionType(type.equals("BREAK")?"BREAK":"FOCUS");s.setNote(request.getNote());if(request.getTaskId()!=null){StudyTask task=taskService.getTask(request.getTaskId());s.setTask(task);}return repository.save(s);}
 @Transactional(readOnly=true) public List<FocusSession> recent(){return repository.findTop8ByOrderByStartedAtDesc();}
 @Transactional(readOnly=true) public int minutesBetween(LocalDateTime from,LocalDateTime to){return repository.findBySessionTypeAndStartedAtBetweenOrderByStartedAtDesc("FOCUS",from,to).stream().mapToInt(FocusSession::getDurationMinutes).sum();}
 @Transactional(readOnly=true) public int countFocusBetween(LocalDateTime from,LocalDateTime to){return repository.findBySessionTypeAndStartedAtBetweenOrderByStartedAtDesc("FOCUS",from,to).size();}
 @Transactional(readOnly=true) public List<FocusSession> focusAfter(LocalDateTime from){return repository.findBySessionTypeAndStartedAtAfterOrderByStartedAtDesc("FOCUS",from);}
}
