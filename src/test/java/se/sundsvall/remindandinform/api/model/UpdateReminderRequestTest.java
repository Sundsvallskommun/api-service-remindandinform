package se.sundsvall.remindandinform.api.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UpdateReminderRequestTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		assertThat(UpdateReminderRequest.class, allOf(
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
		final var externalCaseId = "externalCaseId";
		final var modifiedBy = "modifiedBy";
		final var note = "note";
		final var partyId = "partyId";
		final var reminderDate = LocalDate.now();

		final var updateReminderRequest = UpdateReminderRequest.create()
			.withAction(action)
			.withCaseId(caseId)
			.withCaseType(caseType)
			.withCaseLink(caseLink)
			.withExternalCaseId(externalCaseId)
			.withModifiedBy(modifiedBy)
			.withNote(note)
			.withPartyId(partyId)
			.withReminderDate(reminderDate);

		assertThat(updateReminderRequest).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(updateReminderRequest.getAction()).isEqualTo(action);
		assertThat(updateReminderRequest.getCaseId()).isEqualTo(caseId);
		assertThat(updateReminderRequest.getCaseType()).isEqualTo(caseType);
		assertThat(updateReminderRequest.getCaseLink()).isEqualTo(caseLink);
		assertThat(updateReminderRequest.getExternalCaseId()).isEqualTo(externalCaseId);
		assertThat(updateReminderRequest.getModifiedBy()).isEqualTo(modifiedBy);
		assertThat(updateReminderRequest.getNote()).isEqualTo(note);
		assertThat(updateReminderRequest.getPartyId()).isEqualTo(partyId);
		assertThat(updateReminderRequest.getReminderDate()).isEqualTo(reminderDate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(UpdateReminderRequest.create()).hasAllNullFieldsOrProperties();
		assertThat(new UpdateReminderRequest()).hasAllNullFieldsOrProperties();
	}
}
