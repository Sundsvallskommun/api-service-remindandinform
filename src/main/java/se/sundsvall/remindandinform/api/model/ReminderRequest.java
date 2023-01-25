package se.sundsvall.remindandinform.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@Schema(description = "Reminder creation request model")
public class ReminderRequest {

	@Schema(description = "PartyId (e.g. a personId or an organizationId)", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = REQUIRED)
	@ValidUuid
	private String partyId;

	@Schema(description = "What should be done", example = "Renew application", requiredMode = REQUIRED)
	@NotNull
	@Size(max = 8192)
	private String action;

	@Schema(description = "Reminder note", example = "A short note about the reminder")
	@Size(max = 2048)
	private String note;

	@Schema(description = "Id for the case", example = "12345")
	@Size(max = 255)
	private String caseId;

	@Schema(description = "Type of the case", example = "Byggärende")
	@Size(max = 255)
	private String caseType;

	@Schema(description = "Link to the case", example = "http://test.sundsvall.se/case1337")
	@Size(max = 512)
	private String caseLink;

	@Schema(description = "External id for the case", example = "2229")
	@Size(max = 255)
	private String externalCaseId;

	@Schema(description = "Date for reminding", example = "2021-11-01", requiredMode = REQUIRED)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate reminderDate;

	@Schema(description = "Identity (username or given name) of the individual that is creating the reminder post", example = "Albert Einstein", requiredMode = REQUIRED)
	@NotBlank
	@Size(max = 255)
	private String createdBy;

	public static ReminderRequest create() {
		return new ReminderRequest();
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(final String partyId) {
		this.partyId = partyId;
	}

	public ReminderRequest withPartyId(final String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getAction() {
		return action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public ReminderRequest withAction(final String action) {
		this.action = action;
		return this;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public ReminderRequest withNote(final String note) {
		this.note = note;
		return this;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(final String caseId) {
		this.caseId = caseId;
	}

	public ReminderRequest withCaseId(final String caseId) {
		this.caseId = caseId;
		return this;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(final String caseType) {
		this.caseType = caseType;
	}

	public ReminderRequest withCaseType(final String caseType) {
		this.caseType = caseType;
		return this;
	}

	public String getCaseLink() {
		return caseLink;
	}

	public void setCaseLink(final String caseLink) {
		this.caseLink = caseLink;
	}

	public ReminderRequest withCaseLink(final String caseLink) {
		this.caseLink = caseLink;
		return this;
	}

	public String getExternalCaseId() {
		return externalCaseId;
	}

	public void setExternalCaseId(final String externalCaseId) {
		this.externalCaseId = externalCaseId;
	}

	public ReminderRequest withExternalCaseId(final String externalCaseId) {
		this.externalCaseId = externalCaseId;
		return this;
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(final LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}

	public ReminderRequest withReminderDate(final LocalDate reminderDate) {
		this.reminderDate = reminderDate;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	public ReminderRequest withCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, caseId, caseLink, caseType, createdBy, externalCaseId, note, partyId, reminderDate);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final var other = (ReminderRequest) obj;
		return Objects.equals(action, other.action) && Objects.equals(caseId, other.caseId)
			&& Objects.equals(caseLink, other.caseLink) && Objects.equals(caseType, other.caseType)
			&& Objects.equals(createdBy, other.createdBy) && Objects.equals(externalCaseId, other.externalCaseId)
			&& Objects.equals(note, other.note) && Objects.equals(partyId, other.partyId)
			&& Objects.equals(reminderDate, other.reminderDate);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("ReminderRequest [partyId=").append(partyId).append(", action=").append(action).append(", note=")
			.append(note).append(", caseId=").append(caseId).append(", caseType=").append(caseType)
			.append(", caseLink=").append(caseLink).append(", externalCaseId=").append(externalCaseId)
			.append(", reminderDate=").append(reminderDate).append(", createdBy=").append(createdBy).append("]");
		return builder.toString();
	}
}
