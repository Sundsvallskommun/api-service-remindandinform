package apptest;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

/**
 * Create reminderandinform application tests
 * 
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@WireMockAppTestSuite(
		files = "classpath:/ReadReminders/",
		classes = Application.class
)
@Sql(scripts = {
		"/db/truncate.sql",
		"/db/testdata.sql"
})
class ReadRemindersIT extends AbstractAppTest {

	@Test
	void test1_getRemindersByPartyId() { //NOSONAR
		String partyId = "fbfbd90c-4c47-11ec-81d3-0242ac130001";

		setupCall()
				.withServicePath("/reminders/parties/" + partyId)
				.withHttpMethod(GET)
				.withExpectedResponseStatus(OK)
				.withExpectedResponse("response.json")
				.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_getRemindersByReminderId() { //NOSONAR
		String reminderId = "R-fbfbd90c-4c47-11ec-81d3-0242ac130002";

		setupCall()
				.withServicePath("/reminders/" + reminderId)
				.withHttpMethod(GET)
				.withExpectedResponseStatus(OK)
				.withExpectedResponse("response.json")
				.sendRequestAndVerifyResponse();
	}

}
