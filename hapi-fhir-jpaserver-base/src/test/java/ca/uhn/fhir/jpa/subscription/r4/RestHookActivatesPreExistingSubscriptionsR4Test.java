package ca.uhn.fhir.jpa.subscription.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.subscription.RestHookTestDstu2Test;
import ca.uhn.fhir.jpa.subscription.SubscriptionActivatingSubscriber;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.PortUtil;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.Assert.*;

public class RestHookActivatesPreExistingSubscriptionsR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(RestHookActivatesPreExistingSubscriptionsR4Test.class);
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static String ourListenerServerBase;
	private static Server ourListenerServer;
	private static List<Observation> ourUpdatedObservations = Lists.newArrayList();
	private static List<String> ourContentTypes = new ArrayList<>();
	private static List<String> ourHeaders = new ArrayList<>();

	@After
	public void afterResetSubscriptionActivatingInterceptor() {
		SubscriptionActivatingSubscriber.setWaitForSubscriptionActivationSynchronouslyForUnitTest(false);
	}

	@After
	public void afterUnregisterRestHookListener() {
		ourRestServer.unregisterInterceptor(getRestHookSubscriptionInterceptor());
	}

	@Before
	public void beforeSetSubscriptionActivatingInterceptor() {
		SubscriptionActivatingSubscriber.setWaitForSubscriptionActivationSynchronouslyForUnitTest(true);
		getRestHookSubscriptionInterceptor().initSubscriptions();
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
	public void testSubscriptionInterceptorRegisteredAfterSubscriptionCreated() throws Exception {
		String payload = "application/fhir+json";

		String code = "1000000050";
		String criteria1 = "Observation?code=SNOMED-CT|" + code + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + code + "111&_format=xml";

		createSubscription(criteria1, payload, ourListenerServerBase);
		createSubscription(criteria2, payload, ourListenerServerBase);

		ourRestServer.registerInterceptor(getRestHookSubscriptionInterceptor());
		getRestHookSubscriptionInterceptor().initSubscriptions();

		assertTrue(hasRestHookSubscriptionInterceptor());


		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	private void waitForQueueToDrain() throws InterruptedException {
		if (hasRestHookSubscriptionInterceptor()) {
			RestHookTestDstu2Test.waitForQueueToDrain(getRestHookSubscriptionInterceptor());
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

	public static class ObservationListener implements IResourceProvider {


		private void extractHeaders(HttpServletRequest theRequest) {
			java.util.Enumeration<String> headerNamesEnum = theRequest.getHeaderNames();
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

}
