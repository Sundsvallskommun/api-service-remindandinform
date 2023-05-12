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
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.code.beanmatchers.BeanMatchers;

class ReminderRequestTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(ReminderRequest.class, allOf(
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
		final var createdBy = "createdBy";
		final var externalCaseId = "externalCaseId";
		final var note = "note";
		final var partyId = "partyId";
		final var reminderDate = LocalDate.now();

		final var reminderRequest = ReminderRequest.create()
			.withAction(action)
			.withCaseId(caseId)
			.withCaseType(caseType)
			.withCaseLink(caseLink)
			.withCreatedBy(createdBy)
			.withExternalCaseId(externalCaseId)
			.withNote(note)
			.withPartyId(partyId)
			.withReminderDate(reminderDate);

		assertThat(reminderRequest).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(reminderRequest.getAction()).isEqualTo(action);
		assertThat(reminderRequest.getCaseId()).isEqualTo(caseId);
		assertThat(reminderRequest.getCaseType()).isEqualTo(caseType);
		assertThat(reminderRequest.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderRequest.getCreatedBy()).isEqualTo(createdBy);
		assertThat(reminderRequest.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(reminderRequest.getNote()).isEqualTo(note);
		assertThat(reminderRequest.getPartyId()).isEqualTo(partyId);
		assertThat(reminderRequest.getReminderDate()).isEqualTo(reminderDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(ReminderRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new ReminderRequest()).hasAllNullFieldsOrProperties();
	}
}
