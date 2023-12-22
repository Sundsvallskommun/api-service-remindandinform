package se.sundsvall.remindandinform.service.logic;

import static java.time.LocalDate.now;
import static java.time.ZoneId.systemDefault;
import static se.sundsvall.remindandinform.service.mapper.ReminderMapper.toMessage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.MessageRequest;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.messaging.MessagingClient;
import se.sundsvall.remindandinform.service.mapper.ReminderMapper;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

@Service
@Transactional
public class SendRemindersLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendRemindersLogic.class);

	private final ReminderRepository reminderRepository;
	private final MessagingClient messagingClient;
	private final ReminderMessageProperties reminderMessageProperties;

	public SendRemindersLogic(ReminderRepository reminderRepository, MessagingClient messagingClient, ReminderMessageProperties reminderMessageProperties) {
		this.reminderRepository = reminderRepository;
		this.messagingClient = messagingClient;
		this.reminderMessageProperties = reminderMessageProperties;
	}

	@Scheduled(cron = "${sendReminders.cron.expr}")
	public void sendReminders() {
		sendReminders(now(systemDefault()));
	}

	public void sendReminders(LocalDate reminderDate) {
		final var reminders = findRemindersToSendByReminderDate(reminderDate);

		final var messages = createMessages(reminders);

		if (!messages.isEmpty()) {
			final var messageRequest = new MessageRequest().messages(messages);
			LOGGER.debug("Messages to send to api-messaging-service: '{}'", messageRequest);
			messagingClient.sendMessage(new MessageRequest().messages(messages));
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
		final var reminders = ReminderMapper.toReminders(reminderRepository.findByReminderDateLessThanEqualAndSentFalse(reminderDate));
		if (reminders.isEmpty()) {
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
