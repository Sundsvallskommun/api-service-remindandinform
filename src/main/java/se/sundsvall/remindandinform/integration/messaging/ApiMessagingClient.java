package se.sundsvall.remindandinform.integration.messaging;

import generated.se.sundsvall.messaging.MessageRequest;
import generated.se.sundsvall.messaging.MessageStatusResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.remindandinform.integration.messaging.configuration.MessagingConfiguration.CLIENT_REGISTRATION_ID;


@FeignClient(name = CLIENT_REGISTRATION_ID, url = "${integration.messaging.url}", configuration = MessagingConfiguration.class)
@CircuitBreaker(name = CLIENT_REGISTRATION_ID)
public interface ApiMessagingClient {

	/**
	 * Send messages as email or SMS to list of recipients.
	 * 
	 * @param messageRequest with a list of messages.
	 * @return a MessageStatusResponse
	 * @throws org.zalando.problem.ThrowableProblem
	 */
	@PostMapping(path = "/messages", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	MessageStatusResponse sendMessage(@RequestBody MessageRequest messageRequest) ;
}