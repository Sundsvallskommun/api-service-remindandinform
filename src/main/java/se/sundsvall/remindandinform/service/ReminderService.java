package se.sundsvall.remindandinform.service;

import static java.lang.String.format;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toMergedReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminder;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminders;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;

import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

@Service
@Transactional
public class ReminderService {

	private final ReminderRepository reminderRepository;

	public ReminderService(final ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public Reminder createReminder(final ReminderRequest reminderRequest, final String municipalityId) {

		final String reminderId = "R-" + UUID.randomUUID();

		return toReminder(reminderRepository.save(toReminderEntity(reminderRequest, reminderId, municipalityId)));
	}

	public Reminder updateReminder(final UpdateReminderRequest updateReminderRequest, final String reminderId, final String municipalityId) {

		final var existingReminderEntity = reminderRepository.findByReminderIdAndMunicipalityId(reminderId, municipalityId).orElseThrow(() -> Problem.valueOf(NOT_FOUND, format("No reminder with reminderId:'%s' was found!", reminderId)));

		// Find changes and create new entity to save
		final var newReminderEntity = toMergedReminderEntity(existingReminderEntity, toReminderEntity(updateReminderRequest, reminderId, municipalityId));
		// Save entity
		reminderRepository.save(newReminderEntity);
		// Read and return updated entity
		return findReminderByReminderIdAndMunicipalityId(reminderId, municipalityId);
	}

	public void deleteReminderByReminderIdAndMunicipalityId(final String reminderId, final String municipalityId) {

		if (reminderRepository.findByReminderIdAndMunicipalityId(reminderId, municipalityId).isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, format("No reminder found for reminderId:'%s'", reminderId));
		}
		reminderRepository.deleteByReminderId(reminderId);
	}

	public List<Reminder> findRemindersByPartyIdAndMunicipalityId(final String partyId, final String municipalityId) {
		final var reminders = toReminders(reminderRepository.findByPartyIdAndMunicipalityId(partyId, municipalityId));

		if (reminders.isEmpty()) {
			throw Problem.valueOf(NOT_FOUND, format("No reminders found for partyId:'%s' and municipalityId: '%s'", partyId, municipalityId));
		}

		return reminders;
	}

	public Reminder findReminderByReminderIdAndMunicipalityId(final String reminderId, final String municipalityId) {
		return toReminder(reminderRepository.findByReminderIdAndMunicipalityId(reminderId, municipalityId).orElseThrow(() -> Problem.valueOf(NOT_FOUND, format("No reminders found for reminderId:'%s'", reminderId))));
	}

}
