package apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.remindandinform.Application;

/**
 * Create reminderandinform application tests
 *
 * @see src/test/resources/db/testdata.sql for data setup.
 */
@Sql(scripts = {
	"/db/truncate.sql",
	"/db/testdata.sql"
})
@WireMockAppTestSuite(files = "classpath:/ReadReminders/", classes = Application.class)
class ReadRemindersIT extends AbstractAppTest {

	@Test
	void test1_getRemindersByPartyId() { // NOSONAR
		final String partyId = "fbfbd90c-4c47-11ec-81d3-0242ac130001";
		final String municipalityId = "2281";

		setupCall()
			.withServicePath("/" + municipalityId + "/reminders/parties/" + partyId)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test2_getRemindersByReminderId() { // NOSONAR
		final String reminderId = "R-fbfbd90c-4c47-11ec-81d3-0242ac130002";
		final String municipalityId = "2281";

		setupCall()
			.withServicePath("/" + municipalityId + "/reminders/" + reminderId)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse("response.json")
			.sendRequestAndVerifyResponse();
	}

}
