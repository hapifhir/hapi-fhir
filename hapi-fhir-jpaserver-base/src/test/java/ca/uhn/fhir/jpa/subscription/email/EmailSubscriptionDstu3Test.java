
package ca.uhn.fhir.jpa.subscription.email;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.subscription.RestHookTestDstu2Test;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.collect.Lists;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the rest-hook subscriptions
 */
public class EmailSubscriptionDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(EmailSubscriptionDstu3Test.class);
	private static List<Observation> ourCreatedObservations = Lists.newArrayList();
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static List<Observation> ourUpdatedObservations = Lists.newArrayList();
	private static List<String> ourContentTypes = new ArrayList<>();
	private static GreenMail ourTestSmtp;
	private List<IIdType> mySubscriptionIds = new ArrayList<>();

	@After
	public void afterUnregisterEmailListener() {
		ourLog.info("**** Starting @After *****");

		for (IIdType next : mySubscriptionIds){
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
		ourTestSmtp.purgeEmailFromAllMailboxes();;
		ourRestServer.registerInterceptor(ourEmailSubscriptionInterceptor);

		ourEmailSubscriptionInterceptor.setDefaultFromAddress("123@hapifhir.io");
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



	private Subscription createSubscription(String theCriteria, String thePayload, String theEndpoint) throws InterruptedException {
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
		createSubscription(criteria1, payload, ourListenerServerBase);
		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());
		waitForSize(1, received);

		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("123@hapifhir.io", ((InternetAddress)received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress)received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=us-ascii", received.get(0).getContentType());
		assertEquals("This is the body", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);
	}


	@Test
	public void testEmailSubscriptionWithCustom() throws Exception {
		String payload = "This is the body";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		Subscription sub1 = createSubscription(criteria1, payload, ourListenerServerBase);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, sub1.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_EMAIL_FROM)
			.setValue(new StringType("myfrom@from.com"));
		subscriptionTemp.getChannel().addExtension()
			.setUrl(JpaConstants.EXT_SUBSCRIPTION_SUBJECT_TEMPLATE)
			.setValue(new StringType("This is a subject"));
		subscriptionTemp.setIdElement(subscriptionTemp.getIdElement().toUnqualifiedVersionless());
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();


		sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		List<MimeMessage> received = Arrays.asList(ourTestSmtp.getReceivedMessages());
		waitForSize(1, received);

		assertEquals(1, received.size());
		assertEquals(1, received.get(0).getFrom().length);
		assertEquals("myfrom@from.com", ((InternetAddress)received.get(0).getFrom()[0]).getAddress());
		assertEquals(1, received.get(0).getAllRecipients().length);
		assertEquals("foo@example.com", ((InternetAddress)received.get(0).getAllRecipients()[0]).getAddress());
		assertEquals("text/plain; charset=us-ascii", received.get(0).getContentType());
		assertEquals("This is a subject", received.get(0).getSubject().toString().trim());
		assertEquals("This is the body", received.get(0).getContent().toString().trim());
		assertEquals(mySubscriptionIds.get(0).toUnqualifiedVersionless().getValue(), received.get(0).getHeader("X-FHIR-Subscription")[0]);
	}

	private void waitForQueueToDrain() throws InterruptedException {
		RestHookTestDstu2Test.waitForQueueToDrain(ourEmailSubscriptionInterceptor);
	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = PortUtil.findFreePort();
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu3());
		ourListenerServerBase = "http://localhost:" + ourListenerPort + "/fhir/context";

		ObservationListener obsListener = new ObservationListener();
		ourListenerRestServer.setResourceProviders(obsListener);

		ourListenerServer = new Server(ourListenerPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(ourListenerRestServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourListenerServer.setHandler(proxyHandler);
		ourListenerServer.start();
	}

	@AfterClass
	public static void stopListenerServer() throws Exception {
		ourListenerServer.stop();
	}

	public static class ObservationListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Create");
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourCreatedObservations.add(theObservation);
			return new MethodOutcome(new IdType("Observation/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourUpdatedObservations.add(theObservation);
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourLog.info("Received Listener Update (now have {} updates)", ourUpdatedObservations.size());
			return new MethodOutcome(new IdType("Observation/1"), false);
		}

	}

}
