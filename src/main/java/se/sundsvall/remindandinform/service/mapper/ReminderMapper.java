package se.sundsvall.remindandinform.service.mapper;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.web.util.HtmlUtils;

import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

import generated.se.sundsvall.messaging.Email;
import generated.se.sundsvall.messaging.Message;
import generated.se.sundsvall.messaging.MessageParty;
import generated.se.sundsvall.messaging.MessageSender;
import generated.se.sundsvall.messaging.Sms;

public final class ReminderMapper {

	private ReminderMapper() {}

	public static Reminder toReminder(final ReminderEntity reminderEntity) {
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

	public static ReminderEntity toReminderEntity(final ReminderRequest reminderRequest, final String reminderId, final String municipalityId) {
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
		reminderEntity.setMunicipalityId(municipalityId);

		return reminderEntity;
	}

	public static ReminderEntity toReminderEntity(final UpdateReminderRequest updateReminderRequest, final String reminderId, final String municipalityId) {
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
		reminderEntity.setMunicipalityId(municipalityId);

		return reminderEntity;
	}

	public static ReminderEntity toMergedReminderEntity(final ReminderEntity oldEntity, final ReminderEntity newEntity) {
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

	public static List<Reminder> toReminders(final List<ReminderEntity> reminderEntities) {
		return reminderEntities.stream()
			.filter(Objects::nonNull)
			.map(ReminderMapper::toReminder)
			.toList();
	}

	public static Message toMessage(final Reminder reminder, final ReminderMessageProperties reminderMessageProperties) {

		final var messageText = format(reminderMessageProperties.getMessage(), reminder.getCaseId());
		final var emailMessageText = format(decodeSpecialChars(reminderMessageProperties.getEmailMessage()), HtmlUtils.htmlEscape(reminder.getAction()), reminder.getCaseId());

		final var sender = new MessageSender().email(toEmail(reminderMessageProperties.getSenderEmailName(), reminderMessageProperties.getSenderEmailAddress()))
			.sms(toSms(reminderMessageProperties.getSenderSmsName()));

		return new Message()
			.sender(sender)
			.party(toParty(reminder.getPartyId()))
			.message(messageText)
			.htmlMessage(Base64.getEncoder().encodeToString(emailMessageText.getBytes(StandardCharsets.UTF_8)))
			.subject(reminderMessageProperties.getSubject());
	}

	private static Email toEmail(final String senderEmailName, final String senderEmailAddress) {
		return new Email()
			.name(senderEmailName)
			.address(senderEmailAddress);
	}

	private static Sms toSms(final String senderSmsName) {
		return new Sms()
			.name(senderSmsName);
	}

	private static MessageParty toParty(final String partyId) {
		return new MessageParty()
			.partyId(UUID.fromString(partyId))
			.externalReferences(emptyList());
	}

	private static String decodeSpecialChars(final String stringToConvert) {
		return stringToConvert.replace("%n", "<br>");
	}

}
