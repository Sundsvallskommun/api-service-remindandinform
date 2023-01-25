package se.sundsvall.remindandinform.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.repository.CrudRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CircuitBreaker(name = "reminderRepository")
public interface ReminderRepository extends CrudRepository<ReminderEntity, String> {

	List<ReminderEntity> findByPartyId(String partyId);

	Optional<ReminderEntity> findByReminderId(String reminderId);

	List<ReminderEntity> findByReminderDateLessThanEqualAndSentFalse(LocalDate reminderDate);

	void deleteByReminderId(String reminderId);
}
