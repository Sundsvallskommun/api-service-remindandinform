package se.sundsvall.remindandinform.service.mapper.configuration;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSenderEmailName() {
		return senderEmailName;
	}

	public void setSenderEmailName(String senderEmailName) {
		this.senderEmailName = senderEmailName;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}

	public String getSenderSmsName() {
		return senderSmsName;
	}

	public void setSenderSmsName(String senderSmsName) {
		this.senderSmsName = senderSmsName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

}
