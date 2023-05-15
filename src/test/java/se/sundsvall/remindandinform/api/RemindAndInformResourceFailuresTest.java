package se.sundsvall.remindandinform.api;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.zalando.problem.Status.BAD_REQUEST;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;

import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class RemindAndInformResourceFailuresTest {

	@MockBean
	private ReminderService reminderServiceMock;

	@MockBean
	private SendRemindersLogic sendRemindersLogicMock;

	@Autowired
	private WebTestClient webTestClient;

	/**
	 * Create reminders tests:
	 */

	@Test
	void createMissingBody() {

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.createReminder(org.springframework.web.util.UriComponentsBuilder,se.sundsvall.remindandinform.api.model.ReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderEmptyBody() {

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(ReminderRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(
				tuple("action", "must not be null"),
				tuple("createdBy", "must not be blank"),
				tuple("partyId", "not a valid UUID"),
				tuple("reminderDate", "must not be null"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderMissingPartyId() {

		// Arrange
		final var body = ReminderRequest.create()
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now()); // Body with missing partyId.

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("partyId", "not a valid UUID"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsInvalidPartyId() {

		// Arrange
		final var body = ReminderRequest.create()
			.withPartyId("invalid-person-id")
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now()); // Body with not valid partyId.

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("partyId", "not a valid UUID"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsNoteThatExceedsMaxLength() {

		// Arrange
		final var body = ReminderRequest.create()
			.withPartyId(UUID.randomUUID().toString())
			.withAction("action")
			.withNote(repeat("*", 2049))
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now());

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("note", "size must be between 0 and 2048"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsLocalDateOfWrongFormat() throws JSONException {

		// Arrange
		final var body = new JSONObject();
		body.put("partyId", "81471222-5798-11e9-ae24-57fa13b361e");
		body.put("action", "action");
		body.put("caseId", "caseId");
		body.put("caseLink", "caseLink");
		body.put("reminderDate", "2021-13-01");

		// Act
		final var response = webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Wrong format of date");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderMissingBody() {

		// Parameter values
		final var reminderId = "reminderId";

		// Act
		final var response = webTestClient.patch().uri("/reminders/{reminderId}", reminderId)
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Required request body is missing: public org.springframework.http.ResponseEntity<se.sundsvall.remindandinform.api.model.Reminder> se.sundsvall.remindandinform.api.RemindAndInformResource.updateReminder(java.lang.String,se.sundsvall.remindandinform.api.model.UpdateReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderBadReminderId() {

		// Arrange
		final var reminderId = " ";
		final var body = UpdateReminderRequest.create()
			.withPartyId("81471222-5798-11e9-ae24-57fa13b361e")
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withModifiedBy("modifiedBy")
			.withReminderDate(LocalDate.now()); // Body with not valid partyId

		// Act
		final var response = webTestClient.patch().uri("/reminders/{reminderId}", reminderId)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("updateReminder.reminderId", "must not be blank"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsLocalDateOfWrongFormat() throws JSONException {

		// Arrange
		final var reminderId = "reminderId";

		final JSONObject body = new JSONObject();
		body.put("reminderDate", "2021-13-01");

		// Act
		final var response = webTestClient.patch().uri("/reminders/{reminderId}", reminderId)
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Wrong format of date");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsInvalidPartyId() {

		// Arrange
		final var reminderId = "reminderId";
		final var partyId = "invalidPartyId";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var modifiedBy = "modifiedBy";
		final var action = "action";
		final var reminderDate = LocalDate.now();

		final var body = UpdateReminderRequest.create() // Body with invalid partyId
			.withPartyId(partyId)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withModifiedBy(modifiedBy)
			.withAction(action)
			.withReminderDate(reminderDate);

		// Act
		final var response = webTestClient.patch().uri("/reminders/{reminderId}", reminderId)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("partyId", "not a valid UUID"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void deleteReminderBadReminderId() {

		// Arrange
		final var reminderId = " ";

		// Act
		final var response = webTestClient.delete().uri("/reminders/{reminderId}", reminderId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("deleteReminder.reminderId", "must not be blank"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersWrongFormatOfDate() throws JSONException {

		// Arrange
		final var body = new JSONObject();
		body.put("reminderDate", "2021-13-01");

		// Act
		final var response = webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Wrong format of date");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingBody() {

		// Act
		final var response = webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(Problem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getDetail()).isEqualTo(
			"Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.sendReminders(se.sundsvall.remindandinform.api.model.SendRemindersRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingReminderDate() {

		// Act
		final var response = webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(SendRemindersRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("reminderDate", "must not be null"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getReminderMissingReminderId() {

		// Arrange
		final var reminderId = " ";

		// Act
		final var response = webTestClient.get().uri("/reminders/{reminderId}", reminderId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getReminderByReminderId.reminderId", "must not be blank"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getRemindersInvalidPartyId() {

		// Arrange
		final var partyId = "not-valid-party-id";

		// Act
		final var response = webTestClient.get().uri("/reminders/parties/{partyId}", partyId)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response.getTitle()).isEqualTo("Constraint Violation");
		assertThat(response.getStatus()).isEqualTo(BAD_REQUEST);
		assertThat(response.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple("getRemindersByPartyId.partyId", "not a valid UUID"));

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}
}
