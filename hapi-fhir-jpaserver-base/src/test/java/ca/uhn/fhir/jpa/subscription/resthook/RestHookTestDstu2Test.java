package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderDstu2Test;
import ca.uhn.fhir.jpa.subscription.SubscriptionTestUtil;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Subscription;
import ca.uhn.fhir.model.dstu2.resource.Subscription.Channel;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionChannelTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.SubscriptionStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.subscription.resthook.RestHookTestDstu3Test.logAllInterceptors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestDstu2Test extends BaseResourceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookTestDstu2Test.class);
	private static List<String> ourCreatedObservations = Lists.newArrayList();
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static List<String> ourUpdatedObservations = Lists.newArrayList();
	private List<IIdType> mySubscriptionIds = new ArrayList<IIdType>();

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@After
	public void afterUnregisterRestHookListener() {
		ourLog.info("** AFTER **");
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
	public void beforeRegisterRestHookListener() {
		ourLog.info("Before re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);
		mySubscriptionTestUtil.registerRestHookInterceptor();
		ourLog.info("After re-registering interceptors");
		logAllInterceptors(myInterceptorRegistry);
	}

	@Before
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
	}

	private Subscription createSubscription(String criteria, String payload, String endpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(SubscriptionStatusEnum.REQUESTED);
		subscription.setCriteria(criteria);

		Channel channel = new Channel();
		channel.setType(SubscriptionChannelTypeEnum.REST_HOOK);
		channel.setPayload(payload);
		channel.setEndpoint(endpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		waitForQueueToDrain();

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

		IIdType id = myObservationDao.create(observation).getId();
		observation.setId(id);

		return observation;
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

	@Test
	public void testRestHookSubscriptionJson() throws Exception {
		String payload = "application/json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		String allInterceptors = myInterceptorRegistry
			.getAllRegisteredInterceptors()
			.stream()
			.map(t->t.getClass().toString())
			.sorted()
			.collect(Collectors.joining("\n * "));
		ourLog.info("Current interceptors:\n * {}", allInterceptors);

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		// Update subscription 2 to match as well
		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();
		waitForQueueToDrain();

		Observation observation2 = sendObservation(code, "SNOMED-CT");
		waitForQueueToDrain();

		// Should see one subscription notification
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		// Delet one subscription
		ourClient.delete().resourceById(new IdDt("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConceptDt codeableConcept = new CodeableConceptDt();
		observation3.setCode(codeableConcept);
		CodingDt coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConceptDt codeableConcept1 = new CodeableConceptDt();
		observation3a.setCode(codeableConcept1);
		CodingDt coding1 = codeableConcept1.addCoding();
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
	public void testRestHookSubscriptionXml() throws Exception {
		String payload = "application/xml";

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

		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();

		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		ourClient.delete().resourceById(new IdDt("Subscription/" + subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConceptDt codeableConcept = new CodeableConceptDt();
		observation3.setCode(codeableConcept);
		CodingDt coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConceptDt codeableConcept1 = new CodeableConceptDt();
		observation3a.setCode(codeableConcept1);
		CodingDt coding1 = codeableConcept1.addCoding();
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

	private void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = PortUtil.findFreePort();
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu2());
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
		public MethodOutcome create(@ResourceParam Observation theObservation) {
			ourLog.info("Received Listener Create");
			ourCreatedObservations.add(theObservation.getIdElement().toUnqualified().getValue());
			return new MethodOutcome(new IdDt("Observation/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Observation theObservation) {
			ourLog.info("Received Listener Update");
			ourUpdatedObservations.add(theObservation.getIdElement().toUnqualified().getValue());
			return new MethodOutcome(new IdDt("Observation/1"), false);
		}

	}

}
