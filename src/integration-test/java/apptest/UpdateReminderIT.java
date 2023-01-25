package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpStatus.OK;

/**
 * Create remindandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@WireMockAppTestSuite(
		files = "classpath:/UpdateReminder/",
		classes = Application.class
)
@Sql(scripts = {
		"/db/truncate.sql",
		"/db/testdata.sql"
})
class UpdateReminderIT extends AbstractAppTest {

	@Autowired
	private ReminderRepository reminderRepository;

	@Test
	void test1_updateReminder() {

		String reminderId = "R-fbfbd90c-4c47-11ec-81d3-0242ac130004";

		setupCall()
			.withServicePath("/reminders/" + reminderId)
			.withHttpMethod(PATCH)
			.withRequest("request.json")
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByReminderId(reminderId).stream().findFirst()).isPresent();
	}
}
