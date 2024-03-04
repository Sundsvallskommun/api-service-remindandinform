package se.sundsvall.remindandinform.api;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;
import static org.zalando.problem.Status.BAD_REQUEST;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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

	@Test
	void createMissingBody() {
		webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Bad Request")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.createReminder(se.sundsvall.remindandinform.api.model.ReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderEmptyBody() {
		 webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(ReminderRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("action")
			.jsonPath("$.violations[0].message").isEqualTo("must not be null")
		    .jsonPath("$.violations[1].field").isEqualTo("createdBy")
			.jsonPath("$.violations[1].message").isEqualTo("must not be blank")
			.jsonPath("$.violations[2].field").isEqualTo("partyId")
			.jsonPath("$.violations[2].message").isEqualTo("not a valid UUID")
			.jsonPath("$.violations[3].field").isEqualTo("reminderDate")
			.jsonPath("$.violations[3].message").isEqualTo("must not be null");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderMissingPartyId() {
		final var body = ReminderRequest.create()
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now()); // Body with missing partyId.

		webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsInvalidPartyId() {
		final var body = ReminderRequest.create()
			.withPartyId("invalid-person-id")
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now());

		webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsNoteThatExceedsMaxLength() {
		final var body = ReminderRequest.create()
			.withPartyId(UUID.randomUUID().toString())
			.withAction("action")
			.withNote(repeat("*", 2049))
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withCreatedBy("createdBy")
			.withReminderDate(LocalDate.now());

		webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("note")
			.jsonPath("$.violations[0].message").isEqualTo("size must be between 0 and 2048");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsLocalDateOfWrongFormat() throws JSONException {
		final var body = new JSONObject();
		body.put("partyId", "81471222-5798-11e9-ae24-57fa13b361e");
		body.put("action", "action");
		body.put("caseId", "caseId");
		body.put("caseLink", "caseLink");
		body.put("reminderDate", "2021-13-01");

		webTestClient.post().uri("/reminders")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Wrong format of date")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderMissingBody() {
		webTestClient.patch().uri("/reminders/{reminderId}", "reminderId")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Bad Request")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Required request body is missing: public org.springframework.http.ResponseEntity<se.sundsvall.remindandinform.api.model.Reminder> se.sundsvall.remindandinform.api.RemindAndInformResource.updateReminder(java.lang.String,se.sundsvall.remindandinform.api.model.UpdateReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderBadReminderId() {
		final var body = UpdateReminderRequest.create()
			.withPartyId("81471222-5798-11e9-ae24-57fa13b361e")
			.withAction("action")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withModifiedBy("modifiedBy")
			.withReminderDate(LocalDate.now());

		webTestClient.patch().uri("/reminders/{reminderId}", " ")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("updateReminder.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo("must not be blank");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsLocalDateOfWrongFormat() throws JSONException {
		final JSONObject body = new JSONObject();
		body.put("reminderDate", "2021-13-01");

		webTestClient.patch().uri("/reminders/{reminderId}", "reminderId")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Wrong format of date")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsInvalidPartyId() {
		final var body = UpdateReminderRequest.create()
			.withPartyId("invalidPartyId")
			.withCaseId("caseId")
			.withCaseLink("caseLink")
			.withModifiedBy("modifiedBy")
			.withAction("action")
			.withReminderDate(LocalDate.now());

		webTestClient.patch().uri("/reminders/{reminderId}", "reminderId")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void deleteReminderBadReminderId() {
		webTestClient.delete().uri("/reminders/{reminderId}", " ")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("deleteReminder.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo("must not be blank");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersWrongFormatOfDate() throws JSONException {
		final var body = new JSONObject();
		body.put("reminderDate", "2021-13-01");

	    webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Wrong format of date")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingBody() {
		webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Bad Request")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.detail").isEqualTo("Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.sendReminders(se.sundsvall.remindandinform.api.model.SendRemindersRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingReminderDate() {
		webTestClient.post().uri("/reminders/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(SendRemindersRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("reminderDate")
			.jsonPath("$.violations[0].message").isEqualTo("must not be null");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getReminderMissingReminderId() {
		webTestClient.get().uri("/reminders/{reminderId}", " ")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getReminderByReminderId.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo("must not be blank");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getRemindersInvalidPartyId() {
		 webTestClient.get().uri("/reminders/parties/{partyId}", "not-a-valid-party-id")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath("$.title").isEqualTo("Constraint Violation")
			.jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getRemindersByPartyId.partyId")
			.jsonPath("$.violations[0].message").isEqualTo("not a valid UUID");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}
}
