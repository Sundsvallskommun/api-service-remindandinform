package se.sundsvall.remindandinform.service;

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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReminderServiceTest {


	@Mock
	private ReminderRepository reminderRepositoryMock;

	@InjectMocks
	private ReminderService reminderService;

	@Captor
	private ArgumentCaptor<ReminderEntity> reminderEntityArgumentCaptor;

	@Test
	void findByPartyIdSuccess() {

		// Parameters
		final var partyId = "partyId";
		final var reminderId1 = "reminderId1";
		final var reminderId2 = "reminderId2";

		final var reminderEntity1 = new ReminderEntity();
		reminderEntity1.setReminderId(reminderId1);
		reminderEntity1.setPartyId(partyId);

		final var reminderEntity2 = new ReminderEntity();
		reminderEntity2.setReminderId(reminderId2);
		reminderEntity2.setPartyId(partyId);

		when(reminderRepositoryMock.findByPartyId(partyId)).thenReturn(List.of(reminderEntity1, reminderEntity2));

		final var reminders = reminderService.findRemindersByPartyId(partyId);

		assertThat(reminders).isNotNull().hasSize(2);
		assertThat(reminders.get(0).getReminderId()).isEqualTo(reminderId1);
		assertThat(reminders.get(0).getPartyId()).isEqualTo(partyId);
		assertThat(reminders.get(1).getReminderId()).isEqualTo(reminderId2);
		assertThat(reminders.get(1).getPartyId()).isEqualTo(partyId);

		verify(reminderRepositoryMock).findByPartyId(partyId);
	}

	@Test
	void findByPartyIdNotFound() {

		// Parameters
		final var partyId = "partyId";

		when(reminderRepositoryMock.findByPartyId(partyId)).thenReturn(Collections.emptyList());

		final var problem = assertThrows(DefaultProblem.class, () -> reminderService.findRemindersByPartyId(partyId));

		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo("No reminders found for partyId:'partyId'");
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock).findByPartyId(partyId);
	}

	@Test
	void deleteByReminderIdSuccess() {

		// Parameters
		final var reminderId = "reminderId";

		when(reminderRepositoryMock.findByReminderId(reminderId)).thenReturn(Optional.of(new ReminderEntity()));

		doNothing().when(reminderRepositoryMock).deleteByReminderId(reminderId);

		reminderService.deleteReminderByReminderId(reminderId);

		verify(reminderRepositoryMock).deleteByReminderId(reminderId);
	}

	@Test
	void updateReminderSuccess() {
		// Parameters
		final var reminderId = "reminderId";
		final var partyId = "partyId";
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
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPartyId(partyId);
		reminderEntity.setCaseId(caseId);
		reminderEntity.setCaseLink(caseLink);
		reminderEntity.setAction(action);
		reminderEntity.setNote(note);

		// Create updated ReminderEntity
		final var updatedReminderEntity = new ReminderEntity();
		reminderEntity.setReminderDate(updatedReminderDate);
		reminderEntity.setReminderId(reminderId);
		reminderEntity.setPartyId(updatedPartyId);
		reminderEntity.setCaseId(updatedCaseId);
		reminderEntity.setCaseLink(updatedCaseLink);
		reminderEntity.setAction(updatedAction);
		reminderEntity.setNote(updatedNote);

		when(reminderRepositoryMock.findByReminderId(reminderId)).thenReturn(Optional.of(reminderEntity));
		when(reminderRepositoryMock.save(any(ReminderEntity.class))).thenReturn(updatedReminderEntity);

		final var reminder = reminderService.updateReminder(updateReminderRequest, reminderId);

		assertThat(reminder).isNotNull();

		verify(reminderRepositoryMock).save(reminderEntityArgumentCaptor.capture());
		verify(reminderRepositoryMock, times(2)).findByReminderId(reminderId);

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).isEqualTo(reminderId);
		assertThat(reminderEntityCaptorValue.getPartyId()).isEqualTo(updatedPartyId);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(updatedCaseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(updatedCaseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(updatedAction);
		assertThat(reminderEntityCaptorValue.getNote()).isEqualTo(updatedNote);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(updatedReminderDate);
	}

	@Test
	void deleteByReminderIdNotFound() {

		// Parameters
		final var reminderId = "reminderId";

		when(reminderRepositoryMock.findByReminderId(reminderId)).thenReturn(Optional.empty());

		final var problem = assertThrows(DefaultProblem.class, () -> reminderService.deleteReminderByReminderId(reminderId));

		assertThat(problem.getTitle()).isEqualTo(Status.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo("No reminder found for reminderId:'reminderId'");
		assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);

		verify(reminderRepositoryMock, times(0)).deleteByReminderId(reminderId);
	}

	@Test
	void createReminder() {

		final var partyId = "partyId";
		final var action = "action";
		final var note = "note";
		final var caseId = "caseId";
		final var caseLink = "caseLink";
		final var reminderDate = LocalDate.now();

		// Parameters
		final var reminderRequest = ReminderRequest.create()
			.withPartyId(partyId)
			.withAction(action)
			.withNote(note)
			.withCaseId(caseId)
			.withCaseLink(caseLink)
			.withReminderDate(reminderDate);

		when(reminderRepositoryMock.save(any(ReminderEntity.class))).thenReturn(new ReminderEntity());

		final var reminder = reminderService.createReminder(reminderRequest);
		assertThat(reminder).isNotNull();

		verify(reminderRepositoryMock).save(reminderEntityArgumentCaptor.capture());

		final var reminderEntityCaptorValue = reminderEntityArgumentCaptor.getValue();
		assertThat(reminderEntityCaptorValue).isNotNull();
		assertThat(reminderEntityCaptorValue.getReminderId()).startsWith("R-");
		assertThat(reminderEntityCaptorValue.getPartyId()).isEqualTo(partyId);
		assertThat(reminderEntityCaptorValue.getCaseId()).isEqualTo(caseId);
		assertThat(reminderEntityCaptorValue.getCaseLink()).isEqualTo(caseLink);
		assertThat(reminderEntityCaptorValue.getAction()).isEqualTo(action);
		assertThat(reminderEntityCaptorValue.getNote()).isEqualTo(note);
		assertThat(reminderEntityCaptorValue.getReminderDate()).isEqualTo(reminderDate);
	}
}
