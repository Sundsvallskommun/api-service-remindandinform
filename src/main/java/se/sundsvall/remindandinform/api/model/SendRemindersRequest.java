package se.sundsvall.remindandinform.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request model for sending reminders of a specified date")
public class SendRemindersRequest {

	@Schema(description = "Date for reminding", example = "2021-11-01", requiredMode = REQUIRED)
	@NotNull
	private LocalDate reminderDate;

	public static SendRemindersRequest create() {
		return new SendRemindersRequest();
	}

	public LocalDate getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
	}

	public SendRemindersRequest withReminderDate(LocalDate reminderDate) {
		this.reminderDate = reminderDate;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reminderDate);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		final var sendRemindersRequest = (SendRemindersRequest) o;
		return Objects.equals(sendRemindersRequest.reminderDate, reminderDate);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		return builder.append("SendRemindersRequest{ reminderDate=").append(reminderDate).append("}").toString();
	}
}
