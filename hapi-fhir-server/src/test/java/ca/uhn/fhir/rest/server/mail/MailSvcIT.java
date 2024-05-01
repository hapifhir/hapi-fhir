package ca.uhn.fhir.rest.server.mail;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.simplejavamail.MailException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.email.EmailBuilder;

import jakarta.annotation.Nonnull;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.fail;


public class MailSvcIT {
	private static final String FROM_ADDRESS = "from_address@email.com";
	private static final String TO_ADDRESS = "to_address@email.com";
	private static final String SUBJECT = "Email Subject";
	private static final String BODY = "Email Body !!!";

	@RegisterExtension
	static GreenMailExtension ourGreenMail = new GreenMailExtension(ServerSetupTest.SMTP);

	private IMailSvc fixture;

	@BeforeEach
	public void setUp() {
		fixture = new MailSvc(withMailConfig());
	}

	@Test
	public void testSendSingleMail() throws Exception {
		// setup
		final Email email = withEmail();
		// execute
		fixture.sendMail(email);
		// validate
		boolean condition = ourGreenMail.waitForIncomingEmail(5000, 1);
		assertTrue(condition);
		final MimeMessage[] receivedMessages = ourGreenMail.getReceivedMessages();
		assertThat(receivedMessages.length).isEqualTo(1);
		assertThat(receivedMessages[0].getSubject()).isEqualTo(SUBJECT);
		assertThat(GreenMailUtil.getBody(receivedMessages[0])).isEqualTo(BODY);
	}

	@Test
	public void testSendMultipleMail() throws Exception {
		// setup
		final List<Email> emails = Arrays.asList(withEmail(), withEmail(), withEmail());
		// execute
		fixture.sendMail(emails);
		// validate
		assertTrue(ourGreenMail.waitForIncomingEmail(15000, emails.size()));
		final MimeMessage[] receivedMessages = ourGreenMail.getReceivedMessages();
		assertThat(receivedMessages.length).isEqualTo(emails.size());
		assertThat(receivedMessages[0].getSubject()).isEqualTo(SUBJECT);
		assertThat(GreenMailUtil.getBody(receivedMessages[0])).isEqualTo(BODY);
		assertThat(receivedMessages[1].getSubject()).isEqualTo(SUBJECT);
		assertThat(GreenMailUtil.getBody(receivedMessages[1])).isEqualTo(BODY);
		assertThat(receivedMessages[2].getSubject()).isEqualTo(SUBJECT);
		assertThat(GreenMailUtil.getBody(receivedMessages[2])).isEqualTo(BODY);
	}

	@Test
	public void testSendMailWithInvalidToAddress() {
		// setup
		final Email email = withEmail("xyz");

		// execute
		fixture.sendMail(email);

		// validate
		assertTrue(ourGreenMail.waitForIncomingEmail(5000, 0));
		final MimeMessage[] receivedMessages = ourGreenMail.getReceivedMessages();
		assertThat(receivedMessages.length).isEqualTo(0);
	}

	@Test
	public void testSendMailWithInvalidToAddressExpectErrorHandler() {
		// setup
		final Email email = withEmail("xyz");
		// execute
		fixture.sendMail(email,
			() -> fail("Should not execute on Success"),
			(e) -> {
				assertTrue(e instanceof MailException);
				assertEquals("Invalid TO address: " + email, e.getMessage());
			});
		// validate
		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 0));
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
