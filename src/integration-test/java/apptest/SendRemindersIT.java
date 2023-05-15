package apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;

/**
 * Create remindandinform application tests
 *
 * @see src/test/resources/db/testdata.sql for data setup.
 */

@Sql(scripts = {
	"/db/truncate.sql",
	"/db/testdata.sql"
})
@WireMockAppTestSuite(files = "classpath:/SendReminders/", classes = Application.class)
class SendRemindersIT extends AbstractAppTest {

	@Autowired
	private ReminderRepository reminderRepository;

	@Test
	void test1_sendReminders() {

		assertThat(reminderRepository.findByReminderDateLessThanEqualAndSentFalse(LocalDate.parse("2021-11-25")).stream().toList()).hasSize(3);

		setupCall()
			.withServicePath("/reminders/send")
			.withHttpMethod(POST)
			.withRequest("request.json")
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequestAndVerifyResponse();

		assertThat(reminderRepository.findByReminderDateLessThanEqualAndSentFalse(LocalDate.parse("2021-11-25")).stream().toList()).isEmpty();
	}
}
