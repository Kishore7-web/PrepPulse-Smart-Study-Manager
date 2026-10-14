package com.preppulse.service;
import com.preppulse.model.StudyTopic; import com.preppulse.repository.StudyTopicRepository; import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.time.LocalDate; import java.util.List;
@Service public class RevisionService {
 private final StudyTopicRepository repository; public RevisionService(StudyTopicRepository r){repository=r;}
 @Transactional(readOnly=true) public List<StudyTopic> list(){return repository.findAllByArchivedFalseOrderByNextReviewDateAsc();}
 @Transactional(readOnly=true) public List<StudyTopic> due(){return repository.findByArchivedFalseAndNextReviewDateLessThanEqualOrderByNextReviewDateAsc(LocalDate.now());}
 @Transactional public StudyTopic add(StudyTopic topic){topic.setId(null);topic.setLastReviewedDate(LocalDate.now());topic.setIntervalDays(1);topic.setReviewCount(0);topic.setArchived(false);topic.setNextReviewDate(LocalDate.now().plusDays(1));if(topic.getDifficulty()==null)topic.setDifficulty(StudyTopic.Difficulty.MEDIUM);return repository.save(topic);}
 @Transactional public StudyTopic markReviewed(Long id){StudyTopic t=repository.findById(id).orElseThrow(()->new EntityNotFoundException("Revision topic not found: "+id));int[] medium={1,3,7,14,28};int[] hard={1,2,4,7,14};int[] easy={1,4,9,18,30};int index=Math.min(t.getReviewCount()+1,4);int[] intervals=switch(t.getDifficulty()){case EASY->easy;case HARD->hard;default->medium;};int next=intervals[index];t.setReviewCount(t.getReviewCount()+1);t.setLastReviewedDate(LocalDate.now());t.setIntervalDays(next);t.setNextReviewDate(LocalDate.now().plusDays(next));return repository.save(t);}
 @Transactional public void archive(Long id){StudyTopic t=repository.findById(id).orElseThrow(()->new EntityNotFoundException("Revision topic not found: "+id));t.setArchived(true);repository.save(t);}
 public long countDue(){return repository.countByArchivedFalseAndNextReviewDateLessThanEqual(LocalDate.now());}
}
