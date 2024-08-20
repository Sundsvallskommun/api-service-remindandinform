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

import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.messaging.MessagingClient;
import se.sundsvall.remindandinform.service.mapper.ReminderMapper;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.MessageRequest;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Service
@Transactional
public class SendRemindersLogic {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendRemindersLogic.class);

	private final ReminderRepository reminderRepository;

	private final MessagingClient messagingClient;

	private final ReminderMessageProperties reminderMessageProperties;

	public SendRemindersLogic(final ReminderRepository reminderRepository, final MessagingClient messagingClient, final ReminderMessageProperties reminderMessageProperties) {
		this.reminderRepository = reminderRepository;
		this.messagingClient = messagingClient;
		this.reminderMessageProperties = reminderMessageProperties;
	}

	@Scheduled(cron = "${sendReminders.cron.expr}")
	@SchedulerLock(name = "sendReminders", lockAtMostFor = "${sendReminders.shedlock-lock-at-most-for}")
	public void sendReminders() {

		reminderMessageProperties.getMunicipalityIds().forEach(municipalityId -> sendReminders(now(systemDefault()), municipalityId));
	}

	public void sendReminders(final LocalDate reminderDate, final String municipalityId) {
		final var reminders = findRemindersToSendByReminderDate(reminderDate, municipalityId);

		final var messages = createMessages(reminders);

		if (!messages.isEmpty()) {
			final var messageRequest = new MessageRequest().messages(messages);
			LOGGER.debug("Messages to send to api-messaging-service: '{}'", messageRequest);
			messagingClient.sendMessage(municipalityId, new MessageRequest().messages(messages));
			reminderRepository.saveAll(reminderRepository.findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(reminderDate, municipalityId).stream()
				.map(reminder -> {
					reminder.setSent(true);
					return reminder;
				}).toList());
			LOGGER.info("{} reminders sent for reminderDate", messages.size());
		}
	}

	private List<Reminder> findRemindersToSendByReminderDate(final LocalDate reminderDate, final String municipalityId) {
		final var reminders = ReminderMapper.toReminders(reminderRepository.findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(reminderDate, municipalityId));
		if (reminders.isEmpty()) {
			LOGGER.info("No reminders found for reminderDate");
		}

		return reminders;
	}

	private List<Message> createMessages(final List<Reminder> reminders) {
		return reminders.stream()
			.filter(Objects::nonNull)
			.map(reminder -> toMessage(reminder, reminderMessageProperties))
			.toList();
	}

}
