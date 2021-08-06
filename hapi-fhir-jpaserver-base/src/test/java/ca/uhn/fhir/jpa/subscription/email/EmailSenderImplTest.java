package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailDetails;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailSenderImpl;
import ca.uhn.fhir.rest.server.mail.MailConfig;
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
		fixture = new EmailSenderImpl(withMailConfig());
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
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(messages[0]));
		assertEquals("test subject", messages[0].getSubject());
		assertEquals(1, messages[0].getFrom().length);
		assertEquals("foo@example.com", ((InternetAddress) messages[0].getFrom()[0]).getAddress());
		assertEquals(2, messages[0].getAllRecipients().length);
		assertEquals("to1@example.com", ((InternetAddress) messages[0].getAllRecipients()[0]).getAddress());
		assertEquals("to2@example.com", ((InternetAddress) messages[0].getAllRecipients()[1]).getAddress());
		assertEquals(1, messages[0].getHeader("Content-Type").length);
		assertEquals("text/plain; charset=UTF-8", messages[0].getHeader("Content-Type")[0]);
		String foundBody = GreenMailUtil.getBody(messages[0]);
		assertEquals("foo", foundBody);
	}

	private MailConfig withMailConfig() {
		return new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ServerSetupTest.SMTP.getPort());
	}

}
