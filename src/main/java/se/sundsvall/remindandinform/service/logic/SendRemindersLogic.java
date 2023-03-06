package se.sundsvall.remindandinform.service.logic;

import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toMessage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.MessageRequest;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.messaging.ApiMessagingClient;
import se.sundsvall.remindandinform.service.mapper.ReminderMapper;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

@Service
public class SendRemindersLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendRemindersLogic.class);

	@Autowired
	private ReminderRepository reminderRepository;

	@Autowired
	private ApiMessagingClient apiMessagingClient;

	@Autowired
	private ReminderMessageProperties reminderMessageProperties;

	@Scheduled(cron = "${sendReminders.cron.expr}")
	@Transactional
	public void sendReminders() {
		sendReminders(LocalDate.now(ZoneId.systemDefault()));
	}

	@Transactional
	public void sendReminders(LocalDate reminderDate) {
		final var reminders = findRemindersToSendByReminderDate(reminderDate);

		final var messages = createMessages(reminders);

		if (!messages.isEmpty()) {
			final var messageRequest = new MessageRequest().messages(messages);
			LOGGER.debug("Messages to send to api-messaging-service: '{}'", messageRequest);
			apiMessagingClient.sendMessage(new MessageRequest().messages(messages));
			reminderRepository.saveAll(reminderRepository.findByReminderDateLessThanEqualAndSentFalse(reminderDate).stream()
					.map(reminder -> {
						reminder.setSent(true);
						return reminder;
					})
					.toList());
			LOGGER.info("{} reminders sent for reminderDate: '{}'", messages.size(), reminderDate);
		}
	}

	private List<Reminder> findRemindersToSendByReminderDate(LocalDate reminderDate) {
		var reminders = ReminderMapper.toReminders(reminderRepository.findByReminderDateLessThanEqualAndSentFalse(reminderDate));
		if(reminders.isEmpty()) {
			LOGGER.info("No reminders found for reminderDate: '{}'", reminderDate);
		}

		return reminders;
	}

	private List<Message> createMessages(List<Reminder> reminders) {
		return reminders.stream()
				.filter(Objects::nonNull)
				.map(reminder -> toMessage(reminder, reminderMessageProperties))
				.toList();
	}
}
