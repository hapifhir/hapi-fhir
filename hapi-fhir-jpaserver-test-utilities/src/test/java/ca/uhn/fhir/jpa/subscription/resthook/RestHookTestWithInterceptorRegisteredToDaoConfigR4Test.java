
package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestWithInterceptorRegisteredToDaoConfigR4Test extends BaseResourceProviderR4Test {

	private static final List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookTestWithInterceptorRegisteredToDaoConfigR4Test.class);
	private static final List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;

	@Override
	protected boolean shouldLogClient() {
		return false;
	}

	@AfterEach
	public void afterUnregisterRestHookListener() {
		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		myClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerRestHookInterceptor();
	}

	@BeforeEach
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
	}

	private Subscription createSubscription(String criteria, String payload, String endpoint) throws InterruptedException {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria(criteria);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(payload);
		channel.setEndpoint(endpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());

		waitForQueueToDrain();
		return subscription;
	}

	private void waitForQueueToDrain() throws InterruptedException {
		ourLog.info("QUEUE HAS {} ITEMS", mySubscriptionTestUtil.getExecutorQueueSizeForUnitTests());
		while (mySubscriptionTestUtil.getExecutorQueueSizeForUnitTests() > 0) {
			Thread.sleep(250);
		}
		ourLog.info("QUEUE HAS {} ITEMS", mySubscriptionTestUtil.getExecutorQueueSizeForUnitTests());
	}

	private Observation sendObservation(String code, String system) throws InterruptedException {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		MethodOutcome methodOutcome = myClient.create().resource(observation).execute();

		String observationId = methodOutcome.getId().getIdPart();
		observation.setId(observationId);

		waitForQueueToDrain();

		return observation;
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

		// Should see 1 subscription notification
		Thread.sleep(500);
		waitForSize(0, ourCreatedObservations);
		waitForSize(1, ourUpdatedObservations);

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();


		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see two subscription notifications
		Thread.sleep(500);
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		myClient.delete().resourceById(new IdDt(ResourceTypeEnum.SUBSCRIPTION.getCode(), subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		Thread.sleep(500);
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		myClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		Thread.sleep(500);
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		myClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		Thread.sleep(500);
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(observation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
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

		Subscription subscriptionTemp = myClient.read(Subscription.class, subscription2.getId());
		assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		myClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();


		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see two subscription notifications
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(3, ourUpdatedObservations);

		myClient.delete().resourceById(new IdDt("Subscription", subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3 = myClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		myClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(4, ourUpdatedObservations);

		Observation observation3a = myClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		myClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		waitForQueueToDrain();
		waitForSize(0, ourCreatedObservations);
		waitForSize(5, ourUpdatedObservations);

		ourLog.info("Have observations: {}", toUnqualifiedVersionlessIds(ourUpdatedObservations));

		assertFalse(subscription1.getId().equals(subscription2.getId()));
		assertFalse(observation1.getId().isEmpty());
		assertFalse(observation2.getId().isEmpty());
	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		ourListenerRestServer = new RestfulServer(FhirContext.forR4Cached());

		ObservationListener obsListener = new ObservationListener();
		ourListenerRestServer.setResourceProviders(obsListener);

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

	public static class ObservationListener implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Observation theObservation) {
			ourLog.info("Received Listener Create");
			ourCreatedObservations.add(theObservation);
			return new MethodOutcome(new IdType("Observation/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Observation theObservation) {
			ourLog.info("Received Listener Update");
			ourUpdatedObservations.add(theObservation);
			return new MethodOutcome(new IdType("Observation/1"), false);
		}

	}

}
