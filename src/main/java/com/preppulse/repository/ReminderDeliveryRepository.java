package com.preppulse.repository;
import com.preppulse.model.ReminderDelivery; import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime; import java.util.List;
public interface ReminderDeliveryRepository extends JpaRepository<ReminderDelivery,Long> {
    boolean existsByTaskIdAndLeadMinutesAndScheduledFor(Long taskId, int leadMinutes, LocalDateTime scheduledFor);
    List<ReminderDelivery> findTop20ByOrderBySentAtDesc();
    List<ReminderDelivery> findTop20ByIdGreaterThanOrderByIdAsc(Long id);
    ReminderDelivery findTopByOrderByIdDesc();
}
