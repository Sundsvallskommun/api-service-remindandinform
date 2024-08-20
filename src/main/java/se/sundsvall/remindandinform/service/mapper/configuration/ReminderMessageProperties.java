package se.sundsvall.remindandinform.service.mapper.configuration;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("reminder")
public class ReminderMessageProperties {

	private String message;

	private String emailMessage;

	private String subject;

	private String senderEmailName;

	private String senderEmailAddress;

	private String senderSmsName;

	private List<String> municipalityIds;

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getSenderEmailName() {
		return senderEmailName;
	}

	public void setSenderEmailName(final String senderEmailName) {
		this.senderEmailName = senderEmailName;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public void setSenderEmailAddress(final String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

	public String getSenderSmsName() {
		return senderSmsName;
	}

	public void setSenderSmsName(final String senderSmsName) {
		this.senderSmsName = senderSmsName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(final String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public List<String> getMunicipalityIds() {
		return municipalityIds;
	}

	public ReminderMessageProperties setMunicipalityIds(final List<String> municipalityIds) {
		this.municipalityIds = municipalityIds;
		return this;
	}

}
