package ca.uhn.fhir.jpa.subscription.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

import static org.junit.Assert.*;

public class EmailSenderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSenderTest.class);
	private static GreenMail ourTestSmtp;

	@Test
	public void testSend() throws Exception {
		EmailSender sender = new EmailSender();
		sender.setSmtpServerHost("localhost");
		sender.setSmtpServerPort(3025);
		sender.start();

		String body = "foo";

		EmailDetails details = new EmailDetails();
		details.setFrom("foo@example.com ");
		details.setTo(Arrays.asList(" to1@example.com", "to2@example.com   "));
		details.setSubjectTemplate("test subject");
		details.setBodyTemplate(body);
		sender.send(details);

		MimeMessage[] messages = ourTestSmtp.getReceivedMessages();
		assertEquals(2, messages.length);
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(messages[0]));
		assertEquals("test subject", messages[0].getSubject());
		assertEquals(1, messages[0].getFrom().length);
		assertEquals("foo@example.com", ((InternetAddress)messages[0].getFrom()[0]).getAddress());
		assertEquals(2, messages[0].getAllRecipients().length);
		assertEquals("to1@example.com", ((InternetAddress)messages[0].getAllRecipients()[0]).getAddress());
		assertEquals("to2@example.com", ((InternetAddress)messages[0].getAllRecipients()[1]).getAddress());
		assertEquals(1, messages[0].getHeader("Content-Type").length);
		assertEquals("text/plain; charset=UTF-8", messages[0].getHeader("Content-Type")[0]);
		String foundBody = GreenMailUtil.getBody(messages[0]);
		assertEquals("foo", foundBody);
	}

	@AfterClass
	public static void afterClass() {
		ourTestSmtp.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		ourTestSmtp = new GreenMail(ServerSetupTest.SMTP);
		ourTestSmtp.start();
	}

}
