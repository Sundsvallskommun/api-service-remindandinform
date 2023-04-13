package se.sundsvall.remindandinform.service.mapper;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

@ExtendWith(MockitoExtension.class)
class ReminderMapperTest {

	@Mock
	private ReminderMessageProperties reminderMessagePropertiesMock;

	@Test
	void toReminderSuccess() {

		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId("reminderId");
		reminderEntity.setPartyId("partyId");
		reminderEntity.setAction("action");
		reminderEntity.setNote("note");
		reminderEntity.setCaseLink("caseLink");
		reminderEntity.setCaseType("caseType");
		reminderEntity.setCaseId("caseId");
		reminderEntity.setExternalCaseId("externalCaseId");
		reminderEntity.setReminderDate(LocalDate.now());

		final var reminder = ReminderMapper.toReminder(reminderEntity);

		assertThat(reminder.getReminderId()).isEqualTo("reminderId");
		assertThat(reminder.getPartyId()).isEqualTo("partyId");
		assertThat(reminder.getCaseId()).isEqualTo("caseId");
		assertThat(reminder.getCaseType()).isEqualTo("caseType");
		assertThat(reminder.getCaseLink()).isEqualTo("caseLink");
		assertThat(reminder.getExternalCaseId()).isEqualTo("externalCaseId");
		assertThat(reminder.getAction()).isEqualTo("action");
		assertThat(reminder.getNote()).isEqualTo("note");
		assertThat(reminder.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toReminderFromReminderRequestEntitySuccess() {

		final var partyId = "partyId";
		final var action = "action";
		final var note = "note";
		final var caseId = "caseId";
		final var caseType = "caseType";
		final var caseLink = "caseLink";
		final var externalCaseId = "externalCaseId";
		final var reminderId = "reminderId";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var reminderRequest = ReminderRequest.create()
			.withPartyId(partyId)
			.withAction(action)
			.withNote(note)
			.withCaseId(caseId)
			.withCaseType(caseType)
			.withCaseLink(caseLink)
			.withExternalCaseId(externalCaseId)
			.withReminderDate(reminderDate);

		final var reminderEntity = ReminderMapper.toReminderEntity(reminderRequest, reminderId);

		assertThat(reminderEntity.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntity.getPartyId()).isEqualTo(partyId);
		assertThat(reminderEntity.getCaseType()).isEqualTo(caseType);
		assertThat(reminderEntity.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntity.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(reminderEntity.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntity.getAction()).isEqualTo(action);
		assertThat(reminderEntity.getNote()).isEqualTo(note);
		assertThat(reminderEntity.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toReminderFromUpdateReminderRequestEntitySuccess() {

		final var partyId = "partyId";
		final var action = "action";
		final var note = "note";
		final var caseId = "caseId";
		final var caseType = "caseType";
		final var caseLink = "caseLink";
		final var externalCaseId = "externalCaseId";
		final var reminderId = "reminderId";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var updateReminderRequest = UpdateReminderRequest.create()
			.withPartyId(partyId)
			.withAction(action)
			.withNote(note)
			.withCaseId(caseId)
			.withCaseType(caseType)
			.withCaseLink(caseLink)
			.withExternalCaseId(externalCaseId)
			.withReminderDate(reminderDate);

		final var reminderEntity = ReminderMapper.toReminderEntity(updateReminderRequest, reminderId);

		assertThat(reminderEntity.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntity.getPartyId()).isEqualTo(partyId);
		assertThat(reminderEntity.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntity.getCaseType()).isEqualTo(caseType);
		assertThat(reminderEntity.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntity.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(reminderEntity.getAction()).isEqualTo(action);
		assertThat(reminderEntity.getNote()).isEqualTo(note);
		assertThat(reminderEntity.getReminderDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void toRemindersSuccess() {

		final var reminders = ReminderMapper.toReminders(createReminderEntities());

		assertThat(reminders.get(0).getReminderId()).isEqualTo("reminderId1");
		assertThat(reminders.get(0).getPartyId()).isEqualTo("partyId1");
		assertThat(reminders.get(0).getCaseId()).isEqualTo("caseId1");
		assertThat(reminders.get(0).getCaseType()).isEqualTo("caseType1");
		assertThat(reminders.get(0).getCaseLink()).isEqualTo("caseLink1");
		assertThat(reminders.get(0).getExternalCaseId()).isEqualTo("externalCaseId1");
		assertThat(reminders.get(0).getAction()).isEqualTo("action1");
		assertThat(reminders.get(0).getNote()).isEqualTo("note1");
		assertThat(reminders.get(0).getReminderDate()).isEqualTo(LocalDate.now());

		assertThat(reminders.get(1).getReminderId()).isEqualTo("reminderId2");
		assertThat(reminders.get(1).getPartyId()).isEqualTo("partyId2");
		assertThat(reminders.get(1).getCaseId()).isEqualTo("caseId2");
		assertThat(reminders.get(1).getCaseType()).isEqualTo("caseType2");
		assertThat(reminders.get(1).getCaseLink()).isEqualTo("caseLink2");
		assertThat(reminders.get(1).getExternalCaseId()).isEqualTo("externalCaseId2");
		assertThat(reminders.get(1).getAction()).isEqualTo("action2");
		assertThat(reminders.get(1).getNote()).isEqualTo("note2");
		assertThat(reminders.get(1).getReminderDate()).isEqualTo(LocalDate.now().plusDays(1));
	}

	@Test
	void toMessageSuccess() {

		final var reminder = new Reminder().withCaseLink("caseLink")
			.withCaseId("caseId")
			.withAction("action")
			.withNote("note")
			.withPartyId(randomUUID().toString());
		when(reminderMessagePropertiesMock.getMessage()).thenReturn("message");
		when(reminderMessagePropertiesMock.getEmailMessage()).thenReturn("emailMessage");
		when(reminderMessagePropertiesMock.getSubject()).thenReturn("subject");
		when(reminderMessagePropertiesMock.getSenderEmailName()).thenReturn("email-name");
		when(reminderMessagePropertiesMock.getSenderEmailAddress()).thenReturn("email-address");
		when(reminderMessagePropertiesMock.getSenderSmsName()).thenReturn("sms-name");

		final var message = ReminderMapper.toMessage(reminder, reminderMessagePropertiesMock);

		assertThat(message.getParty().getPartyId().toString()).isEqualTo(reminder.getPartyId());
		assertThat(message.getMessage()).isEqualTo("message");
		// htmlMessage is Base64Encoded
		assertThat(new String(DatatypeConverter.parseBase64Binary((message.getHtmlMessage())))).isEqualTo("emailMessage");
		assertThat(message.getSubject()).isEqualTo("subject");
		assertThat(message.getSender().getEmail().getName()).isEqualTo("email-name");
		assertThat(message.getSender().getEmail().getAddress()).isEqualTo("email-address");
		assertThat(message.getSender().getSms().getName()).isEqualTo("sms-name");
	}

	private List<ReminderEntity> createReminderEntities() {

		final var partyId1 = "partyId1";
		final var partyId2 = "partyId2";
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";
		final var caseId1 = "caseId1";
		final var caseId2 = "caseId2";
		final var caseType1 = "caseType1";
		final var caseType2 = "caseType2";
		final var caseLink1 = "caseLink1";
		final var caseLink2 = "caseLink2";
		final var externalCaseId1 = "externalCaseId1";
		final var externalCaseId2 = "externalCaseId2";
		final var action1 = "action1";
		final var action2 = "action2";
		final var note1 = "note1";
		final var note2 = "note2";
		final var reminderDate1 = LocalDate.now();
		final var reminderDate2 = LocalDate.now().plusDays(1);

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPartyId(partyId1);
		reminderEntity1.setCaseId(caseId1);
		reminderEntity1.setCaseType(caseType1);
		reminderEntity1.setCaseLink(caseLink1);
		reminderEntity1.setExternalCaseId(externalCaseId1);
		reminderEntity1.setAction(action1);
		reminderEntity1.setNote(note1);
		reminderEntity1.setReminderDate(reminderDate1);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPartyId(partyId2);
		reminderEntity2.setCaseId(caseId2);
		reminderEntity2.setCaseType(caseType2);
		reminderEntity2.setCaseLink(caseLink2);
		reminderEntity2.setExternalCaseId(externalCaseId2);
		reminderEntity2.setAction(action2);
		reminderEntity2.setNote(note2);
		reminderEntity2.setReminderDate(reminderDate2);

		return List.of(reminderEntity1, reminderEntity2);
	}
}
