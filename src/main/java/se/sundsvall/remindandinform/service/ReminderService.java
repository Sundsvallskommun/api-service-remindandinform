package se.sundsvall.remindandinform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;
import static org.apache.commons.lang3.BooleanUtils.isNotTrue;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toMergedReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminder;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminderEntity;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toReminders;


@Service
public class ReminderService {

	@Autowired
	private ReminderRepository reminderRepository;

	@Transactional
	public Reminder createReminder(ReminderRequest reminderRequest) {

		String reminderId = "R-" + UUID.randomUUID();

		return toReminder(reminderRepository.save(toReminderEntity(reminderRequest, reminderId)));
	}

	@Transactional
	public Reminder updateReminder(UpdateReminderRequest updateReminderRequest, String reminderId) {

		var existingReminderEntity = reminderRepository.findByReminderId(reminderId).orElseThrow(() -> Problem.valueOf(NOT_FOUND, format("No reminder with reminderId:'%s' was found!", reminderId)));

		//Find changes and create new entity to save
		var newReminderEntity = toMergedReminderEntity(existingReminderEntity, toReminderEntity(updateReminderRequest, reminderId));
		//Save entity
		reminderRepository.save(newReminderEntity);
		//Read and return updated entity
		return findReminderByReminderId(reminderId);
	}

	@Transactional
	public void deleteReminderByReminderId(String reminderId) {

		if (isNotTrue(reminderRepository.findByReminderId(reminderId).isPresent())) {
			throw Problem.valueOf(NOT_FOUND, format("No reminder found for reminderId:'%s'", reminderId));
		}
		reminderRepository.deleteByReminderId(reminderId);
	}


	public List<Reminder> findRemindersByPartyId(String partyId) {
		var reminders = toReminders(reminderRepository.findByPartyId(partyId));

		if(reminders.isEmpty()) {
			throw Problem.valueOf(Status.NOT_FOUND, format("No reminders found for partyId:'%s'", partyId));
		}

		return reminders;
	}

	public Reminder findReminderByReminderId(String reminderId) {
		return toReminder(reminderRepository.findByReminderId(reminderId).orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, format("No reminders found for reminderId:'%s'", reminderId))));
	}
}
