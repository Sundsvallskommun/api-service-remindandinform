package se.sundsvall.remindandinform.integration.messaging;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration.CLIENT_ID;

import generated.se.sundsvall.messaging.MessageRequest;
import generated.se.sundsvall.messaging.MessageResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration;

@FeignClient(name = CLIENT_ID, url = "${integration.messaging.url}", configuration = MessagingConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface MessagingClient {

	/**
	 * Send messages as email or SMS to list of recipients.
	 *
	 * @param  municipalityId                       the municipality ID
	 * @param  messageRequest                       with a list of messages
	 * @return                                      messageStatusResponse containing status for transaction.
	 * @throws org.zalando.problem.ThrowableProblem when called service responds with error code
	 */
	@PostMapping(path = "/{municipalityId}/messages?async=true", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	MessageResult sendMessage(@PathVariable String municipalityId, @RequestBody MessageRequest messageRequest);

}
