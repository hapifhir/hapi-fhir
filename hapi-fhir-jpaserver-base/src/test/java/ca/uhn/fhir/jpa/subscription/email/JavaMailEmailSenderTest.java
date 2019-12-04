package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.subscription.module.subscriber.email.EmailDetails;
import ca.uhn.fhir.jpa.subscription.module.subscriber.email.JavaMailEmailSender;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JavaMailEmailSenderTest {

	private static final Logger ourLog = LoggerFactory.getLogger(JavaMailEmailSenderTest.class);
	private static GreenMail ourTestSmtp;
	private static int ourPort;

	@Test
	public void testSend() throws Exception {
		JavaMailEmailSender sender = new JavaMailEmailSender();
		sender.setSmtpServerHostname("localhost");
		sender.setSmtpServerPort(ourPort);
		sender.setSmtpServerUsername(null);
		sender.setSmtpServerPassword(null);
		sender.start();

		String body = "foo";

		EmailDetails details = new EmailDetails();
		details.setSubscription(new IdType("Subscription/123"));
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
		assertEquals(true, messages[0].getHeader("Content-Type")[0].startsWith("multipart/mixed; \r\n\tboundary="));

		// Expect the body of the email subscription to be a multipart mime message, with one part, that is "foo"
		assertEquals(MimeMultipart.class, messages[0].getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) messages[0].getContent();
		assertEquals(1, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		String content = (String) multipart.getBodyPart(0).getContent();
		assertEquals(true, multipart.getBodyPart(0).getHeader("Content-Type") != null);
		assertEquals("text/plain; charset=us-ascii", multipart.getBodyPart(0).getHeader("Content-Type")[0]);
		assertEquals("foo", content);
	}

	@AfterClass
	public static void afterClass() {
		ourTestSmtp.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		ServerSetup smtp = new ServerSetup(0, null, ServerSetup.PROTOCOL_SMTP);
		smtp.setServerStartupTimeout(2000);
		ourTestSmtp = new GreenMail(smtp);
		ourTestSmtp.start();
        ourPort = ourTestSmtp.getSmtp().getPort();
	}

}
