package se.sundsvall.remindandinform.configuration;

import static org.zalando.problem.Status.BAD_REQUEST;

import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zalando.problem.Problem;

@Configuration
public class ExceptionHandlerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);
	private static final String LOG_MESSAGE = "Mapping exception into Problem";
	public static final String TYPE_VALUE = "https://zalando.github.io/problem/constraint-violation";
	public static final URI TYPE = URI.create(TYPE_VALUE);

	@ControllerAdvice
	public static class ControllerExceptionHandler {
		@ExceptionHandler
		@ResponseBody
		public ResponseEntity<Problem> handleDateParseException(DateTimeParseException exception) {
			LOGGER.info(LOG_MESSAGE, exception);

			final var errorResponse = Problem.builder()
				.withType(TYPE)
				.withStatus(BAD_REQUEST)
				.withDetail(extractMessage(exception))
				.withTitle("Wrong format of date")
				.build();
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(errorResponse);
		}

		private String extractMessage(Exception e) {
			return Optional.ofNullable(e.getMessage()).orElse(String.valueOf(e));
		}
	}
}
