package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.subscription.NotificationServlet;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.module.interceptor.SubscriptionDebugLogInterceptor;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchingStrategy;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookTestDstu3Test.class);
	private static List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	private static NotificationServlet ourNotificationServlet;
	private static String ourNotificationListenerServer;
	private static CountDownLatch communicationRequestListenerLatch;
	private static SubscriptionDebugLogInterceptor ourSubscriptionDebugLogInterceptor = new SubscriptionDebugLogInterceptor();
	private List<IIdType> mySubscriptionIds = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@After
	public void afterUnregisterRestHookListener() {
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
		myInterceptorRegistry.unregisterInterceptor(ourSubscriptionDebugLogInterceptor);
	}

	@Before
	public void beforeRegisterRestHookListener() {
		ourLog.info("Before re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);
		mySubscriptionTestUtil.registerRestHookInterceptor();
		myInterceptorRegistry.registerInterceptor(ourSubscriptionDebugLogInterceptor);
		ourLog.info("After re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);
	}

	@Before
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		ourNotificationServlet.reset();
	}

	private Subscription createSubscription(String criteria, String payload, String endpoint) throws InterruptedException {
		return createSubscription(criteria, payload, endpoint, null);
	}

	private Subscription createSubscription(String theCriteria, String thePayload, String theEndpoint,
														 List<StringType> headers) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(thePayload);
		channel.setEndpoint(theEndpoint);
		if (headers != null) {
			channel.setHeader(headers);
		}
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		mySubscriptionIds.add(methodOutcome.getId());

		waitForQueueToDrain();

		return (Subscription) methodOutcome.getResource();
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
	public void testDatabaseStrategyMeta() throws InterruptedException {
		String databaseCriteria = "Observation?code=17861-6&context.type=IHD";
		Subscription subscription = createSubscription(databaseCriteria, null, ourNotificationListenerServer);
		List<Coding> tag = subscription.getMeta().getTag();
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.get(0).getSystem());
		assertEquals(SubscriptionMatchingStrategy.DATABASE.toString(), tag.get(0).getCode());
	}

	@Test
	public void testMemorytrategyMeta() throws InterruptedException {
		String inMemoryCriteria = "Observation?code=17861-6";
		Subscription subscription = createSubscription(inMemoryCriteria, null, ourNotificationListenerServer);
		List<Coding> tag = subscription.getMeta().getTag();
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.get(0).getSystem());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY.toString(), tag.get(0).getCode());
	}

	@Test
	public void testRestHookSubscription() throws Exception {
		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, null, ourNotificationListenerServer,
			Collections.singletonList(new StringType("Authorization: abc-def")));
		createSubscription(criteria2, null, ourNotificationListenerServer);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification with authorization header
		waitForSize(1, ourNotificationServlet.getReceivedAuthorizationHeaders());
		Assert.assertEquals(1, ourNotificationServlet.getReceivedNotificationCount());
		Assert.assertEquals("abc-def", ourNotificationServlet.getReceivedAuthorizationHeaders().get(0));
		ourNotificationServlet.reset();

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification with authorization header
		waitForSize(1, ourNotificationServlet.getReceivedAuthorizationHeaders());
		Assert.assertEquals(1, ourNotificationServlet.getReceivedNotificationCount());
		Assert.assertEquals("abc-def", ourNotificationServlet.getReceivedAuthorizationHeaders().get(0));
		ourNotificationServlet.reset();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		/// Should see 1 subscription notification with authorization header
		waitForSize(1, ourNotificationServlet.getReceivedAuthorizationHeaders());
		Assert.assertEquals(1, ourNotificationServlet.getReceivedNotificationCount());
		Assert.assertEquals("abc-def", ourNotificationServlet.getReceivedAuthorizationHeaders().get(0));
		ourNotificationServlet.reset();

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see 2 subscription notifications with and without authorization header
		waitForSize(1, ourNotificationServlet.getReceivedAuthorizationHeaders());
		Assert.assertEquals(1, ourNotificationServlet.getReceivedNotificationCount());
		Assert.assertNull(ourNotificationServlet.getReceivedAuthorizationHeaders().get(0));
		ourNotificationServlet.reset();
	}

	@Test
	public void testRestHookSubscriptionApplicationFhirJson() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload, ourListenerServerBase);
		createSubscription(criteria2, payload, ourListenerServerBase);

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionApplicationJson() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		// Modify subscription 2 to also match
		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		// Send another
		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForQueueToDrain();

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
	public void testRestHookSubscriptionApplicationXml() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		ourLog.info("About to send observation 1");
		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));

		// Modify subscription 2 to also match
		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);
		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		// Send another observation
		ourLog.info("About to send observation 2");
		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see two subscription notifications
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdType("Subscription/" + subscription2.getId())).execute();
		waitForQueueToDrain();

		// Send another
		ourLog.info("About to send observation 3");
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

		Assert.assertNotEquals(subscription1.getId(), subscription2.getId());
		Assert.assertFalse(observation1.getId().isEmpty());
		Assert.assertFalse(observation2.getId().isEmpty());
	}

	@Test
	public void testRestHookSubscriptionApplicationXmlJson() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	@Test
	public void testRestHookSubscriptionWithoutPayload() throws Exception {
		String payload = "";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code;
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification, but no payload
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourUpdatedObservations);
	}

	@Test
	public void testRestHookSubscriptionInvalidCriteria() throws Exception {
		String payload = "application/xml";

		String criteria1 = "Observation?codeeeee=SNOMED-CT";

		try {
			createSubscription(criteria1, payload, ourListenerServerBase);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("HTTP 422 Unprocessable Entity: Invalid subscription criteria submitted: Observation?codeeeee=SNOMED-CT Failed to parse match URL[Observation?codeeeee=SNOMED-CT] - Resource type Observation does not have a parameter with name: codeeeee", e.getMessage());
		}
	}

	private void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
	}

	@Test
	public void testSubscriptionActivatesInMemoryTag() throws Exception {
		String payload = "application/fhir+xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";

		Subscription subscriptionOrig = createSubscription(criteria1, payload, ourListenerServerBase);
		IdType subscriptionId = subscriptionOrig.getIdElement();

		assertEquals(Subscription.SubscriptionStatus.REQUESTED, subscriptionOrig.getStatus());
		List<Coding> tags = subscriptionOrig.getMeta().getTag();
		assertEquals(1, tags.size());
		Coding tag = tags.get(0);
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.getSystem());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY.toString(), tag.getCode());
		assertEquals("In-memory", tag.getDisplay());

		Subscription subscriptionActivated = ourClient.read().resource(Subscription.class).withId(subscriptionId.toUnqualifiedVersionless()).execute();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscriptionActivated.getStatus());
		tags = subscriptionActivated.getMeta().getTag();
		assertEquals(1, tags.size());
		tag = tags.get(0);
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.getSystem());
		assertEquals(SubscriptionMatchingStrategy.IN_MEMORY.toString(), tag.getCode());
		assertEquals("In-memory", tag.getDisplay());
	}

	@Test
	public void testSubscriptionActivatesDatabaseTag() throws Exception {
		String payload = "application/fhir+xml";

		Subscription subscriptionOrig = createSubscription("Observation?code=17861-6&context.type=IHD", payload, ourListenerServerBase);
		IdType subscriptionId = subscriptionOrig.getIdElement();

		List<Coding> tags = subscriptionOrig.getMeta().getTag();
		assertEquals(1, tags.size());
		Coding tag = tags.get(0);
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.getSystem());
		assertEquals(SubscriptionMatchingStrategy.DATABASE.toString(), tag.getCode());
		assertEquals("Database", tag.getDisplay());

		Subscription subscription = ourClient.read().resource(Subscription.class).withId(subscriptionId.toUnqualifiedVersionless()).execute();
		assertEquals(Subscription.SubscriptionStatus.ACTIVE, subscription.getStatus());
		tags = subscription.getMeta().getTag();
		assertEquals(1, tags.size());
		tag = tags.get(0);
		assertEquals(SubscriptionConstants.EXT_SUBSCRIPTION_MATCHING_STRATEGY, tag.getSystem());
		assertEquals(SubscriptionMatchingStrategy.DATABASE.toString(), tag.getCode());
		assertEquals("Database", tag.getDisplay());
	}

	@Test
	public void testCommunicationRequestWithRef() throws InterruptedException {
		Organization org = new Organization();
		MethodOutcome methodOutcome = ourClient.create().resource(org).execute();
		String orgId = methodOutcome.getId().getIdPart();

		String criteria = "CommunicationRequest?requester=1276," + orgId + "&occurrence=ge2019-02-08T00:00:00-05:00&occurrence=le2019-02-09T00:00:00-05:00";
		String payload = "application/fhir+xml";
		createSubscription(criteria, payload, ourListenerServerBase);

		CommunicationRequest cr = new CommunicationRequest();
		cr.getRequester().getAgent().setReference("Organization/" + orgId);
		cr.setOccurrence(new DateTimeType("2019-02-08T00:01:00-05:00"));
		communicationRequestListenerLatch = new CountDownLatch(1);
		ourClient.create().resource(cr).execute();
		assertTrue("Timed out waiting for subscription to match", communicationRequestListenerLatch.await(10, TimeUnit.SECONDS));
	}

	public static class ObservationListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Create");
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourCreatedObservations.add((Observation) theObservation);
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

	public static class CommunicationRequestListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam CommunicationRequest theResource, HttpServletRequest theRequest) {
			ourLog.info("Received CommunicationRequestListener Create");
			communicationRequestListenerLatch.countDown();
			return new MethodOutcome(new IdType("CommunicationRequest/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return CommunicationRequest.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam CommunicationRequest theResource, HttpServletRequest theRequest) {
			ourLog.info("Received CommunicationRequestListener Update");
			communicationRequestListenerLatch.countDown();
			return new MethodOutcome(new IdType("CommunicationRequest/1"), false);
		}
	}

	public static void logAllInterceptors(IInterceptorService theInterceptorRegistry) {
		List<Object> allInterceptors = theInterceptorRegistry.getAllRegisteredInterceptors();
		String interceptorList = allInterceptors
			.stream()
			.map(t -> t.getClass().toString())
			.sorted()
			.collect(Collectors.joining("\n * "));
		ourLog.info("Registered interceptors:\n * {}", interceptorList);
	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = PortUtil.findFreePort();
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu3());
		ourListenerServerBase = "http://localhost:" + ourListenerPort + "/fhir/context";
		ourNotificationListenerServer = "http://localhost:" + ourListenerPort + "/fhir/subscription";

		ObservationListener obsListener = new ObservationListener();
		CommunicationRequestListener crListener = new CommunicationRequestListener();
		ourListenerRestServer.setResourceProviders(obsListener, crListener);

		ourListenerServer = new Server(ourListenerPort);
		ourNotificationServlet = new NotificationServlet();

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(ourListenerRestServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");
		servletHolder = new ServletHolder();
		servletHolder.setServlet(ourNotificationServlet);
		proxyHandler.addServlet(servletHolder, "/fhir/subscription");

		ourListenerServer.setHandler(proxyHandler);
		ourListenerServer.start();
	}

	@AfterClass
	public static void stopListenerServer() throws Exception {
		ourListenerServer.stop();
	}
}
