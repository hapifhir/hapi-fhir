package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.SharedByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import static ca.uhn.fhir.jpa.subscription.resthook.RestHookTestDstu3Test.logAllInterceptors;
import static org.junit.Assert.assertEquals;

/**
 * Test the rest-hook subscriptions
 */
public class EmailSubscriptionDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmailSubscriptionDstu3Test.class);
	public static final String TEST_NARRATIVE_DIV_CONTENT = "This is a test observation for email subscription";

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	private static int ourListenerPort;
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

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@Before
	public void beforeRegisterEmailListener() throws FolderException {
		ourTestSmtp.purgeEmailFromAllMailboxes();

		ourLog.info("Before re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);

		mySubscriptionTestUtil.registerEmailInterceptor();

		ourLog.info("After re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);

		mySubscriptionTestUtil.initEmailSender(ourListenerPort);

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

		Narrative narrative = new Narrative();
		narrative.setDivAsString(TEST_NARRATIVE_DIV_CONTENT);
		observation.setText(narrative);

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
	 * Tests that an email subscription with no payload sends an email message that has no body.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionNoPayload() throws Exception {
		String payload = "";
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = createSubscription(criteria1, payload);
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscription.getIdElement());

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());

		// Expect the body of the email subscription to be a multipart mime message, with one part, that is empty
		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(1, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		String content = (String) multipart.getBodyPart(0).getContent();
		assertEquals("", content);
	}

	/**
	 * Tests that an email subscription with a mime-type of application/fhir+json and mime-type options of "bodynarrative=true"
	 * results in sending an email message where the body of the email is the narrative content of the resource that triggered
	 * the subscription, and the email has an attachment that represents the JSON of the resource.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionNarrativeBodyJsonAttach() throws Exception {
		String payload = "application/fhir+json;bodynarrative=true";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = createSubscription(criteria1, payload);
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscription.getIdElement());

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());

		// Expect the body of the email subscription to be an Observation formatted as XML
		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(2, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		String content = (String) multipart.getBodyPart(0).getContent();
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">" + TEST_NARRATIVE_DIV_CONTENT + "</div>", content);

		MimeBodyPart attachmentBodyPart = (MimeBodyPart) multipart.getBodyPart(1);
		String[] attachmentContentType = attachmentBodyPart.getHeader("Content-Type");
		String[] attachmentDisposition = attachmentBodyPart.getHeader("Content-Disposition");

		// Validate headers on the attachment body part
		assertEquals(1, attachmentContentType.length);
		assertEquals("application/json; name=resource.json", attachmentContentType[0]);
		assertEquals(1, attachmentDisposition.length);
		assertEquals("attachment; filename=resource.json", attachmentDisposition[0]);

		// Validate the content of the attachment body part
		SharedByteArrayInputStream is = (SharedByteArrayInputStream) attachmentBodyPart.getContent();
		String attachmentContent = (String) org.apache.commons.io.IOUtils.toString(is);
		Observation parsedObservation = (Observation) ourClient.getFhirContext().newJsonParser().parseResource(attachmentContent);
		assertEquals("SNOMED-CT", parsedObservation.getCode().getCodingFirstRep().getSystem());
		assertEquals("1000000050", parsedObservation.getCode().getCodingFirstRep().getCode());
	}

	/**
	 * Tests that an email subscription with a mime-type of application/fhir+xml and mime-type options of "bodytext=BASE64ENCODEDTEXT"
	 * results in sending an email message where the body of the email is the text specified in the mime-type "bodytext" option, and
	 * the XML representing the resource is an attachment on the email.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionTextBodyXmlAttach() throws Exception {
		String payload = "application/fhir+xml;bodytext=dGhpcyBpcyBzb21lIHRlc3QgdGV4dA==";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = createSubscription(criteria1, payload);
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscription.getIdElement());

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		waitForSize(1, 60000, new Callable<Number>() {
			@Override
			public Number call() throws Exception {
				return ourTestSmtp.getReceivedMessages().length;
			}
		});

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());

		// Expect the body of the email subscription to be an Observation formatted as XML
		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(2, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		String content = (String) multipart.getBodyPart(0).getContent();
		assertEquals("this is some test text", content);

		MimeBodyPart attachmentBodyPart = (MimeBodyPart) multipart.getBodyPart(1);
		String[] attachmentContentType = attachmentBodyPart.getHeader("Content-Type");
		String[] attachmentDisposition = attachmentBodyPart.getHeader("Content-Disposition");

		// Validate headers on the attachment body part
		assertEquals(1, attachmentContentType.length);
		assertEquals("application/xml; name=resource.xml", attachmentContentType[0]);
		assertEquals(1, attachmentDisposition.length);
		assertEquals("attachment; filename=resource.xml", attachmentDisposition[0]);

		// Validate the content of the attachment body part
		SharedByteArrayInputStream is = (SharedByteArrayInputStream) attachmentBodyPart.getContent();
		String attachmentContent = (String) org.apache.commons.io.IOUtils.toString(is);
		Observation parsedObservation = (Observation) ourClient.getFhirContext().newXmlParser().parseResource(attachmentContent);
		assertEquals("SNOMED-CT", parsedObservation.getCode().getCodingFirstRep().getSystem());
		assertEquals("1000000050", parsedObservation.getCode().getCodingFirstRep().getCode());
	}

	/**
	 * Tests an email subscription with payload set to XML. The email sent must include content in the body of the email that is formatted as XML.
	 * This test ensures backwards compatibility with previous versions of HAPI-FHIR.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionXml() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription subscription = createSubscription(criteria1, payload);
		waitForQueueToDrain();
		mySubscriptionTestUtil.setEmailSender(subscription.getIdElement());

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
		assertEquals(true, received.get(0).getContentType().indexOf("multipart/mixed;") == 0);
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		// Expect the body of the email subscription to be an Observation formatted as XML
		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(1, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		Observation parsedObservation = (Observation) ourClient.getFhirContext().newXmlParser().parseResource((String) multipart.getBodyPart(0).getContent());
		assertEquals("SNOMED-CT", parsedObservation.getCode().getCodingFirstRep().getSystem());
		assertEquals("1000000050", parsedObservation.getCode().getCodingFirstRep().getCode());
	}

	/**
	 * Tests an email subscription with payload set to JSON. The email sent must include content in the body of the email that is formatted as JSON.
	 * This test ensures backwards compatibility with previous versions of HAPI-FHIR.
	 * @throws Exception
	 */
	@Test
	public void testEmailSubscriptionJson() throws Exception {
		String payload = "application/fhir+json";

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
		mySubscriptionTestUtil.setEmailSender(subscriptionTemp.getIdElement());

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
		assertEquals(true, received.get(0).getContentType().indexOf("multipart/mixed;") == 0);
		assertEquals("This is a subject", received.get(0).getSubject().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(1, multipart.getCount());
		assertEquals(String.class, multipart.getBodyPart(0).getContent().getClass());

		Observation parsedObservation = (Observation) ourClient.getFhirContext().newJsonParser().parseResource((String) multipart.getBodyPart(0).getContent());
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
		mySubscriptionTestUtil.setEmailSender(subscriptionTemp.getIdElement());

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
		assertEquals(true, received.get(0).getContentType().indexOf("multipart/mixed;") == 0);
		assertEquals("This is a subject", received.get(0).getSubject().trim());

		assertEquals(MimeMultipart.class, received.get(0).getContent().getClass());
		MimeMultipart multipart = (MimeMultipart) received.get(0).getContent();
		assertEquals(1, multipart.getCount());

		String[] contentTypeValues = multipart.getBodyPart(0).getHeader("Content-Type");
		assertEquals(true, contentTypeValues != null);
		assertEquals(1, contentTypeValues.length);
		assertEquals("text/plain; charset=us-ascii", contentTypeValues[0]);
		assertEquals("", multipart.getBodyPart(0).getContent());

		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);

		ourLog.info("Subscription: {}", myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(ourClient.history().onInstance(id).andReturnBundle(Bundle.class).execute()));

		Subscription subscription = ourClient.read().resource(Subscription.class).withId(id.toUnqualifiedVersionless()).execute();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus());
		assertEquals("3", subscription.getIdElement().getVersionIdPart());
	}

	private void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
	}

	@AfterClass
	public static void afterClass() {
		ourTestSmtp.stop();
	}

	@BeforeClass
	public static void beforeClass() {
		ServerSetup smtp = new ServerSetup(0, null, ServerSetup.PROTOCOL_SMTP);
		smtp.setServerStartupTimeout(2000);
		smtp.setReadTimeout(2000);
		smtp.setConnectionTimeout(2000);
		ourTestSmtp = new GreenMail(smtp);
		ourTestSmtp.start();
        ourListenerPort = ourTestSmtp.getSmtp().getPort();
	}

}
