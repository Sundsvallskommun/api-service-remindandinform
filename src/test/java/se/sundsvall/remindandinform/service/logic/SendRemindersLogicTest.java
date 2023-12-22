package se.sundsvall.remindandinform.service.logic;

import static generated.se.sundsvall.messaging.MessageStatus.SENT;
import static generated.se.sundsvall.messaging.MessageType.MESSAGE;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.se.sundsvall.messaging.DeliveryResult;
import generated.se.sundsvall.messaging.MessageResult;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.integration.messaging.MessagingClient;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

@ExtendWith(MockitoExtension.class)
class SendRemindersLogicTest {

	private static final UUID partyId1 = UUID.randomUUID();
	private static final UUID partyId2 = UUID.randomUUID();
	private static final String reminderId1 = "reminderId1";
	private static final String reminderId2 = "reminderId2";
	private static final String action1 = "action1";
	private static final String action2 = "action2";
	private static final String caseId1 = "caseId1";
	private static final String caseId2 = "caseId2";
	private static final String caseLink1 = "caseLink1";
	private static final String caseLink2 = "caseLink2";

	private static final ReminderEntity reminderEntity1 = new ReminderEntity();
	private static final ReminderEntity reminderEntity2 = new ReminderEntity();

	@Mock
	private ReminderRepository reminderRepositoryMock;

	@Mock
	private ReminderMessageProperties reminderMessagePropertiesMock;

	@Mock
	private MessagingClient messagingClientMock;

	@InjectMocks
	private SendRemindersLogic sendRemindersLogic;

	@BeforeEach
	void setup() {
		// Parameters
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPartyId(partyId1.toString());
		reminderEntity1.setAction(action1);
		reminderEntity1.setCaseId(caseId1);
		reminderEntity1.setCaseLink(caseLink1);

		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPartyId(partyId2.toString());
		reminderEntity2.setAction(action2);
		reminderEntity2.setCaseId(caseId2);
		reminderEntity2.setCaseLink(caseLink2);
	}

	@Test
	void sendRemindersSuccess() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(messagingClientMock.sendMessage(any())).thenReturn(new MessageResult().deliveries(List.of(new DeliveryResult().status(SENT).messageType(MESSAGE))));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(messagingClientMock).sendMessage(any());
	}

	@Test
	void sendRemindersNotFound() {

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(Collections.emptyList());

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(messagingClientMock, never()).sendMessage(any());
	}

	@Test
	void sendRemindersMessagesNotSent() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(messagingClientMock.sendMessage(any())).thenReturn(new MessageResult().messageId(randomUUID()).deliveries(List.of(new DeliveryResult().status(SENT).messageType(MESSAGE))));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(messagingClientMock).sendMessage(any());
	}

	public void setUpProperties() {
		when(reminderMessagePropertiesMock.getMessage()).thenReturn("smsMessage");
		when(reminderMessagePropertiesMock.getEmailMessage()).thenReturn("emailMessage");
		when(reminderMessagePropertiesMock.getSubject()).thenReturn("subject");
		when(reminderMessagePropertiesMock.getSenderEmailName()).thenReturn("email-name");
		when(reminderMessagePropertiesMock.getSenderEmailAddress()).thenReturn("email-address");
		when(reminderMessagePropertiesMock.getSenderSmsName()).thenReturn("sms-name");
	}
}
