package se.sundsvall.remindandinform.api;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

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

import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.SendRemindersRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.service.ReminderService;
import se.sundsvall.remindandinform.service.logic.SendRemindersLogic;

@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
class RemindAndInformResourceFailuresTest {

	private static final String STATUS = "$.status";

	private static final String DETAIL = "$.detail";

	private static final String TITLE = "$.title";

	private static final String NOT_A_VALID_UUID = "not a valid UUID";

	private static final String REMINDER_DATE = "reminderDate";

	private static final String MUST_NOT_BE_NULL = "must not be null";

	private static final String MUST_NOT_BE_BLANK = "must not be blank";

	private static final String PARTY_ID = "partyId";

	private static final String CREATED_BY = "createdBy";

	private static final String CONSTRAINT_VIOLATION = "Constraint Violation";

	private static final String ACTION = "action";

	private static final String CASE_ID = "caseId";

	private static final String PARTY_ID_VALUE = "81471222-5798-11e9-ae24-57fa13b361e";

	private static final String CASE_LINK = "caseLink";

	private static final String INVALID = "invalid-person-id";

	private static final String SIZE_MUST_BE_BETWEEN_0_AND_2048 = "size must be between 0 and 2048";

	private static final String WRONG_FORMAT_OF_DATE = "Wrong format of date";

	private static final String REMINDER_DATE_DATE = "2021-13-01";

	private static final String MODIFIED_BY = "modifiedBy";

	private static final String REMINDER_ID = "reminderId";

	private static final String MUNICIPALITY_ID = "2281";

	private static final String PATH = "/" + MUNICIPALITY_ID + "/reminders";

	@MockBean
	private ReminderService reminderServiceMock;

	@MockBean
	private SendRemindersLogic sendRemindersLogicMock;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void createMissingBody() {
		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo("Bad Request")
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo(
				"Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.createReminder(java.lang.String,se.sundsvall.remindandinform.api.model.ReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderEmptyBody() {
		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.bodyValue(ReminderRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo(ACTION)
			.jsonPath("$.violations[0].message").isEqualTo(MUST_NOT_BE_NULL)
			.jsonPath("$.violations[1].field").isEqualTo(CREATED_BY)
			.jsonPath("$.violations[1].message").isEqualTo(MUST_NOT_BE_BLANK)
			.jsonPath("$.violations[2].field").isEqualTo(PARTY_ID)
			.jsonPath("$.violations[2].message").isEqualTo(NOT_A_VALID_UUID)
			.jsonPath("$.violations[3].field").isEqualTo(REMINDER_DATE)
			.jsonPath("$.violations[3].message").isEqualTo(MUST_NOT_BE_NULL);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderMissingPartyId() {
		final var body = ReminderRequest.create()
			.withAction(ACTION)
			.withCaseId(CASE_ID)
			.withCaseLink(CASE_LINK)
			.withCreatedBy(CREATED_BY)
			.withReminderDate(LocalDate.now()); // Body with missing partyId.

		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo(PARTY_ID)
			.jsonPath("$.violations[0].message").isEqualTo(NOT_A_VALID_UUID);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsInvalidPartyId() {
		final var body = ReminderRequest.create()
			.withPartyId(INVALID)
			.withAction(ACTION)
			.withCaseId(CASE_ID)
			.withCaseLink(CASE_LINK)
			.withCreatedBy(CREATED_BY)
			.withReminderDate(LocalDate.now());

		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo(PARTY_ID)
			.jsonPath("$.violations[0].message").isEqualTo(NOT_A_VALID_UUID);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsNoteThatExceedsMaxLength() {
		final var body = ReminderRequest.create()
			.withPartyId(UUID.randomUUID().toString())
			.withAction(ACTION)
			.withNote(repeat("*", 2049))
			.withCaseId(CASE_ID)
			.withCaseLink(CASE_LINK)
			.withCreatedBy(CREATED_BY)
			.withReminderDate(LocalDate.now());

		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("note")
			.jsonPath("$.violations[0].message").isEqualTo(SIZE_MUST_BE_BETWEEN_0_AND_2048);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void createReminderContainsLocalDateOfWrongFormat() throws JSONException {
		final var body = new JSONObject();
		body.put(PARTY_ID, PARTY_ID_VALUE);
		body.put(ACTION, ACTION);
		body.put(CASE_ID, CASE_ID);
		body.put(CASE_LINK, CASE_LINK);
		body.put(REMINDER_DATE, REMINDER_DATE_DATE);

		webTestClient.post().uri(PATH)
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(WRONG_FORMAT_OF_DATE)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderMissingBody() {
		webTestClient.patch().uri(PATH + "/{reminderId}", REMINDER_ID)
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo("Bad Request")
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo(
				"Required request body is missing: public org.springframework.http.ResponseEntity<se.sundsvall.remindandinform.api.model.Reminder> se.sundsvall.remindandinform.api.RemindAndInformResource.updateReminder(java.lang.String,java.lang.String,se.sundsvall.remindandinform.api.model.UpdateReminderRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderBadReminderId() {
		final var body = UpdateReminderRequest.create()
			.withPartyId(PARTY_ID_VALUE)
			.withAction(ACTION)
			.withCaseId(CASE_ID)
			.withCaseLink(CASE_LINK)
			.withModifiedBy(MODIFIED_BY)
			.withReminderDate(LocalDate.now());

		webTestClient.patch().uri(PATH + "/{reminderId}", " ")
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("updateReminder.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo(MUST_NOT_BE_BLANK);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsLocalDateOfWrongFormat() throws JSONException {
		final JSONObject body = new JSONObject();
		body.put(REMINDER_DATE, REMINDER_DATE_DATE);

		webTestClient.patch().uri(PATH + "/{reminderId}", REMINDER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(WRONG_FORMAT_OF_DATE)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void updateReminderContainsInvalidPartyId() {
		final var body = UpdateReminderRequest.create()
			.withPartyId(INVALID)
			.withCaseId(CASE_ID)
			.withCaseLink(CASE_LINK)
			.withModifiedBy(MODIFIED_BY)
			.withAction(ACTION)
			.withReminderDate(LocalDate.now());

		webTestClient.patch().uri(PATH + "/{reminderId}", REMINDER_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(body)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo(PARTY_ID)
			.jsonPath("$.violations[0].message").isEqualTo(NOT_A_VALID_UUID);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void deleteReminderBadReminderId() {
		webTestClient.delete().uri(PATH + "/{reminderId}", " ")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("deleteReminder.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo(MUST_NOT_BE_BLANK);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersWrongFormatOfDate() throws JSONException {
		final var body = new JSONObject();
		body.put(REMINDER_DATE, REMINDER_DATE_DATE);

		webTestClient.post().uri(PATH + "/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(body.toString())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(WRONG_FORMAT_OF_DATE)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo("Text '2021-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingBody() {
		webTestClient.post().uri(PATH + "/send")
			.contentType(APPLICATION_JSON)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(HttpStatus.BAD_REQUEST.getReasonPhrase())
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath(DETAIL).isEqualTo(
				"Required request body is missing: public org.springframework.http.ResponseEntity<java.lang.Void> se.sundsvall.remindandinform.api.RemindAndInformResource.sendReminders(java.lang.String,se.sundsvall.remindandinform.api.model.SendRemindersRequest)");

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void sendRemindersMissingReminderDate() {
		webTestClient.post().uri(PATH + "/send")
			.contentType(APPLICATION_JSON)
			.bodyValue(SendRemindersRequest.create())
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo(REMINDER_DATE)
			.jsonPath("$.violations[0].message").isEqualTo(MUST_NOT_BE_NULL);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getReminderMissingReminderId() {
		webTestClient.get().uri(PATH + "/{reminderId}", " ")
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getReminderByReminderId.reminderId")
			.jsonPath("$.violations[0].message").isEqualTo(MUST_NOT_BE_BLANK);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

	@Test
	void getRemindersInvalidPartyId() {
		webTestClient.get().uri(PATH + "/parties/{partyId}", INVALID)
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON_VALUE)
			.expectBody()
			.jsonPath(TITLE).isEqualTo(CONSTRAINT_VIOLATION)
			.jsonPath(STATUS).isEqualTo(HttpStatus.BAD_REQUEST.value())
			.jsonPath("$.violations[0].field").isEqualTo("getRemindersByPartyId.partyId")
			.jsonPath("$.violations[0].message").isEqualTo(NOT_A_VALID_UUID);

		verifyNoInteractions(reminderServiceMock, sendRemindersLogicMock);
	}

}
