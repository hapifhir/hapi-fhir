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

import static org.assertj.core.api.Assertions.assertThat;

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

		assertThat(ourGreenMail.waitForIncomingEmail(10000, 1)).isTrue();

		MimeMessage[] messages = ourGreenMail.getReceivedMessages();
		assertThat(messages.length).isEqualTo(2);
		final MimeMessage message = messages[0];
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(message));
		assertThat(message.getSubject()).isEqualTo("test subject");
		assertThat(message.getFrom().length).isEqualTo(1);
		assertThat(((InternetAddress) message.getFrom()[0]).getAddress()).isEqualTo("foo@example.com");
		assertThat(message.getAllRecipients().length).isEqualTo(2);
		assertThat(((InternetAddress) message.getAllRecipients()[0]).getAddress()).isEqualTo("to1@example.com");
		assertThat(((InternetAddress) message.getAllRecipients()[1]).getAddress()).isEqualTo("to2@example.com");
		assertThat(message.getHeader("Content-Type").length).isEqualTo(1);
		assertThat(message.getHeader("Content-Type")[0]).isEqualTo("text/plain; charset=UTF-8");
		String foundBody = GreenMailUtil.getBody(message);
		assertThat(foundBody).isEqualTo("foo");
	}

	private IMailSvc withMailService() {
		final MailConfig mailConfig = new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ourGreenMail.getSmtp().getPort());
		return new MailSvc(mailConfig);
	}

}
