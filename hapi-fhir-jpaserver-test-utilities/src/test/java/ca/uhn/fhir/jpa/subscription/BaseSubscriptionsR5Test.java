package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.r5.BaseResourceProviderR5Test;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import com.google.common.collect.Lists;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Disabled
public abstract class BaseSubscriptionsR5Test extends BaseResourceProviderR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSubscriptionsR5Test.class);
	protected static int ourListenerPort;
	protected static List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	protected static List<String> ourHeaders = Collections.synchronizedList(new ArrayList<>());
	protected static List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static Server ourListenerServer;
	private static SingleQueryCountHolder ourCountHolder;
	private static String ourListenerServerBase;
	@Autowired
	protected SubscriptionTestUtil mySubscriptionTestUtil;
	@Autowired
	protected SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	protected CountingInterceptor myCountingInterceptor;
	protected List<IIdType> mySubscriptionIds = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	private SingleQueryCountHolder myCountHolder;

	@AfterEach
	public void afterUnregisterRestHookListener() {
		for (IIdType next : mySubscriptionIds) {
			IIdType nextId = next.toUnqualifiedVersionless();
			ourLog.info("Deleting: {}", nextId);
			myClient.delete().resourceById(nextId).execute();
		}
		mySubscriptionIds.clear();

		myDaoConfig.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		myClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		myClient.delete().resourceConditionalByUrl("Observation?code:missing=false").execute();
		ourLog.info("Done deleting all subscriptions");
		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
	}

	@BeforeEach
	public void beforeRegisterRestHookListener() {
		mySubscriptionTestUtil.registerRestHookInterceptor();
	}

	@BeforeEach
	public void beforeReset() throws Exception {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		ourHeaders.clear();

		// Delete all Subscriptions
		if (myClient != null) {
			Bundle allSubscriptions = myClient.search().forResource(Subscription.class).returnBundle(Bundle.class).execute();
			for (IBaseResource next : BundleUtil.toListOfResources(myFhirCtx, allSubscriptions)) {
				myClient.delete().resource(next).execute();
			}
			waitForActivatedSubscriptionCount(0);
		}

		LinkedBlockingChannel processingChannel = mySubscriptionMatcherInterceptor.getProcessingChannelForUnitTest();
		if (processingChannel != null) {
			processingChannel.clearInterceptorsForUnitTest();
		}
		myCountingInterceptor = new CountingInterceptor();
		if (processingChannel != null) {
			processingChannel.addInterceptor(myCountingInterceptor);
		}
	}


	protected Subscription createSubscription(String theCriteria, String thePayload) {
		Subscription subscription = newSubscription(theCriteria, thePayload);

		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		subscription.setId(methodOutcome.getId().getIdPart());
		mySubscriptionIds.add(methodOutcome.getId());

		return subscription;
	}

	protected Subscription newSubscription(String theCriteria, String thePayload) {
		SubscriptionTopic topic = new SubscriptionTopic();
		topic.getResourceTriggerFirstRep().getQueryCriteria().setCurrent(theCriteria);
		topic.setId("1");

		Subscription subscription = new Subscription();
		subscription.getContained().add(topic);
		subscription.setTopic("#1");
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Enumerations.SubscriptionState.REQUESTED);

		subscription.getChannelType()
			.setSystem(CanonicalSubscriptionChannelType.RESTHOOK.getSystem())
			.setCode(CanonicalSubscriptionChannelType.RESTHOOK.toCode());
		subscription.setContentType(thePayload);
		subscription.setEndpoint(ourListenerServerBase);
		return subscription;
	}


	protected void waitForQueueToDrain() throws InterruptedException {
		mySubscriptionTestUtil.waitForQueueToDrain();
	}

	@PostConstruct
	public void initializeOurCountHolder() {
		ourCountHolder = myCountHolder;
	}


	protected Observation sendObservation(String code, String system) {
		Observation observation = new Observation();
		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		observation.getIdentifierFirstRep().setSystem("foo").setValue("1");
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(Enumerations.ObservationStatus.FINAL);

		IIdType id = myObservationDao.create(observation).getId();
		observation.setId(id);

		return observation;
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

	@AfterAll
	public static void reportTotalSelects() {
		ourLog.info("Total database select queries: {}", getQueryCount().getSelect());
	}

	private static QueryCount getQueryCount() {
		return ourCountHolder.getQueryCountMap().get("");
	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		RestfulServer ourListenerRestServer = new RestfulServer(FhirContext.forR5Cached());

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
