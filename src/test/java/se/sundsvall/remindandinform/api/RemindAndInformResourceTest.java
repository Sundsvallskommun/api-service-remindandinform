package se.sundsvall.remindandinform.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.api.model.Reminder;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class RemindAndInformResourceTest {

	@MockBean
	private ReminderService reminderServiceMock;

	@MockBean
	private SendRemindersLogic sendRemindersLogicMock;

	@Autowired
	private WebTestClient webTestClient;

	@LocalServerPort
	private int port;

	@Test
	void getRemindersByPartyId() {

		// Arrange
		final var partyId = UUID.randomUUID().toString();
		final var reminderId = "reminderId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var reminder = Reminder.create()
			.withPartyId(partyId)
			.withReminderId(reminderId)
			.withAction(action)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withReminderDate(reminderDate);

		when(reminderServiceMock.findRemindersByPartyId(partyId)).thenReturn(List.of(reminder));

		// Act
		final var response = webTestClient.get().uri("/reminders/parties/{partyId}", partyId)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON_VALUE)
			.expectBodyList(Reminder.class).hasSize(1);

		// Assert
		assertThat(response).isNotNull();
		verify(reminderServiceMock).findRemindersByPartyId(partyId);
	}

	@Test
	void getReminderByReminderId() {

		// Arrange
		final var partyId = UUID.randomUUID().toString();
		final var reminderId = "reminderId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var reminder = Reminder.create()
			.withPartyId(partyId)
			.withReminderId(reminderId)
			.withAction(action)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withReminderDate(reminderDate);

		when(reminderServiceMock.findReminderByReminderId(reminderId)).thenReturn(reminder);

		// Act
		final var response = webTestClient.get().uri("/reminders/{reminderId}", reminderId)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON_VALUE)
			.expectBody(Reminder.class)
			.returnResult();

		// Assert
		assertThat(response).isNotNull();
		verify(reminderServiceMock).findReminderByReminderId(reminderId);
	}

	@Test
	void postRemindersByReminderDate() {

		// Arrange
		final var sendRemindersRequest = SendRemindersRequest.create().withReminderDate(LocalDate.now());

		doNothing().when(sendRemindersLogicMock).sendReminders(sendRemindersRequest.getReminderDate());

		// Act
		final var response = webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(sendRemindersRequest)
			.exchange()
			.expectStatus().isNoContent();

		// Assert
		assertThat(response).isNotNull();
		verify(sendRemindersLogicMock).sendReminders(LocalDate.now());
	}

	@Test
	void updateReminder() {

		// Arrange
		final var reminderId = "reminderId";
		final var body = UpdateReminderRequest.create()
			.withPartyId("81471222-5798-11e9-ae24-57fa13b361e")
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withModifiedBy("modifiedBy")
			.withReminderDate(LocalDate.now());

		when(reminderServiceMock.updateReminder(argThat(reminderRequest -> "81471222-5798-11e9-ae24-57fa13b361e".equals(reminderRequest.getPartyId())), argThat(id -> reminderId.equals(id)))).thenReturn(Reminder.create());

		// Act
		final var response = webTestClient.patch().uri("/reminders/{reminderId}", reminderId)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON_VALUE)
			.expectBody(Reminder.class)
			.returnResult();

		// Assert
		assertThat(response).isNotNull();
		verify(reminderServiceMock).updateReminder(argThat(reminderRequest -> "81471222-5798-11e9-ae24-57fa13b361e".equals(reminderRequest.getPartyId())), anyString());
	}

	@Test
	void deleteReminder() {

		// Arrange
		final var reminderId = "reminderId";
		doNothing().when(reminderServiceMock).deleteReminderByReminderId(reminderId);

		// Act
		webTestClient.delete().uri("/reminders/{reminderId}", reminderId)
			.exchange()
			.expectStatus().isNoContent();

		// Assert
		verify(reminderServiceMock).deleteReminderByReminderId(reminderId);
	}

	@Test
	void createReminder() {

		// Arrange
		final var partyId = "81471222-5798-11e9-ae24-57fa13b361e";
		final var reminderId = "reminderId";

		final var expectedLocationURL = "http://localhost:".concat(String.valueOf(port)).concat("/").concat(reminderId);
		final var body = ReminderRequest.create()
			.withPartyId(partyId)
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now(ZoneId.systemDefault()));

		when(reminderServiceMock.createReminder(argThat(reminderRequest -> partyId.equals(reminderRequest.getPartyId())))).thenReturn(Reminder.create().withReminderId(reminderId));

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL_VALUE)
			.expectHeader().value(LOCATION, is(expectedLocationURL));

		// Assert
		assertThat(response).isNotNull();
		verify(reminderServiceMock).createReminder(argThat(reminderRequest -> "81471222-5798-11e9-ae24-57fa13b361e".equals(reminderRequest.getPartyId())));
	}
}
