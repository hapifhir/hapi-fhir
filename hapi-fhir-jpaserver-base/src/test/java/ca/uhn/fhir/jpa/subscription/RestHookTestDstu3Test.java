
package ca.uhn.fhir.jpa.subscription;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.*;

import com.google.common.collect.Lists;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.provider.dstu3.BaseResourceProviderDstu3Test;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 * Test the rest-hook subscriptions
 */
public class RestHookTestDstu3Test extends BaseResourceProviderDstu3Test {

	private static List<String> ourContentTypes = new ArrayList<String>();
	private static List<Observation> ourCreatedObservations = Lists.newArrayList();
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	private static String ourListenerServerBase;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RestHookTestDstu3Test.class);

	private static List<Observation> ourUpdatedObservations = Lists.newArrayList();

	@After
	public void afterUnregisterRestHookListener() {
		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		ourClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		
		ourRestServer.unregisterInterceptor(ourRestHookSubscriptionInterceptor);
	}

	@Before
	public void beforeRegisterRestHookListener() {
		ourRestServer.registerInterceptor(ourRestHookSubscriptionInterceptor);
	}

	@Before
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
	}

	private Subscription createSubscription(String theCriteria, String thePayload, String theEndpoint) {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		subscription.setCriteria(theCriteria);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(thePayload);
		channel.setEndpoint(theEndpoint);
		subscription.setChannel(channel);

		MethodOutcome methodOutcome = ourClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());

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

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		Thread.sleep(500);
		assertEquals(1, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
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
		Thread.sleep(500);
		assertEquals(1, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
		
		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();


		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see two subscription notifications
		Thread.sleep(500);
		assertEquals(3, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
		
		ourClient.delete().resourceById(new IdDt("Subscription", subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(1, ourUpdatedObservations.size());

		Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
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
		Thread.sleep(500);
		assertEquals(1, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
	}

	
	@Test
	public void testRestHookSubscriptionApplicationXml() throws Exception {
		String payload = "application/xml";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		Subscription subscription1 = createSubscription(criteria1, payload, ourListenerServerBase);
		Subscription subscription2 = createSubscription(criteria2, payload, ourListenerServerBase);

		Observation observation1 = sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		Thread.sleep(500);
		assertEquals(1, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
		assertEquals(Constants.CT_FHIR_XML_NEW, ourContentTypes.get(0));
		
		Subscription subscriptionTemp = ourClient.read(Subscription.class, subscription2.getId());
		Assert.assertNotNull(subscriptionTemp);

		subscriptionTemp.setCriteria(criteria1);
		ourClient.update().resource(subscriptionTemp).withId(subscriptionTemp.getIdElement()).execute();


		Observation observation2 = sendObservation(code, "SNOMED-CT");

		// Should see two subscription notifications
		Thread.sleep(500);
		assertEquals(3, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());
		
		ourClient.delete().resourceById(new IdDt("Subscription", subscription2.getId())).execute();

		Observation observationTemp3 = sendObservation(code, "SNOMED-CT");

		// Should see only one subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());

		Observation observation3 = ourClient.read(Observation.class, observationTemp3.getId());
		CodeableConcept codeableConcept = new CodeableConcept();
		observation3.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code + "111");
		coding.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3).withId(observation3.getIdElement()).execute();

		// Should see no subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(0, ourUpdatedObservations.size());

		Observation observation3a = ourClient.read(Observation.class, observationTemp3.getId());

		CodeableConcept codeableConcept1 = new CodeableConcept();
		observation3a.setCode(codeableConcept1);
		Coding coding1 = codeableConcept1.addCoding();
		coding1.setCode(code);
		coding1.setSystem("SNOMED-CT");
		ourClient.update().resource(observation3a).withId(observation3a.getIdElement()).execute();

		// Should see only one subscription notification
		Thread.sleep(500);
		assertEquals(4, ourCreatedObservations.size());
		assertEquals(1, ourUpdatedObservations.size());

		Assert.assertFalse(subscription1.getId().equals(subscription2.getId()));
		Assert.assertFalse(observation1.getId().isEmpty());
		Assert.assertFalse(observation2.getId().isEmpty());
	}
	
	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = RandomServerPortProvider.findFreePort();
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
			ourLog.info("Received Listener Update");
			ourUpdatedObservations.add(theObservation);
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			return new MethodOutcome(new IdType("Observation/1"), false);
		}

	}

}
