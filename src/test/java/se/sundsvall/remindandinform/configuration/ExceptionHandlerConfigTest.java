package se.sundsvall.remindandinform.configuration;

import java.time.format.DateTimeParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(classes = ExceptionHandlerConfig.class)
class ExceptionHandlerConfigTest {
	@Autowired
	private ExceptionHandlerConfig.ControllerExceptionHandler controllerExceptionHandler;

	@Test
	void testDateTimeParseExceptionIsParsed() {
		final var cause = new DateTimeParseException("test", "2022-13-01", 0);
		final var exception = new HttpMessageNotReadableException("Failed to read request", cause, null);

		final var response = controllerExceptionHandler.handleHttpMessageNotReadable(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getTitle()).isEqualTo("Wrong format of date");
		assertThat(response.getBody().getStatus()).isEqualTo(BAD_REQUEST);
	}

	@Test
	void testNonDateTimeParseExceptionIsHandled() {
		final var exception = new HttpMessageNotReadableException("Some other error", (Throwable) null, null);

		final var response = controllerExceptionHandler.handleHttpMessageNotReadable(exception);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getTitle()).isEqualTo(BAD_REQUEST.getReasonPhrase());
		assertThat(response.getBody().getStatus()).isEqualTo(BAD_REQUEST);
	}
}
