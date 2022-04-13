package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.triggering.ISubscriptionTriggeringSvc;
import ca.uhn.fhir.jpa.subscription.triggering.SubscriptionTriggeringSvcImpl;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Test the rest-hook subscriptions
 */
@SuppressWarnings("Duplicates")
public class SubscriptionTriggeringDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionTriggeringDstu3Test.class);
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static final List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static final List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static final List<Patient> ourCreatedPatients = Collections.synchronizedList(Lists.newArrayList());
	private static final List<Patient> ourUpdatedPatients = Collections.synchronizedList(Lists.newArrayList());
	private static final List<String> ourContentTypes = Collections.synchronizedList(Lists.newArrayList());
	private final List<IIdType> mySubscriptionIds = Collections.synchronizedList(Lists.newArrayList());

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;
	@Autowired
	private ISubscriptionTriggeringSvc mySubscriptionTriggeringSvc;
	@Autowired
	private ISchedulerService mySchedulerService;

	@AfterEach
	public void afterUnregisterRestHookListener() {
		ourLog.info("**** Starting @AfterEach *****");

		for (IIdType next : mySubscriptionIds) {
			ourClient.delete().resourceById(next).execute();
		}
		mySubscriptionIds.clear();

		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		ourClient.delete().resourceConditionalByUrl("Subscription?_lastUpdated=lt3000").execute();
		ourClient.delete().resourceConditionalByUrl("Observation?_lastUpdated=lt3000").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		svc.cancelAll();
		svc.setMaxSubmitPerPass(null);

		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerRestHookInterceptor();
	}

	/**
	 * Only do counter resets here! We call this inside tests
	 */
	@BeforeEach
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourCreatedPatients.clear();
		ourUpdatedPatients.clear();
		ourContentTypes.clear();

		mySchedulerService.logStatusForUnitTest();
	}

	private Subscription createSubscription(String theCriteria, String thePayload, String theEndpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(thePayload);
		channel.setEndpoint(theEndpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId());
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
	public void testTriggerResourceToSpecificSubscription() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		IdType subscriptionId = createSubscription(criteria1, payload, ourListenerServerBase).getIdElement().withResourceType("Subscription");
		createSubscription(criteria2, payload, ourListenerServerBase).getIdElement();

		IdType obsId = sendObservation(code, "SNOMED-CT").getIdElement().withResourceType("Observation");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		Parameters response = ourClient
			.operation()
			.onInstance(subscriptionId)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID, new UriType(obsId.toUnqualifiedVersionless().getValue()))
			.execute();

		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		waitForQueueToDrain();

		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForSize(0, ourCreatedObservations);
		waitForSize(2, ourUpdatedObservations);

	}

	@Test
	public void testTriggerUsingMultipleSearches() throws Exception {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(13, 22, 100));

		String payload = "application/fhir+json";
		IdType sub1id = createSubscription("Observation?", payload, ourListenerServerBase).getIdElement();
		IdType sub2id = createSubscription("Patient?", payload, ourListenerServerBase).getIdElement();

		// Create lots
		for (int i = 0; i < 50; i++) {
			Patient p = new Patient();
			p.addName().setFamily("P" + i);
			ourClient.create().resource(p).execute();
		}
		for (int i = 0; i < 50; i++) {
			Observation o = new Observation();
			o.setId("O" + i);
			o.setStatus(Observation.ObservationStatus.FINAL);
			o.getCode().setText("O" + i);
			ourClient.update().resource(o).execute();
		}

		waitForSize(50, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(50, ourUpdatedPatients);
		beforeReset();

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		svc.setMaxSubmitPerPass(33);

		Parameters response = ourClient
			.operation()
			.onInstance(sub1id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Observation?"))
			.andParameter(ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID, new UriType("Observation/O2"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		response = ourClient
			.operation()
			.onInstance(sub2id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?"))
			.execute();
		responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		mySubscriptionTriggeringSvc.runDeliveryPass();
		waitForSize(33, ourUpdatedObservations);

		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		waitForSize(51, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(50, ourUpdatedPatients);
		ourLog.info("Updated patients: {}", ourUpdatedPatients.stream().map(t->t.getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList()));
	}

	@Test
	public void testTriggerUsingOrSeparatedList_MultipleStrings() throws Exception {
		String payload = "application/fhir+json";
		IdType sub2id = createSubscription("Patient?", payload, ourListenerServerBase).getIdElement();

		// Create lots
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("P" + i);
			p.addName().setFamily("P" + i);
			ourClient.update().resource(p).execute();
		}
		waitForSize(10, ourUpdatedPatients);

		// Use multiple strings
		beforeReset();
		Parameters response = ourClient
			.operation()
			.onInstance(sub2id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?_id=P0"))
			.andParameter(ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?_id=P1"))
			.andParameter(ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?_id=P2"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		assertEquals(0, svc.getActiveJobCount());

		assertEquals(0, ourCreatedPatients.size());
		await().until(() -> ourUpdatedPatients.size() == 3);

	}

	@Test
	public void testTriggerUsingOrSeparatedList_SingleString() throws Exception {
		myDaoConfig.setSearchPreFetchThresholds(Lists.newArrayList(13, 22, 100));

		String payload = "application/fhir+json";
		IdType sub2id = createSubscription("Patient?", payload, ourListenerServerBase).getIdElement();

		// Create lots
		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.setId("P" + i);
			p.addName().setFamily("P" + i);
			ourClient.update().resource(p).execute();
		}
		waitForSize(10, ourUpdatedPatients);

		// Use a single
		beforeReset();
		Parameters response = ourClient
			.operation()
			.onInstance(sub2id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?_id=P0,P1,P2"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForSize(0, ourCreatedPatients);
		waitForSize(3, ourUpdatedPatients);

	}

	@Test
	public void testTriggerUsingSearchesWithCount() throws Exception {
		String payload = "application/fhir+json";
		IdType sub1id = createSubscription("Observation?", payload, ourListenerServerBase).getIdElement();
		IdType sub2id = createSubscription("Patient?", payload, ourListenerServerBase).getIdElement();

		// Create lots
		for (int i = 0; i < 50; i++) {
			Patient p = new Patient();
			p.addName().setFamily("P" + i);
			ourClient.create().resource(p).execute();
		}
		for (int i = 0; i < 50; i++) {
			Observation o = new Observation();
			o.setId("O" + i);
			o.setStatus(Observation.ObservationStatus.FINAL);
			o.getCode().setText("O" + i);
			ourClient.update().resource(o).execute();
		}

		waitForSize(50, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(50, ourUpdatedPatients);
		beforeReset();

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		svc.setMaxSubmitPerPass(33);

		Parameters response = ourClient
			.operation()
			.onInstance(sub1id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Observation?_count=10"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		response = ourClient
			.operation()
			.onInstance(sub2id)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Patient?_count=16"))
			.execute();
		responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		mySubscriptionTriggeringSvc.runDeliveryPass();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForSize(10, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(16, ourUpdatedPatients);

	}

	@Test
	public void testTriggerUsingInvalidSearchUrl() {

		try {
			ourClient
				.operation()
				.onType(Subscription.class)
				.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
				.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Observation"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(24) + "Search URL is not valid (must be in the form \"[resource type]?[optional params]\")", e.getMessage());
		}
	}

	@Test
	public void testTriggerAllSubscriptions() throws Exception {
		String payload = "application/fhir+json";
		IdType sub1id = createSubscription("Observation?", payload, ourListenerServerBase).getIdElement();
		IdType sub2id = createSubscription("Observation?status=final", payload, ourListenerServerBase).getIdElement();

		for (int i = 0; i < 10; i++) {
			Observation o = new Observation();
			o.setId("O" + i);
			o.setStatus(Observation.ObservationStatus.FINAL);
			o.getCode().setText("O" + i);
			ourClient.update().resource(o).execute();
		}

		waitForSize(20, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(0, ourUpdatedPatients);
		beforeReset();

		SubscriptionTriggeringSvcImpl svc = ProxyUtil.getSingletonTarget(mySubscriptionTriggeringSvc, SubscriptionTriggeringSvcImpl.class);
		svc.setMaxSubmitPerPass(50);

		Parameters response = ourClient
			.operation()
			.onType(Subscription.class)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_SEARCH_URL, new StringType("Observation?"))
			.execute();
		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForSize(20, ourUpdatedObservations);
		waitForSize(0, ourCreatedObservations);
		waitForSize(0, ourCreatedPatients);
		waitForSize(0, ourUpdatedPatients);

	}

	@Test
	public void testTriggerResourceToSpecificSubscriptionWhichDoesntMatch() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload, ourListenerServerBase).getIdElement().withResourceType("Subscription");
		IdType subscriptionId = createSubscription(criteria2, payload, ourListenerServerBase).getIdElement().withResourceType("Subscription");

		IdType obsId = sendObservation(code, "SNOMED-CT").getIdElement().withResourceType("Observation");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));

		Parameters response = ourClient
			.operation()
			.onInstance(subscriptionId)
			.named(JpaConstants.OPERATION_TRIGGER_SUBSCRIPTION)
			.withParameter(Parameters.class, ProviderConstants.SUBSCRIPTION_TRIGGERING_PARAM_RESOURCE_ID, new UriType(obsId.toUnqualifiedVersionless().getValue()))
			.execute();

		String responseValue = response.getParameter().get(0).getValue().primitiveValue();
		assertThat(responseValue, containsString("Subscription triggering job submitted as JOB ID"));

		waitForQueueToDrain();
		mySubscriptionTriggeringSvc.runDeliveryPass();

		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

	}


	@Override
	protected boolean shouldLogClient() {
		return false;
	}

	private void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
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

	public static class PatientListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Create");
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourCreatedPatients.add(thePatient);
			return new MethodOutcome(new IdType("Patient/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Patient thePatient, HttpServletRequest theRequest) {
			ourUpdatedPatients.add(thePatient);
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourLog.info("Received Listener Update (now have {} updates)", ourUpdatedPatients.size());
			return new MethodOutcome(new IdType("Patient/1"), false);
		}

	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu3Cached());

		ObservationListener obsListener = new ObservationListener();
		PatientListener ptListener = new PatientListener();
		ourListenerRestServer.setResourceProviders(obsListener, ptListener);

		ourListenerServer = new Server(0);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(ourListenerRestServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourListenerServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourListenerServer);
		ourListenerPort = JettyUtil.getPortForStartedServer(ourListenerServer);
		ourListenerServerBase = "http://localhost:" + ourListenerPort + "/fhir/context";
	}

	@AfterAll
	public static void stopListenerServer() throws Exception {
		JettyUtil.closeServer(ourListenerServer);
	}

}
