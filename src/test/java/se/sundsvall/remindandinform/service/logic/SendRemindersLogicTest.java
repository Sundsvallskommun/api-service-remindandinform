package se.sundsvall.remindandinform.service.logic;

import static generated.se.sundsvall.messaging.MessageStatus.SENT;
import static generated.se.sundsvall.messaging.MessageType.MESSAGE;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import generated.se.sundsvall.messaging.DeliveryResult;
import generated.se.sundsvall.messaging.MessageResult;
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
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.integration.messaging.MessagingClient;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

@ExtendWith(MockitoExtension.class)
class SendRemindersLogicTest {

	private static final UUID PARTY_ID_1 = UUID.randomUUID();

	private static final UUID PARTY_ID_2 = UUID.randomUUID();

	private static final String REMINDER_ID_1 = "reminderId1";

	private static final String REMINDER_ID_2 = "reminderId2";

	private static final String ACTION_1 = "action1";

	private static final String ACTION_2 = "action2";

	private static final String CASE_ID_1 = "caseId1";

	private static final String CASE_ID_2 = "caseId2";

	private static final String CASE_LINK_1 = "caseLink1";

	private static final String CASE_LINK_2 = "caseLink2";

	private static final String MUNICIPALITY_ID = "municipalityId";

	private static final ReminderEntity REMINDER_ENTITY_1 = new ReminderEntity();

	private static final ReminderEntity REMINDER_ENTITY_2 = new ReminderEntity();

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
		REMINDER_ENTITY_1.setReminderId(REMINDER_ID_1);
		REMINDER_ENTITY_1.setPartyId(PARTY_ID_1.toString());
		REMINDER_ENTITY_1.setAction(ACTION_1);
		REMINDER_ENTITY_1.setCaseId(CASE_ID_1);
		REMINDER_ENTITY_1.setCaseLink(CASE_LINK_1);

		REMINDER_ENTITY_2.setReminderId(REMINDER_ID_2);
		REMINDER_ENTITY_2.setPartyId(PARTY_ID_2.toString());
		REMINDER_ENTITY_2.setAction(ACTION_2);
		REMINDER_ENTITY_2.setCaseId(CASE_ID_2);
		REMINDER_ENTITY_2.setCaseLink(CASE_LINK_2);
	}

	@Test
	void sendRemindersSuccess() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID)).thenReturn(List.of(REMINDER_ENTITY_1, REMINDER_ENTITY_2));
		when(reminderMessagePropertiesMock.getMunicipalityIds()).thenReturn(List.of(MUNICIPALITY_ID));

		when(messagingClientMock.sendMessage(eq(MUNICIPALITY_ID), any())).thenReturn(new MessageResult().deliveries(List.of(new DeliveryResult().status(SENT).messageType(MESSAGE))));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID);
		verify(messagingClientMock).sendMessage(eq(MUNICIPALITY_ID), any());
	}

	@Test
	void sendRemindersNotFound() {

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID)).thenReturn(Collections.emptyList());
		when(reminderMessagePropertiesMock.getMunicipalityIds()).thenReturn(List.of(MUNICIPALITY_ID));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID);
		verify(messagingClientMock, never()).sendMessage(eq(MUNICIPALITY_ID), any());
	}

	@Test
	void sendRemindersMessagesNotSent() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID)).thenReturn(List.of(REMINDER_ENTITY_1, REMINDER_ENTITY_2));

		when(reminderMessagePropertiesMock.getMunicipalityIds()).thenReturn(List.of(MUNICIPALITY_ID));

		when(messagingClientMock.sendMessage(eq(MUNICIPALITY_ID), any())).thenReturn(new MessageResult().messageId(randomUUID()).deliveries(List.of(new DeliveryResult().status(SENT).messageType(MESSAGE))));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalseAndMunicipalityId(LocalDate.now(), MUNICIPALITY_ID);
		verify(messagingClientMock).sendMessage(eq(MUNICIPALITY_ID), any());
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
