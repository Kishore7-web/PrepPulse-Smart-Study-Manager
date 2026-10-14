package com.preppulse.repository;
import com.preppulse.model.StudyTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate; import java.util.List;
public interface StudyTopicRepository extends JpaRepository<StudyTopic,Long> {
    List<StudyTopic> findAllByArchivedFalseOrderByNextReviewDateAsc();
    List<StudyTopic> findByArchivedFalseAndNextReviewDateLessThanEqualOrderByNextReviewDateAsc(LocalDate date);
    long countByArchivedFalseAndNextReviewDateLessThanEqual(LocalDate date);
}
