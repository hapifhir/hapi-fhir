package ca.uhn.fhir.jpa.subscription.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
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
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.springframework.messaging.support.ExecutorSubscribableChannel;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookTestDstu2Test.class);
	private static List<Observation> ourCreatedObservations = Lists.newArrayList();
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static List<Observation> ourUpdatedObservations = Lists.newArrayList();
	private static List<String> ourContentTypes = new ArrayList<>();
	private static List<String> ourHeaders = new ArrayList<>();
	private List<IIdType> mySubscriptionIds = new ArrayList<>();
	private CountingInterceptor myCountingInterceptor;

	@After
	public void afterUnregisterRestHookListener() {
		for (IIdType next : mySubscriptionIds) {
			IIdType nextId = next.toUnqualifiedVersionless();
			ourLog.info("Deleting: {}", nextId);
			ourClient.delete().resourceById(nextId).execute();
		}
		mySubscriptionIds.clear();

		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		ourClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		ourClient.delete().resourceConditionalByUrl("Observation?code:missing=false").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		ourRestServer.unregisterInterceptor(getRestHookSubscriptionInterceptor());
	}

	@Before
	public void beforeRegisterRestHookListener() {
		ourRestServer.registerInterceptor(getRestHookSubscriptionInterceptor());
	}

	@Before
	public void beforeReset() throws Exception {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		ourHeaders.clear();

		// Delete all Subscriptions
		Bundle allSubscriptions = ourClient.search().forResource(Subscription.class).returnBundle(Bundle.class).execute();
		for (IBaseResource next : BundleUtil.toListOfResources(myFhirCtx, allSubscriptions)) {
			ourClient.delete().resource(next).execute();
		}
		waitForRegisteredSubscriptionCount(0);

		ExecutorSubscribableChannel processingChannel = (ExecutorSubscribableChannel) getRestHookSubscriptionInterceptor().getProcessingChannel();
		processingChannel.setInterceptors(new ArrayList<>());
		myCountingInterceptor = new CountingInterceptor();
		processingChannel.addInterceptor(myCountingInterceptor);
	}

	private Subscription createSubscription(String theCriteria, String thePayload, String theEndpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(thePayload);
		channel.setEndpoint(theEndpoint);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

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
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload, ourListenerServerBase);
		createSubscription(criteria2, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(2);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testActiveSubscriptionShouldntReActivate() throws Exception {
		String criteria = "Observation?code=111111111&_format=xml";
		String payload = "application/fhir+json";
		createSubscription(criteria, payload, ourListenerServerBase);

		waitForRegisteredSubscriptionCount(1);
		for (int i = 0; i < 5; i++) {
			Integer changes = ourReskHookSubscriptionInterceptor.doInitSubscriptions();
			assertEquals(0, changes.intValue());
		}
	}

	@Test
	public void testRestHookSubscriptionNoopUpdateDoesntTriggerNewDelivery() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload, ourListenerServerBase);
		createSubscription(criteria2, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(2);

		Observation obs = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		// Send an update with no changes
		obs.setId(obs.getIdElement().toUnqualifiedVersionless());
		ourClient.update().resource(obs).execute();

		// Should be no further deliveries
		Thread.sleep(1000);
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);


	}

	@Test
	public void testRestHookSubscriptionApplicationJsonDisableVersionIdInDelivery() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		waitForRegisteredSubscriptionCount(0);
		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(1);

		int modCount = myCountingInterceptor.getSentCount();
		subscription1
			.getChannel()
			.addExtension(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS, new BooleanType("true"));
		subscription1
			.getChannel()
			.addExtension(JpaConstants.EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION, new BooleanType("true"));
		ourLog.info("** About to update subscription");
		ourClient.update().resource(subscription1).execute();
		waitForSize(modCount + 1, () -> myCountingInterceptor.getSentCount());

		ourLog.info("** About to send observation");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		assertEquals(observation1.getIdElement().getIdPart(), ourUpdatedObservations.get(0).getIdElement().getIdPart());
		assertEquals(null, ourUpdatedObservations.get(0).getIdElement().getVersionIdPart());
	}

	@Test
	public void testRestHookSubscriptionApplicationJson() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		assertEquals("1", ourUpdatedObservations.get(0).getIdElement().getVersionIdPart());

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForQueueToDrain();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see only one subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
		Assert.assertFalse(observation1.getId().isEmpty());
		Assert.assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testRestHookSubscriptionApplicationXml() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(2);

		ourLog.info("** About to send obervation");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see two subscription notifications
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
		Assert.assertFalse(observation1.getId().isEmpty());
		Assert.assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testUpdateSubscriptionToMatchLater() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteriaBad = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		ourLog.info("** About to create non-matching subscription");

		Subscription subscription2 = createSubscription(criteriaBad, payload, ourListenerServerBase);

		ourLog.info("** About to send observation that wont match");

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Criteria didn't match, shouldn't see any updates
		waitForQueueToDrain();
		Thread.sleep(1000);
		assertEquals(0, ourUpdatedObservations.size());

		Subscription subscriptionTemp = ourClient.read().resource(Subscription.class).withId(subscription2.getId()).execute();
		Assert.assertNotNull(subscriptionTemp);
		String criteriaGood = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		subscriptionTemp.setCriteria(criteriaGood);
		ourLog.info("** About to update subscription");
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		ourLog.info("** About to send Observation 2");
		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see a subscription notification this time
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// No more matches
		Thread.sleep(1000);
		assertEquals(1, ourUpdatedObservations.size());
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(2);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	// TODO: reenable
	@Test
	@Ignore
	public void testRestHookSubscriptionInvalidCriteria() throws Exception {
		String payload = "application/xml";

		String criteria1 = "Observation?codeeeee=SNOMED-CT";

		try {
			createSubscription(criteria1, payload, ourListenerServerBase);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Invalid criteria: Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
		}
	}

	@Test
	public void testSubscriptionWithHeaders() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		// Add some headers, and we'll also turn back to requested status for fun
		Subscription subscription = createSubscription(criteria1, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(1);

		subscription.getChannel().addHeader("X-Foo: FOO");
		subscription.getChannel().addHeader("X-Bar: BAR");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		ourClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
		assertThat(ourHeaders, hasItem("X-Foo: FOO"));
		assertThat(ourHeaders, hasItem("X-Bar: BAR"));
	}

	@Test
	public void testDisableSubscription() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		Subscription subscription = createSubscription(criteria1, payload, ourListenerServerBase);
		waitForRegisteredSubscriptionCount(1);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		// Disable
		subscription.setStatus(Subscription.SubscriptionStatus.OFF);
		ourClient.update().resource(subscription).execute();
		waitForQueueToDrain();

		// Send another object
		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

	}

	private void waitForQueueToDrain() throws InterruptedException {
		RestHookTestDstu2Test.waitForQueueToDrain(getRestHookSubscriptionInterceptor());
	}

	public static class ObservationListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Create");
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourCreatedObservations.add(theObservation);
			extractHeaders(theRequest);
			return new MethodOutcome(new IdType("Observation/1"), true);
		}

		private void extractHeaders(HttpServletRequest theRequest) {
			Enumeration<String> headerNamesEnum = theRequest.getHeaderNames();
			while (headerNamesEnum.hasMoreElements()) {
				String nextName = headerNamesEnum.nextElement();
				Enumeration<String> valueEnum = theRequest.getHeaders(nextName);
				while (valueEnum.hasMoreElements()) {
					String nextValue = valueEnum.nextElement();
					ourHeaders.add(nextName + ": " + nextValue);
				}
			}
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Update");
			ourUpdatedObservations.add(theObservation);
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			extractHeaders(theRequest);
			return new MethodOutcome(new IdType("Observation/1"), false);
		}

	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = PortUtil.findFreePort();
		ourListenerRestServer = new RestfulServer(FhirContext.forR4());
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

}
