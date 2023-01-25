package se.sundsvall.remindandinform.service.mapper;

import generated.se.sundsvall.messaging.Email;
import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.Party;
import generated.se.sundsvall.messaging.Sender;
import generated.se.sundsvall.messaging.Sms;
import org.springframework.web.util.HtmlUtils;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public class ReminderMapper {

	private ReminderMapper() {}

	public static Reminder toReminder(ReminderEntity reminderEntity) {
		return Reminder.create()
			.withAction(reminderEntity.getAction())
			.withCaseId(reminderEntity.getCaseId())
			.withCaseType(reminderEntity.getCaseType())
			.withCaseLink(reminderEntity.getCaseLink())
			.withCreated(reminderEntity.getCreated())
			.withCreatedBy(reminderEntity.getCreatedBy())
			.withExternalCaseId(reminderEntity.getExternalCaseId())
			.withNote(reminderEntity.getNote())
			.withModified(reminderEntity.getModified())
			.withModifiedBy(reminderEntity.getModifiedBy())
			.withPartyId(reminderEntity.getPartyId())
			.withReminderDate(reminderEntity.getReminderDate())
			.withReminderId(reminderEntity.getReminderId());
	}

	public static ReminderEntity toReminderEntity(ReminderRequest reminderRequest, String reminderId) {
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPartyId(reminderRequest.getPartyId());
		reminderEntity.setCaseId(reminderRequest.getCaseId());
		reminderEntity.setCaseType(reminderRequest.getCaseType());
		reminderEntity.setCaseLink(reminderRequest.getCaseLink());
		reminderEntity.setExternalCaseId(reminderRequest.getExternalCaseId());
		reminderEntity.setAction(reminderRequest.getAction());
		reminderEntity.setNote(reminderRequest.getNote());
		reminderEntity.setReminderDate(reminderRequest.getReminderDate());
		reminderEntity.setCreatedBy(reminderRequest.getCreatedBy());

		return reminderEntity;
	}

	public static ReminderEntity toReminderEntity(UpdateReminderRequest updateReminderRequest, String reminderId) {
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPartyId(updateReminderRequest.getPartyId());
		reminderEntity.setCaseId(updateReminderRequest.getCaseId());
		reminderEntity.setCaseType(updateReminderRequest.getCaseType());
		reminderEntity.setCaseLink(updateReminderRequest.getCaseLink());
		reminderEntity.setExternalCaseId(updateReminderRequest.getExternalCaseId());
		reminderEntity.setAction(updateReminderRequest.getAction());
		reminderEntity.setNote(updateReminderRequest.getNote());
		reminderEntity.setReminderDate(updateReminderRequest.getReminderDate());
		reminderEntity.setModifiedBy(updateReminderRequest.getModifiedBy());

		return reminderEntity;
	}

	public static ReminderEntity toMergedReminderEntity(ReminderEntity oldEntity, ReminderEntity newEntity) {
		ofNullable(newEntity.getPartyId()).ifPresent(oldEntity::setPartyId);
		ofNullable(newEntity.getCaseId()).ifPresent(oldEntity::setCaseId);
		ofNullable(newEntity.getCaseType()).ifPresent(oldEntity::setCaseType);
		ofNullable(newEntity.getCaseLink()).ifPresent(oldEntity::setCaseLink);
		ofNullable(newEntity.getExternalCaseId()).ifPresent(oldEntity::setExternalCaseId);
		ofNullable(newEntity.getAction()).ifPresent(oldEntity::setAction);
		ofNullable(newEntity.getNote()).ifPresent(oldEntity::setNote);
		ofNullable(newEntity.getReminderDate()).ifPresent(oldEntity::setReminderDate);
		ofNullable(newEntity.getCreatedBy()).ifPresent(oldEntity::setCreatedBy);
		ofNullable(newEntity.getModifiedBy()).ifPresent(oldEntity::setModifiedBy);
		
		return oldEntity;
	}

	public static List<Reminder> toReminders(List<ReminderEntity> reminderEntities) {
		return reminderEntities.stream().filter(Objects::nonNull)
			.map(ReminderMapper::toReminder)
			.toList();
	}

	public static Message toMessage(Reminder reminder, ReminderMessageProperties reminderMessageProperties) {

		final var messageText = format(reminderMessageProperties.getMessage(), reminder.getCaseId());
		final var emailMessageText = format(decodeSpecialChars(reminderMessageProperties.getEmailMessage()), HtmlUtils.htmlEscape(reminder.getAction()), reminder.getCaseId());

		final var sender = new Sender().email(toEmail(reminderMessageProperties.getSenderEmailName(), reminderMessageProperties.getSenderEmailAddress()))
			.sms(toSms(reminderMessageProperties.getSenderSmsName()));

		return new Message()
			.sender(sender)
			.party(toParty(reminder.getPartyId()))
			.message(messageText)
			.htmlMessage(Base64.getEncoder().encodeToString(emailMessageText.getBytes(StandardCharsets.UTF_8)))
			.subject(reminderMessageProperties.getSubject());
	}

	private static Email toEmail(String senderEmailName, String senderEmailAddress) {
		return new Email()
				.name(senderEmailName)
				.address(senderEmailAddress);
	}

	private static Sms toSms(String senderSmsName) {
		return new Sms()
				.name(senderSmsName);
	}

	private static Party toParty(String partyId) {
		return new Party()
				.partyId(partyId);
	}

	private static String decodeSpecialChars(String stringToConvert) {
		return stringToConvert.replace("%n", "<br>");
	}
}
