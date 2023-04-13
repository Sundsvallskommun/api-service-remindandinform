package se.sundsvall.remindandinform.integration.messaging;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration.CLIENT_REGISTRATION_ID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import generated.se.sundsvall.messaging.MessageRequest;
import generated.se.sundsvall.messaging.MessageResult;
import se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration;

@FeignClient(name = CLIENT_REGISTRATION_ID, url = "${integration.messaging.url}", configuration = MessagingConfiguration.class)
public interface ApiMessagingClient {

	/**
	 * Send messages as email or SMS to list of recipients.
	 *
	 * @param messageRequest with a list of messages
	 * @return messageStatusResponse containing status for transaction
	 * @throws org.zalando.problem.ThrowableProblem when called service responds with error code
	 */
	@PostMapping(path = "/messages?async=true", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	MessageResult sendMessage(@RequestBody MessageRequest messageRequest);
}
