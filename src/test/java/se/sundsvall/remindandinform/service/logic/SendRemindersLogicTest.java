package se.sundsvall.remindandinform.service.logic;

import generated.se.sundsvall.messaging.MessageStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;
import se.sundsvall.remindandinform.integration.messaging.ApiMessagingClient;
import se.sundsvall.remindandinform.service.mapper.configuration.ReminderMessageProperties;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static generated.se.sundsvall.messaging.MessageStatusResponse.StatusEnum.FAILED;
import static generated.se.sundsvall.messaging.MessageStatusResponse.StatusEnum.SENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendRemindersLogicTest {

	private static final String partyId1 = "partyId1";
	private static final String partyId2 = "partyId2";
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
	private ApiMessagingClient apiMessagingClientMock;

	@InjectMocks
	private SendRemindersLogic sendRemindersLogic;



	@BeforeEach
	void setup() {
		// Parameters
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPartyId(partyId1);
		reminderEntity1.setAction(action1);
		reminderEntity1.setCaseId(caseId1);
		reminderEntity1.setCaseLink(caseLink1);

		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPartyId(partyId2);
		reminderEntity2.setAction(action2);
		reminderEntity2.setCaseId(caseId2);
		reminderEntity2.setCaseLink(caseLink2);
	}

	@Test
	void sendRemindersSuccess() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(apiMessagingClientMock.sendMessage(any())).thenReturn(new MessageStatusResponse().status(SENT));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(apiMessagingClientMock).sendMessage(any());
	}

	@Test
	void sendRemindersNotFound() {

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(Collections.emptyList());

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(apiMessagingClientMock, never()).sendMessage(any());
	}

	@Test
	void sendRemindersMessagesNotSent() {

		setUpProperties();

		when(reminderRepositoryMock.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now())).thenReturn(List.of(reminderEntity1, reminderEntity2));

		when(apiMessagingClientMock.sendMessage(any())).thenReturn(new MessageStatusResponse().status(FAILED).messageId("messageId"));

		sendRemindersLogic.sendReminders();

		verify(reminderRepositoryMock, times(2)).findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());
		verify(apiMessagingClientMock).sendMessage(any());
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
