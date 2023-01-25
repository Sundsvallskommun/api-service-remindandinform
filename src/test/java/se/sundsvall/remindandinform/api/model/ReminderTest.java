package se.sundsvall.remindandinform.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.code.beanmatchers.BeanMatchers;

class ReminderTest {

	@BeforeEach
	void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
		BeanMatchers.registerValueGenerator(() -> OffsetDateTime.now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(Reminder.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilderMethods() {
		final var action = "action";
		final var caseId = "caseId";
		final var caseType = "caseType";
		final var caseLink = "caselink";
		final var created = OffsetDateTime.now().minusDays(7);
		final var createdBy = "createdBy";
		final var externalCaseId = "externalCaseId";
		final var note = "note";
		final var modified = OffsetDateTime.now().minusDays(3);
		final var modifiedBy = "modifiedBy";
		final var partyId = "partyId";
		final var reminderDate = LocalDate.now();
		final var reminderId = "reminderId";
		
		final var reminder = Reminder.create()
			.withAction(action)
			.withCaseId(caseId)
			.withCaseType(caseType)
			.withCaseLink(caseLink)
			.withCreated(created)
			.withCreatedBy(createdBy)
			.withExternalCaseId(externalCaseId)
			.withNote(note)
			.withModified(modified)
			.withModifiedBy(modifiedBy)
			.withPartyId(partyId)
			.withReminderDate(reminderDate)
			.withReminderId(reminderId);

		assertThat(reminder).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(reminder.getAction()).isEqualTo(action);
		assertThat(reminder.getCaseId()).isEqualTo(caseId);
		assertThat(reminder.getCaseType()).isEqualTo(caseType);
		assertThat(reminder.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminder.getCreated()).isEqualTo(created);
		assertThat(reminder.getCreatedBy()).isEqualTo(createdBy);
		assertThat(reminder.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(reminder.getNote()).isEqualTo(note);
		assertThat(reminder.getModified()).isEqualTo(modified);
		assertThat(reminder.getModifiedBy()).isEqualTo(modifiedBy);
		assertThat(reminder.getPartyId()).isEqualTo(partyId);
		assertThat(reminder.getReminderDate()).isEqualTo(reminderDate);
		assertThat(reminder.getReminderId()).isEqualTo(reminderId);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(Reminder.create()).hasAllNullFieldsOrProperties();
		assertThat(new Reminder()).hasAllNullFieldsOrProperties();
	}
}
