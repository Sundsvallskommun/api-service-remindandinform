package se.sundsvall.remindandinform.integration.db.model;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

import org.hibernate.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reminder",
	indexes = {
		@Index(name = "reminder_id_index", columnList = "reminder_id"),
		@Index(name = "party_id_index", columnList = "party_id"),
	})
public class ReminderEntity implements Serializable {

	private static final long serialVersionUID = -771490219800899398L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "reminder_id", unique = true)
	private String reminderId;

	@Column(name = "party_id")
	private String partyId;

	@Column(name = "action", nullable = false, length = 8192)
	private String action;

	@Column(name = "note", length = Length.LONG32)
	private String note;

	@Column(name = "case_id")
	private String caseId;

	@Column(name = "case_type")
	private String caseType;

	@Column(name = "case_link", length = 512)
	private String caseLink;

	@Column(name = "external_case_id")
	private String externalCaseId;

	@Column(name = "reminder_date")
	private LocalDate reminderDate;

	@Column(name = "sent")
	private boolean sent;

	@Column(name = "created")
	private OffsetDateTime created;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified")
	private OffsetDateTime modified;

	@Column(name = "modified_by")
	private String modifiedBy;

	@PrePersist
	protected void onCreate() {
		created = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(MILLIS);
	}

	@PreUpdate
	protected void onUpdate() {
		modified = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(MILLIS);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(String reminderId) {
		this.reminderId = reminderId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseLink() {
		return caseLink;
	}

	public void setCaseLink(String caseLink) {
		this.caseLink = caseLink;
	}

	public String getExternalCaseId() {
		return externalCaseId;
	}

	public void setExternalCaseId(String externalCaseId) {
		this.externalCaseId = externalCaseId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}

	public boolean getSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(OffsetDateTime modified) {
		this.modified = modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, caseId, caseLink, caseType, created, createdBy, externalCaseId, id, modified, modifiedBy, note, partyId, reminderDate, reminderId, sent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final ReminderEntity other)) {
			return false;
		}
		return Objects.equals(action, other.action) && Objects.equals(caseId, other.caseId) && Objects.equals(caseLink, other.caseLink) && Objects.equals(caseType, other.caseType) && Objects.equals(created, other.created) && Objects.equals(createdBy,
			other.createdBy) && Objects.equals(externalCaseId, other.externalCaseId) && (id == other.id) && Objects.equals(modified, other.modified) && Objects.equals(modifiedBy, other.modifiedBy) && Objects.equals(note, other.note) && Objects.equals(
				partyId, other.partyId) && Objects.equals(reminderDate, other.reminderDate) && Objects.equals(reminderId, other.reminderId) && (sent == other.sent);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ReminderEntity [id=").append(id).append(", reminderId=").append(reminderId).append(", partyId=").append(partyId).append(", action=").append(action).append(", note=").append(note).append(", caseId=").append(caseId).append(
			", caseType=").append(caseType).append(", caseLink=").append(caseLink).append(", externalCaseId=").append(externalCaseId).append(", reminderDate=").append(reminderDate).append(", sent=").append(sent).append(", created=").append(created).append(
				", createdBy=").append(createdBy).append(", modified=").append(modified).append(", modifiedBy=").append(modifiedBy).append("]");
		return builder.toString();
	}
}
