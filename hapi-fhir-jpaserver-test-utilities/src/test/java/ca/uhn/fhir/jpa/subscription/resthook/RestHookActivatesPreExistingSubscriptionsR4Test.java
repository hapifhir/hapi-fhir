package ca.uhn.fhir.jpa.subscription.resthook;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.r4.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestHookActivatesPreExistingSubscriptionsR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(RestHookActivatesPreExistingSubscriptionsR4Test.class);
	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static String ourListenerServerBase;
	private static Server ourListenerServer;
	private static final List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static final List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	private static final List<String> ourHeaders = Collections.synchronizedList(new ArrayList<>());

	@Autowired
	private SubscriptionTestUtil mySubscriptionTestUtil;
	@Autowired
	private SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;

	@AfterEach
	public void afterUnregisterRestHookListener() {
		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
		myDaoConfig.clearSupportedSubscriptionTypesForUnitTest();
	}

	@BeforeEach
	public void beforeSetSubscriptionActivatingInterceptor() {
		myDaoConfig.addSupportedSubscriptionType(org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.RESTHOOK);
		mySubscriptionMatcherInterceptor.startIfNeeded();
		mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
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

		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
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

		MethodOutcome methodOutcome = myClient.create().resource(observation).execute();

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

		mySubscriptionTestUtil.registerRestHookInterceptor();
		mySubscriptionLoader.doSyncSubscriptionsForUnitTest();

		sendObservation(code, "SNOMED-CT");

		// Should see 1 subscription notification
		waitForQueueToDrain();
		waitForSize(1, ourUpdatedObservations);
		assertEquals(Constants.CT_FHIR_JSON_NEW, ourContentTypes.get(0));
	}

	private void waitForQueueToDrain() throws InterruptedException {
			mySubscriptionTestUtil.waitForQueueToDrain();
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

}
