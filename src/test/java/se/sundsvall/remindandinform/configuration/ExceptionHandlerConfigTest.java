package se.sundsvall.remindandinform.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zalando.problem.Status;

import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@SpringBootTest(classes = ExceptionHandlerConfig.class)
class ExceptionHandlerConfigTest {
    @Autowired
    private ExceptionHandlerConfig.ControllerExceptionHandler controllerExceptionHandler;
    @Test
    void test_dateTimeParseExceptionIsParsed() {
        var response = controllerExceptionHandler.handleDateParseException(new DateTimeParseException("test", "2022-13-01", 0));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Wrong format of date");
        assertThat(response.getBody().getStatus()).isEqualTo(Status.BAD_REQUEST);
    }
}
