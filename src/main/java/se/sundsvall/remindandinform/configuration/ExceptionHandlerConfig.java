package se.sundsvall.remindandinform.configuration;

import java.net.URI;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import se.sundsvall.dept44.problem.Problem;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Configuration
public class ExceptionHandlerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);
	private static final String LOG_MESSAGE = "Mapping exception into Problem";
	public static final String TYPE_VALUE = "about:blank";
	public static final URI TYPE = URI.create(TYPE_VALUE);

	@ControllerAdvice
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class ControllerExceptionHandler {
		@ExceptionHandler
		@ResponseBody
		public ResponseEntity<Problem> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
			final var dateTimeParseException = findCause(exception, DateTimeParseException.class);
			if (dateTimeParseException != null) {
				return handleDateParseException(dateTimeParseException);
			}
			LOGGER.info(LOG_MESSAGE, exception);

			final var errorResponse = Problem.builder()
				.withType(TYPE)
				.withStatus(BAD_REQUEST)
				.withDetail(extractMessage(exception))
				.withTitle(BAD_REQUEST.getReasonPhrase())
				.build();
			return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_PROBLEM_JSON).body(errorResponse);
		}

		private ResponseEntity<Problem> handleDateParseException(DateTimeParseException exception) {
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

		@SuppressWarnings("unchecked")
		private <T extends Throwable> T findCause(Throwable throwable, Class<T> type) {
			var cause = throwable;
			while (cause != null) {
				if (type.isInstance(cause)) {
					return (T) cause;
				}
				cause = cause.getCause();
			}
			return null;
		}
	}
}
