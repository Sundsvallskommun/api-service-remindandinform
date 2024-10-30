package se.sundsvall.remindandinform.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Status;

import se.sundsvall.remindandinform.api.model.ReminderRequest;
import se.sundsvall.remindandinform.api.model.UpdateReminderRequest;
import se.sundsvall.remindandinform.integration.db.ReminderRepository;
import se.sundsvall.remindandinform.integration.db.model.ReminderEntity;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {

	private static final String MUNICIPALITY_ID = "municipalityId";

	private static final String PARTY_ID = "partyId";

	private static final String REMINDER_ID = "reminderId";

	@Mock
	private ReminderRepository reminderRepositoryMock;

	@InjectMocks
	private ReminderService reminderService;

	@Captor
	private ArgumentCaptor<ReminderEntity> reminderEntityArgumentCaptor;

	@Test
	void findByPartyIdSuccess() {

		// Parameters
		final var partyId = PARTY_ID;
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPartyId(partyId);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPartyId(partyId);

		when(reminderRepositoryMock.findByPartyIdAndMunicipalityId(partyId, MUNICIPALITY_ID)).thenReturn(List.of(reminderEntity1, reminderEntity2));

		final var reminders = reminderService.findRemindersByPartyIdAndMunicipalityId(partyId, MUNICIPALITY_ID);

		assertThat(reminders).isNotNull().hasSize(2);
		assertThat(reminders.get(0).getReminderId()).isEqualTo(reminderId1);
		assertThat(reminders.get(0).getPartyId()).isEqualTo(partyId);
		assertThat(reminders.get(1).getReminderId()).isEqualTo(reminderId2);
		assertThat(reminders.get(1).getPartyId()).isEqualTo(partyId);

		verify(reminderRepositoryMock).findByPartyIdAndMunicipalityId(partyId, MUNICIPALITY_ID);
	}

	@Test
	void findByPartyIdNotFound() {

		when(reminderRepositoryMock.findByPartyIdAndMunicipalityId(PARTY_ID, MUNICIPALITY_ID)).thenReturn(Collections.emptyList());

		final var problem = assertThrows(DefaultProblem.class, () -> reminderService.findRemindersByPartyIdAndMunicipalityId(PARTY_ID, MUNICIPALITY_ID));

		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo("No reminders found for partyId:'partyId' and municipalityId: 'municipalityId'");
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock).findByPartyIdAndMunicipalityId(PARTY_ID, MUNICIPALITY_ID);
	}

	@Test
	void deleteByReminderIdSuccess() {

		when(reminderRepositoryMock.findByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID)).thenReturn(Optional.of(new ReminderEntity()));

		doNothing().when(reminderRepositoryMock).deleteByReminderId(REMINDER_ID);

		reminderService.deleteReminderByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID);

		verify(reminderRepositoryMock).deleteByReminderId(REMINDER_ID);
	}

	@Test
	void updateReminderSuccess() {
		// Parameters
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var action = "action";
		final var note = "note";
		final var reminderDate = LocalDate.now();

		final var updatedPartyId = "updatedPartyId";
		final var updatedCaseId = "updatedCaseId";
		final var updatedCaseLink = "updatedCaseLink";
		final var updatedAction = "updatedAction";
		final var updatedNote = "updatedNote";
		final var updatedReminderDate = LocalDate.now().plusDays(10);

		final var updateReminderRequest = UpdateReminderRequest.create()
			.withPartyId(updatedPartyId)
			.withReminderDate(updatedReminderDate)
			.withCaseId(updatedCaseId)
			.withCaseLink(updatedCaseLink)
			.withAction(updatedAction)
			.withNote(updatedNote);

		// Create existing ReminderEntity
		final var reminderEntity = new ReminderEntity();
		reminderEntity.setReminderDate(reminderDate);
		reminderEntity.setReminderId(REMINDER_ID);
		reminderEntity.setPartyId(PARTY_ID);
		reminderEntity.setCaseId(caseId);
		reminderEntity.setCaseLink(caseLink);
		reminderEntity.setAction(action);
		reminderEntity.setNote(note);

		// Create updated ReminderEntity
		final var updatedReminderEntity = new ReminderEntity();
		reminderEntity.setReminderDate(updatedReminderDate);
		reminderEntity.setReminderId(REMINDER_ID);
		reminderEntity.setPartyId(updatedPartyId);
		reminderEntity.setCaseId(updatedCaseId);
		reminderEntity.setCaseLink(updatedCaseLink);
		reminderEntity.setAction(updatedAction);
		reminderEntity.setNote(updatedNote);

		when(reminderRepositoryMock.findByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID)).thenReturn(Optional.of(reminderEntity));
		when(reminderRepositoryMock.save(any(ReminderEntity.class))).thenReturn(updatedReminderEntity);

		final var reminder = reminderService.updateReminder(updateReminderRequest, REMINDER_ID, MUNICIPALITY_ID);

		assertThat(reminder).isNotNull();

		verify(reminderRepositoryMock).save(reminderEntityArgumentCaptor.capture());
		verify(reminderRepositoryMock, times(2)).findByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID);

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).isEqualTo(REMINDER_ID);
		assertThat(reminderEntityCaptorValue.getPartyId()).isEqualTo(updatedPartyId);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(updatedCaseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(updatedCaseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(updatedAction);
		assertThat(reminderEntityCaptorValue.getNote()).isEqualTo(updatedNote);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(updatedReminderDate);
	}

	@Test
	void deleteByReminderIdNotFound() {

		when(reminderRepositoryMock.findByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID)).thenReturn(Optional.empty());

		final var problem = assertThrows(DefaultProblem.class, () -> reminderService.deleteReminderByReminderIdAndMunicipalityId(REMINDER_ID, MUNICIPALITY_ID));

		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo("No reminder found for reminderId:'reminderId'");
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock, times(0)).deleteByReminderId(REMINDER_ID);
	}

	@Test
	void createReminder() {

		final var action = "action";
		final var note = "note";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var reminderRequest = ReminderRequest.create()
			.withPartyId(PARTY_ID)
			.withAction(action)
			.withNote(note)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withReminderDate(reminderDate);

		when(reminderRepositoryMock.save(any(ReminderEntity.class))).thenReturn(new ReminderEntity());

		final var reminder = reminderService.createReminder(reminderRequest, MUNICIPALITY_ID);
		assertThat(reminder).isNotNull();

		verify(reminderRepositoryMock).save(reminderEntityArgumentCaptor.capture());

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).startsWith("R-");
		assertThat(reminderEntityCaptorValue.getPartyId()).isEqualTo(PARTY_ID);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(action);
		assertThat(reminderEntityCaptorValue.getNote()).isEqualTo(note);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(reminderDate);
	}

}
