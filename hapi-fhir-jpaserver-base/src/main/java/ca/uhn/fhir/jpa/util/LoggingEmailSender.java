package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailDetails;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingEmailSender implements IEmailSender {
	private static final Logger ourLog = LoggerFactory.getLogger(LoggingEmailSender.class);

	@Override
	public void send(EmailDetails theDetails) {
		ourLog.info("Not sending subscription email to: {}", theDetails.getTo());
	}
}
