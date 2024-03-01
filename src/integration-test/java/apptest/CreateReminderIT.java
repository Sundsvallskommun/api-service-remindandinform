package apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

@WireMockAppTestSuite(files = "classpath:/CreateReminder/", classes = Application.class)
class CreateReminderIT extends AbstractAppTest {

	@Autowired
	private ReminderRepository reminderRepository;

	@Test
	void test1_createReminder() {
		final String partyId = "81471222-5798-11e9-ae24-57fa13b361e1";

		assertThat(reminderRepository.findByPartyId(partyId).stream().findFirst()).isNotPresent();

		setupCall()
			.withServicePath("/reminders")
			.withHttpMethod(POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("^/reminders/(.*)$"))
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByPartyId(partyId).stream().findFirst()).isPresent();
	}
}
