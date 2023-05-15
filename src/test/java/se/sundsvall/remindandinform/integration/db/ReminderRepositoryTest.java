package se.sundsvall.remindandinform.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
class ReminderRepositoryTest {

	private static final String REMINDER_ID_1 = "reminderId1";
	private static final String REMINDER_ID_2 = "reminderId2";
	private static final String REMINDER_ID_3 = "reminderId3";
	private static final String REMINDER_ID_4 = "reminderId4";
	private static final String CASE_ID_1 = "caseId1";
	private static final String CASE_ID_2 = "caseId2";
	private static final String CASE_ID_3 = "caseId3";
	private static final String CASE_ID_4 = "caseId4";
	private static final String CASE_TYPE_1 = "caseType1";
	private static final String CASE_TYPE_2 = "caseType2";
	private static final String CASE_TYPE_3 = "caseType3";
	private static final String CASE_TYPE_4 = "caseType4";
	private static final String CASE_LINK_1 = "caseLink1";
	private static final String CASE_LINK_2 = "caseLink2";
	private static final String CASE_LINK_3 = "caseLink3";
	private static final String CASE_LINK_4 = "caseLink4";
	private static final String EXTERNAL_CASE_ID_1 = "externalCaseId1";
	private static final String EXTERNAL_CASE_ID_2 = "externalCaseId2";
	private static final String EXTERNAL_CASE_ID_3 = "externalCaseId3";
	private static final String EXTERNAL_CASE_ID_4 = "externalCaseId4";
	private static final String ACTION_1 = "action1";
	private static final String ACTION_2 = "action2";
	private static final String ACTION_3 = "action3";
	private static final String ACTION_4 = "action4";
	private static final String NOTE_1 = "note1";
	private static final String NOTE_2 = "note2";
	private static final String NOTE_3 = "note3";
	private static final String NOTE_4 = "note4";
	private static final LocalDate REMINDER_DATE_1 = LocalDate.now();
	private static final LocalDate REMINDER_DATE_2 = LocalDate.now().minusDays(2);
	private static final LocalDate REMINDER_DATE_3 = LocalDate.now().minusDays(2);
	private static final LocalDate REMINDER_DATE_4 = LocalDate.now().plusDays(2);
	private static final String PARTY_ID_1 = "partyId1";
	private static final String PARTY_ID_2 = "partyId2";
	private static final String PARTY_ID_3 = "partyId3";
	private static final String PARTY_ID_4 = "partyId4";

	private ReminderEntity entity1;
	private ReminderEntity entity2;
	private ReminderEntity entity3;
	private ReminderEntity entity4;

	@Autowired
	private ReminderRepository reminderRepository;

	@BeforeEach
	void setup() {
		reminderRepository.findAll().forEach(reminder -> reminderRepository.delete(reminder));

		entity1 = new ReminderEntity();
		entity1.setReminderId(REMINDER_ID_1);
		entity1.setPartyId(PARTY_ID_1);
		entity1.setCaseId(CASE_ID_1);
		entity1.setCaseType(CASE_TYPE_1);
		entity1.setCaseLink(CASE_LINK_1);
		entity1.setExternalCaseId(EXTERNAL_CASE_ID_1);
		entity1.setAction(ACTION_1);
		entity1.setNote(NOTE_1);
		entity1.setReminderDate(REMINDER_DATE_1);
		entity1.setSent(false);

		entity2 = new ReminderEntity();
		entity2.setReminderId(REMINDER_ID_2);
		entity2.setPartyId(PARTY_ID_2);
		entity2.setCaseId(CASE_ID_2);
		entity2.setCaseType(CASE_TYPE_2);
		entity2.setCaseLink(CASE_LINK_2);
		entity2.setExternalCaseId(EXTERNAL_CASE_ID_2);
		entity2.setAction(ACTION_2);
		entity2.setNote(NOTE_2);
		entity2.setReminderDate(REMINDER_DATE_2);
		entity2.setSent(false);

		// Entity with null in companyId
		entity3 = new ReminderEntity();
		entity3.setReminderId(REMINDER_ID_3);
		entity3.setPartyId(PARTY_ID_3);
		entity3.setCaseId(CASE_ID_3);
		entity3.setCaseType(CASE_TYPE_3);
		entity3.setCaseLink(CASE_LINK_3);
		entity3.setExternalCaseId(EXTERNAL_CASE_ID_3);
		entity3.setAction(ACTION_3);
		entity3.setNote(NOTE_3);
		entity3.setReminderDate(REMINDER_DATE_3);
		entity3.setSent(true);

		// Entity to remove
		entity4 = new ReminderEntity();
		entity4.setReminderId(REMINDER_ID_4);
		entity4.setPartyId(PARTY_ID_4);
		entity4.setCaseId(CASE_ID_4);
		entity4.setCaseType(CASE_TYPE_4);
		entity4.setCaseLink(CASE_LINK_4);
		entity4.setExternalCaseId(EXTERNAL_CASE_ID_4);
		entity4.setAction(ACTION_4);
		entity4.setNote(NOTE_4);
		entity4.setReminderDate(REMINDER_DATE_4);
		entity4.setSent(false);

		reminderRepository.saveAll(List.of(entity1, entity2, entity3, entity4));
	}

	@Test
	void findByPartyId() {
		final var reminders = reminderRepository.findByPartyId(PARTY_ID_1);
		assertThat(reminders).isNotEmpty().hasSize(1);
		assertThat(reminders)
			.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
			.containsExactlyInAnyOrder(entity1);
	}

	@Test
	void findByReminderId() {
		final var reminder = reminderRepository.findByReminderId((REMINDER_ID_1));
		assertThat(reminder).isPresent().contains(entity1);
	}

	@Test
	void persistAndFetch() {

		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderId("reminderId");
		reminderEntity.setPartyId("partyId");
		reminderEntity.setAction("action");
		reminderEntity.setNote("note");
		reminderEntity.setCaseLink("caseLink");
		reminderEntity.setCaseId("caseId");
		reminderEntity.setCaseType("caseType");
		reminderEntity.setExternalCaseId("externalCaseId");
		reminderEntity.setReminderDate(LocalDate.now());

		final var fetchedEntity = reminderRepository.save(reminderEntity);
		assertThat(fetchedEntity)
			.usingRecursiveComparison(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
			.isEqualTo(reminderEntity);
		assertThat(fetchedEntity.getId()).isPositive();
	}

	@Test
	void deleteByReminderId() {

		var reminders = reminderRepository.findByPartyId(PARTY_ID_4);

		assertThat(reminders).isNotEmpty().hasSize(1);
		assertThat(reminders)
			.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
			.containsExactlyInAnyOrder(entity4);

		reminderRepository.deleteByReminderId(REMINDER_ID_4);

		reminders = reminderRepository.findByPartyId(PARTY_ID_4);

		assertThat(reminders).isEmpty();
	}

	@Test
	void getRemindersByReminderDate() {

		final var reminders = reminderRepository.findByReminderDateLessThanEqualAndSentFalse(LocalDate.now());

		assertThat(reminders).isNotEmpty().hasSize(2);
		assertThat(reminders)
			.usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder().withIgnoredFieldsMatchingRegexes(".+hibernate.+").build())
			.containsExactlyInAnyOrder(entity1, entity2);
	}
}
