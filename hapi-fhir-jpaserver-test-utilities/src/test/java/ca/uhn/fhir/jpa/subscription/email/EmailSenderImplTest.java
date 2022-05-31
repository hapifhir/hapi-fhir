package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailDetails;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailSenderImpl;
import ca.uhn.fhir.rest.server.mail.IMailSvc;
import ca.uhn.fhir.rest.server.mail.MailConfig;
import ca.uhn.fhir.rest.server.mail.MailSvc;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailSenderImplTest {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSenderImplTest.class);

	@RegisterExtension
	static GreenMailExtension ourGreenMail = new GreenMailExtension(ServerSetupTest.SMTP);

	private EmailSenderImpl fixture;

	@BeforeEach
	public void setUp() {
		fixture = new EmailSenderImpl(withMailService());
	}

	@Test
	public void testSend() throws Exception {
		EmailDetails details = new EmailDetails();
		details.setSubscription(new IdType("Subscription/123"));
		details.setFrom("foo@example.com ");
		details.setTo(Arrays.asList(" to1@example.com", "to2@example.com   "));
		details.setSubjectTemplate("test subject");
		details.setBodyTemplate("foo");
		fixture.send(details);

		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 1));

		MimeMessage[] messages = ourGreenMail.getReceivedMessages();
		assertEquals(2, messages.length);
		final MimeMessage message = messages[0];
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(message));
		assertEquals("test subject", message.getSubject());
		assertEquals(1, message.getFrom().length);
		assertEquals("foo@example.com", ((InternetAddress) message.getFrom()[0]).getAddress());
		assertEquals(2, message.getAllRecipients().length);
		assertEquals("to1@example.com", ((InternetAddress) message.getAllRecipients()[0]).getAddress());
		assertEquals("to2@example.com", ((InternetAddress) message.getAllRecipients()[1]).getAddress());
		assertEquals(1, message.getHeader("Content-Type").length);
		assertEquals("text/plain; charset=UTF-8", message.getHeader("Content-Type")[0]);
		String foundBody = GreenMailUtil.getBody(message);
		assertEquals("foo", foundBody);
	}

	private IMailSvc withMailService() {
		final MailConfig mailConfig = new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ourGreenMail.getSmtp().getPort());
		return new MailSvc(mailConfig);
	}

}
