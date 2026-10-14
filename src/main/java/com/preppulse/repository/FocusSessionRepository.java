package com.preppulse.repository;
import com.preppulse.model.FocusSession; import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime; import java.util.List;
public interface FocusSessionRepository extends JpaRepository<FocusSession,Long> {
    List<FocusSession> findBySessionTypeAndStartedAtAfterOrderByStartedAtDesc(String type, LocalDateTime after);
    List<FocusSession> findBySessionTypeAndStartedAtBetweenOrderByStartedAtDesc(String type, LocalDateTime from, LocalDateTime to);
    List<FocusSession> findTop8ByOrderByStartedAtDesc();
}
