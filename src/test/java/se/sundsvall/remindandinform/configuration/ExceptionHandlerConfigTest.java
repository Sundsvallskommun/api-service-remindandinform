package se.sundsvall.remindandinform.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zalando.problem.Status;

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
