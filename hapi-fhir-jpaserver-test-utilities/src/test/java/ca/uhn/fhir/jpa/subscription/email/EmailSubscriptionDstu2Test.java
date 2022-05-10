package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderDstu2Test;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailSenderImpl;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.mail.IMailSvc;
import ca.uhn.fhir.rest.server.mail.MailConfig;
import ca.uhn.fhir.rest.server.mail.MailSvc;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ca.uhn.fhir.jpa.subscription.resthook.RestHookTestDstu3Test.logAllInterceptors;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailSubscriptionDstu2Test extends BaseResourceProviderDstu2Test {

	private static final Logger ourLog = LoggerFactory.getLogger(EmailSubscriptionDstu2Test.class);

	@RegisterExtension
	static GreenMailExtension ourGreenMail = new GreenMailExtension(ServerSetupTest.SMTP.withPort(0));

	private final List<IIdType> mySubscriptionIds = new ArrayList<>();

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@Override
	@AfterEach
	public void after() throws Exception {
		ourLog.info("** AFTER **");
		super.after();

		for (IIdType next : mySubscriptionIds) {
			ourClient.delete().resourceById(next).execute();
		}
		mySubscriptionIds.clear();
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		ourLog.info("Before re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);

		mySubscriptionTestUtil.registerEmailInterceptor();

		ourLog.info("After re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);
	}

	private Subscription createSubscription(String criteria, String payload, String endpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(SubscriptionStatusEnum.REQUESTED);
		subscription.setCriteria(criteria);

		Subscription.Channel channel = new Subscription.Channel();
		channel.setType(SubscriptionChannelTypeEnum.EMAIL);
		channel.setPayload(payload);
		channel.setEndpoint(endpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		mySubscriptionTestUtil.waitForQueueToDrain();

		return subscription;
	}

	private Observation sendObservation(String code, String system) {
		Observation observation = new Observation();
		CodeableConceptDt codeableConcept = new CodeableConceptDt();
		observation.setCode(codeableConcept);
		CodingDt coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(ObservationStatusEnum.FINAL);

		MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();

		String observationId = methodOutcome.getId().getIdPart();
		observation.setId(observationId);

		return observation;
	}

	@Test
	public void testSubscribeAndDeliver() throws Exception {
		String payload = "A subscription update has been received";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, "to1@example.com,to2@example.com");
		mySubscriptionTestUtil.waitForQueueToDrain();
		await().until(() -> mySubscriptionRegistry.get(subscription1.getIdElement().getIdPart()), Matchers.not(Matchers.nullValue()));
		mySubscriptionTestUtil.setEmailSender(subscription1.getIdElement(), new EmailSenderImpl(withMailService()));
		assertEquals(0, Arrays.asList(ourGreenMail.getReceivedMessages()).size());

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 1));

		MimeMessage[] messages = ourGreenMail.getReceivedMessages();
		assertEquals(2, messages.length);
		ourLog.info("Received: " + GreenMailUtil.getWholeMessage(messages[0]));
		assertEquals("HAPI FHIR Subscriptions", messages[0].getSubject());
		assertEquals(1, messages[0].getFrom().length);
		assertEquals("noreply@unknown.com", ((InternetAddress) messages[0].getFrom()[0]).getAddress());
		assertEquals(2, messages[0].getAllRecipients().length);
		assertEquals("to1@example.com", ((InternetAddress) messages[0].getAllRecipients()[0]).getAddress());
		assertEquals("to2@example.com", ((InternetAddress) messages[0].getAllRecipients()[1]).getAddress());
		assertEquals(1, messages[0].getHeader("Content-Type").length);
		assertEquals("text/plain; charset=UTF-8", messages[0].getHeader("Content-Type")[0]);
		String foundBody = GreenMailUtil.getBody(messages[0]);
		assertEquals("", foundBody);
	}

	private IMailSvc withMailService() {
		final MailConfig mailConfig = new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ourGreenMail.getSmtp().getPort());
		return new MailSvc(mailConfig);
	}

}
