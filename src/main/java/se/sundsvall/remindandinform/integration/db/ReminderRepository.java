package se.sundsvall.remindandinform.integration.db;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@CircuitBreaker(name = "reminderRepository")
public interface ReminderRepository extends CrudRepository<ReminderEntity, Long> {

	List<ReminderEntity> findByPartyIdAndMunicipalityId(String partyId, String municipalityId);

	Optional<ReminderEntity> findByReminderIdAndMunicipalityId(String reminderId, String municipalityId);

	List<ReminderEntity> findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate reminderDate, String municipalityId);

	void deleteByReminderId(String reminderId);

}
