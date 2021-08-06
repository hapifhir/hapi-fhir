package ca.uhn.fhir.rest.server.mail;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;

import javax.annotation.Nonnull;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MailSvcIT {
	private static final String FROM_ADDRESS = "from_address@email.com";
	private static final String TO_ADDRESS = "to_address@email.com";
	private static final String SUBJECT = "Email Subject";
	private static final String BODY = "Email Body !!!";

	@RegisterExtension
	static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP);

	private MailSvc fixture;

	@BeforeEach
	public void setUp() {
		fixture = new MailSvc();
	}

	@Test
	public void testSendSingleMail() throws Exception {
		// setup
		final MailConfig mailConfig = withMailConfig();
		final Email email = withEmail();
		// execute
		fixture.sendMail(mailConfig, email);
		// validate
		assertTrue(greenMail.waitForIncomingEmail(1000, 1));
		final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertEquals(1, receivedMessages.length);
		assertEquals(SUBJECT, receivedMessages[0].getSubject());
		assertEquals(BODY, GreenMailUtil.getBody(receivedMessages[0]));
	}

	@Test
	public void testSendMultipleMail() throws Exception {
		// setup
		final MailConfig mailConfig = withMailConfig();
		final List<Email> emails = Arrays.asList(withEmail(), withEmail(), withEmail());
		// execute
		fixture.sendMail(mailConfig, emails);
		// validate
		assertTrue(greenMail.waitForIncomingEmail(1000, emails.size()));
		final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertEquals(emails.size(), receivedMessages.length);
		assertEquals(SUBJECT, receivedMessages[0].getSubject());
		assertEquals(BODY, GreenMailUtil.getBody(receivedMessages[0]));
		assertEquals(SUBJECT, receivedMessages[1].getSubject());
		assertEquals(BODY, GreenMailUtil.getBody(receivedMessages[1]));
		assertEquals(SUBJECT, receivedMessages[2].getSubject());
		assertEquals(BODY, GreenMailUtil.getBody(receivedMessages[2]));
	}

	@Test
	public void testSendMailWithInvalidToAddress() {
		// setup
		final MailConfig mailConfig = withMailConfig();
		final Email email = withEmail("xyz");
		// execute
		fixture.sendMail(mailConfig, email);
		// validate
		assertTrue(greenMail.waitForIncomingEmail(1000, 0));
		final MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
		assertEquals(0, receivedMessages.length);
	}

	@Test
	public void testSendMailWithInvalidToAddressExpectErrorHandler() {
		// setup
		final MailConfig mailConfig = withMailConfig();
		final Email email = withEmail("xyz");
		// execute
		fixture.sendMail(mailConfig, email,
			() -> fail("Should not execute on Success"),
			(e) -> assertEquals("Invalid TO address: " + email, e.getMessage()));
		// validate
		assertTrue(greenMail.waitForIncomingEmail(1000, 0));
	}

	private MailConfig withMailConfig() {
		return new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ServerSetupTest.SMTP.getPort());
	}

	private Email withEmail() {
		return withEmail(TO_ADDRESS);
	}

	private Email withEmail(@Nonnull String toAddress) {
		return EmailBuilder.startingBlank()
			.from(FROM_ADDRESS)
			.to(toAddress)
			.withSubject(SUBJECT)
			.withPlainText(BODY)
			.buildEmail();
	}

}
