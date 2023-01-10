package ca.uhn.fhirtest.config;

import ca.uhn.fhir.rest.server.mail.EmailDetails;
import ca.uhn.fhir.rest.server.mail.IEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingEmailSender implements IEmailSender {
	private static final Logger ourLog = LoggerFactory.getLogger(LoggingEmailSender.class);

	@Override
	public void send(EmailDetails theDetails) {
		ourLog.info("Not sending subscription email to: {}", theDetails.getTo());
	}
}
