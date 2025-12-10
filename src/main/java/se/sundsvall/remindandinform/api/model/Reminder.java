package se.sundsvall.remindandinform.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@Schema(description = "Reminder model")
public class Reminder {

	@Schema(description = "Reminder ID", examples = "R-81471222-5798-11e9-ae24-57fa13b361e1")
	private String reminderId;

	@Schema(description = "Party ID (e.g. a personId or an organizationId)", examples = "81471222-5798-11e9-ae24-57fa13b361e2")
	private String partyId;

	@Schema(description = "What should be done", examples = "Renew application")
	private String action;

	@Schema(description = "Reminder note", examples = "A short note about the reminder")
	private String note;

	@Schema(description = "Case ID", examples = "12345")
	private String caseId;

	@Schema(description = "Case Type", examples = "Byggärende")
	private String caseType;

	@Schema(description = "Link to the case", examples = "http://test.sundsvall.se/case12345")
	private String caseLink;

	@Schema(description = "External Case ID", examples = "2229")
	private String externalCaseId;

	@Schema(description = "Date for reminding", examples = "2021-11-01")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate reminderDate;

	@Schema(description = "Timestamp when reminder was created", examples = "2021-10-15T07:43:13.225+02:00")
	private OffsetDateTime created;

	@Schema(description = "Identity of the individual that created the reminder", examples = "Albert Einstein")
	private String createdBy;

	@Schema(description = "Timestamp when reminder was last modified", examples = "2021-10-22T11:28:37.431+02:00")
	private OffsetDateTime modified;

	@Schema(description = "Identity of the individual that last modified the reminder", examples = "Albert Einstein")
	private String modifiedBy;

	public static Reminder create() {
		return new Reminder();
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public Reminder withReminderId(String reminderId) {
		this.reminderId = reminderId;
		return this;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public Reminder withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Reminder withAction(String action) {
		this.action = action;
		return this;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Reminder withNote(String note) {
		this.note = note;
		return this;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public Reminder withCaseId(String caseId) {
		this.caseId = caseId;
		return this;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public Reminder withCaseType(String caseType) {
		this.caseType = caseType;
		return this;
	}

	public String getCaseLink() {
		return caseLink;
	}

	public void setCaseLink(String caseLink) {
		this.caseLink = caseLink;
	}

	public Reminder withCaseLink(String caseLink) {
		this.caseLink = caseLink;
		return this;
	}

	public String getExternalCaseId() {
		return externalCaseId;
	}

	public void setExternalCaseId(String externalCaseId) {
		this.externalCaseId = externalCaseId;
	}

	public Reminder withExternalCaseId(String externalCaseId) {
		this.externalCaseId = externalCaseId;
		return this;
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}

	public Reminder withReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
		return this;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public Reminder withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Reminder withCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(OffsetDateTime modified) {
		this.modified = modified;
	}

	public Reminder withModified(OffsetDateTime modified) {
		this.modified = modified;
		return this;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Reminder withModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, caseId, caseLink, caseType, created, createdBy, externalCaseId, modified,
			modifiedBy, note, partyId, reminderDate, reminderId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Reminder other = (Reminder) obj;
		return Objects.equals(action, other.action) && Objects.equals(caseId, other.caseId)
			&& Objects.equals(caseLink, other.caseLink) && Objects.equals(caseType, other.caseType)
			&& Objects.equals(created, other.created) && Objects.equals(createdBy, other.createdBy)
			&& Objects.equals(externalCaseId, other.externalCaseId) && Objects.equals(modified, other.modified)
			&& Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(note, other.note)
			&& Objects.equals(partyId, other.partyId) && Objects.equals(reminderDate, other.reminderDate)
			&& Objects.equals(reminderId, other.reminderId);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Reminder [reminderId=").append(reminderId).append(", partyId=").append(partyId)
			.append(", action=").append(action).append(", note=").append(note).append(", caseId=").append(caseId)
			.append(", caseType=").append(caseType).append(", caseLink=").append(caseLink)
			.append(", externalCaseId=").append(externalCaseId).append(", reminderDate=").append(reminderDate)
			.append(", createdBy=").append(createdBy).append(", modifiedBy=").append(modifiedBy)
			.append(", created=").append(created).append(", modified=").append(modified).append("]");
		return builder.toString();
	}
}
