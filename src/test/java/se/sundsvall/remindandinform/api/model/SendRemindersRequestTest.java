package se.sundsvall.remindandinform.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.code.beanmatchers.BeanMatchers;

class SendRemindersRequestTest {

	@BeforeEach
	void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(SendRemindersRequest.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var reminderDate = LocalDate.now();

		final var sendRemindersRequest = SendRemindersRequest.create()
			.withReminderDate(reminderDate);

		assertThat(sendRemindersRequest.getReminderDate()).isEqualTo(reminderDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(SendRemindersRequest.create()).hasAllNullFieldsOrProperties();
	}
}
