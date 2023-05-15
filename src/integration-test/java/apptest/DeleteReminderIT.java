package apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

/**
 * Create reminderandinform application tests
 *
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@Sql(scripts = {
	"/db/truncate.sql",
	"/db/testdata.sql"
})
@WireMockAppTestSuite(files = "classpath:/DeleteReminder/", classes = Application.class)
class DeleteReminderIT extends AbstractAppTest {

	@Autowired
	private ReminderRepository reminderRepository;

	@Test
	void test1_deleteReminder() {
		final String reminderId = "R-fbfbd90c-4c47-11ec-81d3-0242ac130006";

		assertThat(reminderRepository.findByReminderId(reminderId).stream().findFirst()).isPresent();

		setupCall()
			.withServicePath("/reminders/" + reminderId)
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByReminderId(reminderId).stream().findFirst()).isNotPresent();
	}
}
