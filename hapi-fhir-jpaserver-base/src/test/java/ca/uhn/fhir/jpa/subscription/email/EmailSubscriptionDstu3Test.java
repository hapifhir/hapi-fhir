package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.subscription.RestHookTestDstu2Test;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.google.common.collect.Lists;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 * Test the rest-hook subscriptions
 */
public class EmailSubscriptionDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmailSubscriptionDstu3Test.class);
	private static List<Observation> ourCreatedObservations = Lists.newArrayList();
	private static int ourListenerPort;
	private static List<String> ourContentTypes = new ArrayList<>();
	private static GreenMail ourTestSmtp;
	private List<IIdType> mySubscriptionIds = new ArrayList<>();

	@After
	public void afterUnregisterEmailListener() {
		ourLog.info("**** Starting @After *****");

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

		ourRestServer.unregisterInterceptor(ourEmailSubscriptionInterceptor);

	}

	@Before
	public void beforeRegisterEmailListener() throws FolderException {
		ourTestSmtp.purgeEmailFromAllMailboxes();
		;
		ourRestServer.registerInterceptor(ourEmailSubscriptionInterceptor);

		JavaMailEmailSender emailSender = new JavaMailEmailSender();
		emailSender.setSmtpServerHostname("localhost");
		emailSender.setSmtpServerPort(ourListenerPort);
		emailSender.start();

		ourEmailSubscriptionInterceptor.setEmailSender(emailSender);
		ourEmailSubscriptionInterceptor.setDefaultFromAddress("123@hapifhir.io");
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

	@Test
	public void testEmailSubscriptionNormal() throws Exception {
		String payload = "This is the body";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		createSubscription(criteria1, payload);
		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("123@hapifhir.io", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=us-ascii", received.get(0).getContentType());
		assertEquals("This is the body", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);
	}

	@Test
	public void testEmailSubscriptionWithCustom() throws Exception {
		String payload = "This is the body";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription sub1 = createSubscription(criteria1, payload);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, sub1.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_EMAIL_FROM)
			.setValue(new StringType("mailto:myfrom@from.com"));
		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE)
			.setValue(new StringType("This is a subject"));
		subscriptionTemp.setIdElement(subscriptionTemp.getIdElement().toUnqualifiedVersionless());


		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(subscriptionTemp));

		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();


		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());
		assertEquals(1, received.size());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("myfrom@from.com", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=us-ascii", received.get(0).getContentType());
		assertEquals("This is a subject", received.get(0).getSubject().toString().trim());
		assertEquals("This is the body", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);
	}

	@Test
	public void testEmailSubscriptionWithCustomNoMailtoOnFrom() throws Exception {
		String payload = "This is the body";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription sub1 = createSubscription(criteria1, payload);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, sub1.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_EMAIL_FROM)
			.setValue(new StringType("myfrom@from.com"));
		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE)
			.setValue(new StringType("This is a subject"));
		subscriptionTemp.setIdElement(subscriptionTemp.getIdElement().toUnqualifiedVersionless());

		IIdType id = ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute().getId();
		ourLog.info("Subscription ID is: {}", id.getValue());

		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());
		assertEquals(1, received.size());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("myfrom@from.com", ((InternetAddress) received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress) received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=us-ascii", received.get(0).getContentType());
		assertEquals("This is a subject", received.get(0).getSubject().toString().trim());
		assertEquals("This is the body", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		ourLog.info("Subscription: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(ourClient.history().onInstance(id).andReturnBundle(Bundle.class).execute()));

		Subscription subscription = ourClient.read().resource(Subscription.class).withId(id.toUnqualifiedVersionless()).execute();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus());
		assertEquals("3", subscription.getIdElement().getVersionIdPart());
	}

	private void waitForQueueToDrain() throws InterruptedException {
		RestHookTestDstu2Test.waitForQueueToDrain(ourEmailSubscriptionInterceptor);
	}

	@AfterClass
	public static void afterClass() {
		ourTestSmtp.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		ourListenerPort = RandomServerPortProvider.findFreePort();
		ServerSetup smtp = new ServerSetup(ourListenerPort, null, ServerSetup.PROTOCOL_SMTP);
		smtp.setServerStartupTimeout(2000);
		smtp.setReadTimeout(2000);
		smtp.setConnectionTimeout(2000);
		ourTestSmtp = new GreenMail(smtp);
		ourTestSmtp.start();
	}

}
