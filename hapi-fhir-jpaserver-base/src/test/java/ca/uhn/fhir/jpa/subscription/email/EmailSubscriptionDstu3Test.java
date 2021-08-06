package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.EmailSenderImpl;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.mail.MailConfig;
import ca.uhn.fhir.util.HapiExtensions;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ca.uhn.fhir.jpa.subscription.resthook.RestHookTestDstu3Test.logAllInterceptors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test the rest-hook subscriptions
 */
public class EmailSubscriptionDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmailSubscriptionDstu3Test.class);

	@RegisterExtension
	static GreenMailExtension ourGreenMail = new GreenMailExtension(ServerSetupTest.SMTP);

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	private List<IIdType> mySubscriptionIds = new ArrayList<>();

	@AfterEach
	public void afterUnregisterEmailListener() {
		ourLog.info("**** Starting @AfterEach *****");

		for (IIdType next : mySubscriptionIds) {
			ourClient.delete().resourceById(next).execute();
		}
		mySubscriptionIds.clear();

		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		ourClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		ourClient.delete().resourceConditionalByUrl("Observation?code:missing=false").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@BeforeEach
	public void beforeRegisterEmailListener() throws FolderException {
		ourGreenMail.purgeEmailFromAllMailboxes();

		ourLog.info("Before re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);

		mySubscriptionTestUtil.registerEmailInterceptor();

		ourLog.info("After re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);

		myDaoConfig.setEmailFromAddress("123@hapifhir.io");
	}

	private Subscription createSubscription(String theCriteria, String thePayload) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.EMAIL);
		channel.setPayload(thePayload);
		channel.setEndpoint("mailto:foo@example.com");
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		waitForQueueToDrain();

		return subscription;
	}

	private Observation sendObservation(String code, String system) {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		MethodOutcome methodOutcome = ourClient.create().resource(observation).execute();

		String observationId = methodOutcome.getId().getIdPart();
		observation.setId(observationId);

		return observation;
	}

	/**
	 * Tests an email subscription with payload set to XML. The email sent must include content in the body of the email that is formatted as XML.
	 */
	@Test
	public void testEmailSubscriptionNormal() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = createSubscription(criteria1, payload);
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscription.getIdElement(), new EmailSenderImpl(withMailConfig()));

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 1));

		List<MimeMessage> received = Arrays.asList(ourGreenMail.getReceivedMessages());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("123@hapifhir.io", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=UTF-8", received.get(0).getContentType());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		// Expect the body of the email subscription to be an Observation formatted as XML
		Observation parsedObservation = (Observation) ourClient.getFhirContext().newXmlParser().parseResource(received.get(0).getContent().toString().trim());
		assertEquals("SNOMED-CT", parsedObservation.getCode().getCodingFirstRep().getSystem());
		assertEquals("1000000050", parsedObservation.getCode().getCodingFirstRep().getCode());
	}

	/**
	 * Tests an email subscription with payload set to JSON. The email sent must include content in the body of the email that is formatted as JSON.
	 */
	@Test
	public void testEmailSubscriptionWithCustom() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription sub1 = createSubscription(criteria1, payload);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, sub1.getId());
		assertNotNull(subscriptionTemp);

		subscriptionTemp.getChannel().addExtension()
			.setUrl(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM)
			.setValue(new StringType("mailto:myfrom@from.com"));
		subscriptionTemp.getChannel().addExtension()
			.setUrl(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE)
			.setValue(new StringType("This is a subject"));
		subscriptionTemp.setIdElement(subscriptionTemp.getIdElement().toUnqualifiedVersionless());

		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(subscriptionTemp));

		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscriptionTemp.getIdElement(), new EmailSenderImpl(withMailConfig()));

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 1));

		List<MimeMessage> received = Arrays.asList(ourGreenMail.getReceivedMessages());
		assertEquals(1, received.size());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("myfrom@from.com", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=UTF-8", received.get(0).getContentType());
		assertEquals("This is a subject", received.get(0).getSubject().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		// Expect the body of the email subscription to be an Observation formatted as JSON
		Observation parsedObservation = (Observation) ourClient.getFhirContext().newJsonParser().parseResource(received.get(0).getContent().toString().trim());
		assertEquals("SNOMED-CT", parsedObservation.getCode().getCodingFirstRep().getSystem());
		assertEquals("1000000050", parsedObservation.getCode().getCodingFirstRep().getCode());
	}

	/**
	 * Tests an email subscription with no payload. When the email is sent, the body of the email must be empty.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionWithCustomNoMailtoOnFrom() throws Exception {
		String payload = "";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription sub1 = createSubscription(criteria1, payload);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, sub1.getId());
		assertNotNull(subscriptionTemp);
		subscriptionTemp.getChannel().addExtension()
			.setUrl(HapiExtensions.EXT_SUBSCRIPTION_EMAIL_FROM)
			.setValue(new StringType("myfrom@from.com"));
		subscriptionTemp.getChannel().addExtension()
			.setUrl(HapiExtensions.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE)
			.setValue(new StringType("This is a subject"));
		subscriptionTemp.setIdElement(subscriptionTemp.getIdElement().toUnqualifiedVersionless());

		IIdType id = ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute().getId();
		ourLog.info("Subscription ID is: {}", id.getValue());

		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscriptionTemp.getIdElement(), new EmailSenderImpl(withMailConfig()));

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		assertTrue(ourGreenMail.waitForIncomingEmail(1000, 1));

		List<MimeMessage> received = Arrays.asList(ourGreenMail.getReceivedMessages());
		assertEquals(1, received.size());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("myfrom@from.com", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=UTF-8", received.get(0).getContentType());
		assertEquals("This is a subject", received.get(0).getSubject().toString().trim());
		assertEquals("", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		ourLog.info("Subscription: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(ourClient.history().onInstance(id).andReturnBundle(Bundle.class).execute()));

		Subscription subscription = ourClient.read().resource(Subscription.class).withId(id.toUnqualifiedVersionless()).execute();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus());
		assertEquals("3", subscription.getIdElement().getVersionIdPart());
	}

	private void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
	}

	private MailConfig withMailConfig() {
		return new MailConfig()
			.setSmtpHostname(ServerSetupTest.SMTP.getBindAddress())
			.setSmtpPort(ServerSetupTest.SMTP.getPort());
	}


}
