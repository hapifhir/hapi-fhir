package ca.uhn.fhir.rest.server.mail;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MailConfig {
	private String mySmtpHostname;
	private Integer mySmtpPort;
	private String mySmtpUsername;
	private String mySmtpPassword;
	private boolean mySmtpUseStartTLS;

	public MailConfig() {
	}

	public String getSmtpHostname() {
		return mySmtpHostname;
	}

	public MailConfig setSmtpHostname(String theSmtpHostname) {
		mySmtpHostname = theSmtpHostname;
		return this;
	}

	public Integer getSmtpPort() {
		return mySmtpPort;
	}

	public MailConfig setSmtpPort(Integer theSmtpPort) {
		mySmtpPort = theSmtpPort;
		return this;
	}

	public String getSmtpUsername() {
		return mySmtpUsername;
	}

	public MailConfig setSmtpUsername(String theSmtpUsername) {
		// SimpleJavaMail treats empty smtp username as valid username and requires auth
		mySmtpUsername = StringUtils.isBlank(theSmtpUsername) ? null : theSmtpUsername;
		return this;
	}

	public String getSmtpPassword() {
		return mySmtpPassword;
	}

	public MailConfig setSmtpPassword(String theSmtpPassword) {
		// SimpleJavaMail treats empty smtp password as valid password and requires auth
		mySmtpPassword = StringUtils.isBlank(theSmtpPassword) ? null : theSmtpPassword;
		return this;
	}

	public boolean isSmtpUseStartTLS() {
		return mySmtpUseStartTLS;
	}

	public MailConfig setSmtpUseStartTLS(boolean theSmtpUseStartTLS) {
		mySmtpUseStartTLS = theSmtpUseStartTLS;
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
