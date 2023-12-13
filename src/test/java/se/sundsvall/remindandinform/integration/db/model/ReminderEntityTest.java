package se.sundsvall.remindandinform.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ReminderEntityTest {

	@BeforeAll
	static void setup() {
		registerValueGenerator(() -> OffsetDateTime.now().plusDays(new Random().nextInt()), OffsetDateTime.class);
		registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(ReminderEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testOnCreate() {
		final ReminderEntity entity = new ReminderEntity();
		entity.onCreate();

		assertThat(entity.getCreated()).isCloseTo(OffsetDateTime.now(), within(1, SECONDS));
		assertThat(entity).hasAllNullFieldsOrPropertiesExcept("id", "sent", "created")
			.hasFieldOrPropertyWithValue("id", 0L)
			.hasFieldOrPropertyWithValue("sent", false);
	}

	@Test
	void testOnUpdate() {
		final ReminderEntity entity = new ReminderEntity();
		entity.onUpdate();

		assertThat(entity.getModified()).isCloseTo(OffsetDateTime.now(), within(1, SECONDS));
		assertThat(entity)
			.hasAllNullFieldsOrPropertiesExcept("id", "sent", "modified")
			.hasFieldOrPropertyWithValue("id", 0L)
			.hasFieldOrPropertyWithValue("sent", false);
	}
}
