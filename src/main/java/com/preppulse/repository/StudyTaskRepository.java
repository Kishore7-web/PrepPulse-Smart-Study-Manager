package com.preppulse.repository;
import com.preppulse.model.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
    List<StudyTask> findAllByOrderByCompletedAscDueDateAsc();
    List<StudyTask> findByCompletedFalseAndDueDateLessThanEqualOrderByDueDateAsc(LocalDate date);
    List<StudyTask> findByCompletedFalse();
    long countByCompletedTrue(); long countByCompletedFalse();
}
