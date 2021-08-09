package ca.uhn.fhir.rest.server.mail;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MailConfig {
	private String smtpHostname;
	private Integer smtpPort;
	private String smtpUsername;
	private String smtpPassword;
	private boolean smtpUseStartTLS;

	public MailConfig() {
	}

	public String getSmtpHostname() {
		return smtpHostname;
	}

	public MailConfig setSmtpHostname(String theSmtpHostname) {
		smtpHostname = theSmtpHostname;
		return this;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public MailConfig setSmtpPort(Integer theSmtpPort) {
		smtpPort = theSmtpPort;
		return this;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public MailConfig setSmtpUsername(String theSmtpUsername) {
		smtpUsername = theSmtpUsername;
		return this;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public MailConfig setSmtpPassword(String theSmtpPassword) {
		smtpPassword = theSmtpPassword;
		return this;
	}

	public boolean isSmtpUseStartTLS() {
		return smtpUseStartTLS;
	}

	public MailConfig setSmtpUseStartTLS(boolean theSmtpUseStartTLS) {
		smtpUseStartTLS = theSmtpUseStartTLS;
		return this;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		return EqualsBuilder.reflectionEquals(this, object);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
