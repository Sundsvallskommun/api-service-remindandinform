package se.sundsvall.remindandinform.integration.db.model;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.Length;
import org.hibernate.annotations.TimeZoneStorage;

@Entity
@Table(name = "reminder",
	indexes = {
		@Index(name = "reminder_id_index", columnList = "reminder_id"),
		@Index(name = "party_id_index", columnList = "party_id"),
		@Index(name = "municipality_id_index", columnList = "municipality_id"),
	},
	uniqueConstraints = {
		@UniqueConstraint(name = "uq_reminder_id", columnNames = {"reminder_id"})
	})
public class ReminderEntity implements Serializable {

	private static final long serialVersionUID = -771490219800899398L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "municipality_id")
	private String municipalityId;

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
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime created;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified")
	@TimeZoneStorage(NORMALIZE)
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

	public void setId(final long id) {
		this.id = id;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(final String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public String getReminderId() {
		return reminderId;
	}

	public void setReminderId(final String reminderId) {
		this.reminderId = reminderId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(final String partyId) {
		this.partyId = partyId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public String getNote() {
		return note;
	}

	public void setNote(final String note) {
		this.note = note;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(final String caseId) {
		this.caseId = caseId;
	}

	public String getCaseLink() {
		return caseLink;
	}

	public void setCaseLink(final String caseLink) {
		this.caseLink = caseLink;
	}

	public String getExternalCaseId() {
		return externalCaseId;
	}

	public void setExternalCaseId(final String externalCaseId) {
		this.externalCaseId = externalCaseId;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(final String caseType) {
		this.caseType = caseType;
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(final LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}

	public boolean getSent() {
		return sent;
	}

	public void setSent(final boolean sent) {
		this.sent = sent;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(final OffsetDateTime created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	public OffsetDateTime getModified() {
		return modified;
	}

	public void setModified(final OffsetDateTime modified) {
		this.modified = modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(final String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final ReminderEntity that = (ReminderEntity) o;
		return id == that.id && sent == that.sent && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(reminderId, that.reminderId) && Objects.equals(partyId, that.partyId) && Objects.equals(action, that.action) && Objects.equals(note, that.note) && Objects.equals(caseId, that.caseId) && Objects.equals(caseType, that.caseType) && Objects.equals(caseLink, that.caseLink) && Objects.equals(externalCaseId, that.externalCaseId) && Objects.equals(reminderDate, that.reminderDate) && Objects.equals(created, that.created) && Objects.equals(createdBy, that.createdBy) && Objects.equals(modified, that.modified) && Objects.equals(modifiedBy, that.modifiedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, municipalityId, reminderId, partyId, action, note, caseId, caseType, caseLink, externalCaseId, reminderDate, sent, created, createdBy, modified, modifiedBy);
	}

	@Override
	public String toString() {
		return "ReminderEntity{" +
			"id=" + id +
			", municipalityId='" + municipalityId + '\'' +
			", reminderId='" + reminderId + '\'' +
			", partyId='" + partyId + '\'' +
			", action='" + action + '\'' +
			", note='" + note + '\'' +
			", caseId='" + caseId + '\'' +
			", caseType='" + caseType + '\'' +
			", caseLink='" + caseLink + '\'' +
			", externalCaseId='" + externalCaseId + '\'' +
			", reminderDate=" + reminderDate +
			", sent=" + sent +
			", created=" + created +
			", createdBy='" + createdBy + '\'' +
			", modified=" + modified +
			", modifiedBy='" + modifiedBy + '\'' +
			'}';
	}

}
