package se.sundsvall.remindandinform.integration.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

@CircuitBreaker(name = "reminderRepository")
public interface ReminderRepository extends CrudRepository<ReminderEntity, Long> {

	List<ReminderEntity> findByPartyId(String partyId);

	Optional<ReminderEntity> findByReminderId(String reminderId);

	List<ReminderEntity> findByReminderDateLessThanEqualAndSentFalse(LocalDate reminderDate);

	void deleteByReminderId(String reminderId);
}
