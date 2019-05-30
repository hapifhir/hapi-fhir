package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.concurrency.IPointcutLatch;
import ca.uhn.fhir.jpa.model.concurrency.PointcutLatch;
import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriberTest;
import ca.uhn.fhir.rest.annotation.Create;
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
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseBlockingQueueSubscribableChannelDstu3Test extends BaseSubscriptionDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriberTest.class);
	protected static ObservationListener ourObservationListener;

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	StandaloneSubscriptionMessageHandler myStandaloneSubscriptionMessageHandler;
	@Autowired
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Autowired
	IInterceptorService myInterceptorRegistry;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;


	protected String myCode = "1000000050";

	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	protected static String ourListenerServerBase;
	protected static List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	private static SubscribableChannel ourSubscribableChannel;
	protected PointcutLatch mySubscriptionMatchingPost = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED);
	protected PointcutLatch mySubscriptionActivatedPost = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);

	@Before
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		mySubscriptionRegistry.unregisterAllSubscriptions();
		if (ourSubscribableChannel == null) {
			ourSubscribableChannel = mySubscriptionChannelFactory.newDeliveryChannel("test", Subscription.SubscriptionChannelType.RESTHOOK.toCode().toLowerCase());
			ourSubscribableChannel.subscribe(myStandaloneSubscriptionMessageHandler);
		}
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED, mySubscriptionMatchingPost);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, mySubscriptionActivatedPost);
	}

	@After
	public void cleanup() {
		myInterceptorRegistry.unregisterAllInterceptors();
		mySubscriptionMatchingPost.clear();
		mySubscriptionActivatedPost.clear();
		ourObservationListener.clear();
	}

	public <T extends IBaseResource> T sendResource(T theResource) throws InterruptedException {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(msg);
		mySubscriptionMatchingPost.setExpectedCount(1);
		ourSubscribableChannel.send(message);
		mySubscriptionMatchingPost.awaitExpected();
		return theResource;
	}

	protected Subscription sendSubscription(String theCriteria, String thePayload, String theEndpoint) throws InterruptedException {
		Subscription subscription = makeActiveSubscription(theCriteria, thePayload, theEndpoint);
		mySubscriptionActivatedPost.setExpectedCount(1);
		Subscription retval = sendResource(subscription);
		mySubscriptionActivatedPost.awaitExpected();
		return retval;
	}

	protected Observation sendObservation(String code, String system) throws InterruptedException {
		Observation observation = new Observation();
		IdType id = new IdType("Observation", nextId());
		observation.setId(id);

		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		return sendResource(observation);
	}

	@BeforeClass
	public static void startListenerServer() throws Exception {
		ourListenerPort = PortUtil.findFreePort();
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu3());
		ourListenerServerBase = "http://localhost:" + ourListenerPort + "/fhir/context";

		ourObservationListener = new ObservationListener();
		ourListenerRestServer.setResourceProviders(ourObservationListener);

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

	public static class ObservationListener implements IResourceProvider, IPointcutLatch {

		private PointcutLatch updateLatch = new PointcutLatch("Observation Update");

		@Create
		public MethodOutcome create(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourLog.info("Received Listener Create");
			ourContentTypes.add(theRequest.getHeader(ca.uhn.fhir.rest.api.Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourCreatedObservations.add(theObservation);
			return new MethodOutcome(new IdType("Observation/1"), true);
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Observation.class;
		}

		@Update
		public MethodOutcome update(@ResourceParam Observation theObservation, HttpServletRequest theRequest) {
			ourContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			ourUpdatedObservations.add(theObservation);
			updateLatch.invoke(null, new HookParams().add(Observation.class, theObservation));
			ourLog.info("Received Listener Update (now have {} updates)", ourUpdatedObservations.size());
			return new MethodOutcome(new IdType("Observation/1"), false);
		}

		@Override
		public void setExpectedCount(int count) {
			updateLatch.setExpectedCount(count);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return updateLatch.awaitExpected();
		}

		@Override
		public void clear() { updateLatch.clear();}
	}
}
