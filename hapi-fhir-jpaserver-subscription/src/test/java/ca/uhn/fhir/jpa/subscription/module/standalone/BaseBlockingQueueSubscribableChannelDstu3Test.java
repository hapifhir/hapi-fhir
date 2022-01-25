package ca.uhn.fhir.jpa.subscription.module.standalone;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.ISubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriberTest;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.Lists;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseBlockingQueueSubscribableChannelDstu3Test extends BaseSubscriptionDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriberTest.class);
	public static final ChannelConsumerSettings CONSUMER_OPTIONS = new ChannelConsumerSettings().setConcurrentConsumers(1);
	protected static ObservationListener ourObservationListener;

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	protected DaoRegistry myDaoRegistry;

	// Caused by: java.lang.IllegalStateException: Unable to register mock bean org.springframework.messaging.MessageHandler expected a single matching bean to replace but found [subscriptionActivatingSubscriber, subscriptionDeliveringEmailSubscriber, subscriptionDeliveringRestHookSubscriber, subscriptionMatchingSubscriber, subscriptionRegisteringSubscriber]

	@Autowired
	@Qualifier("subscriptionActivatingSubscriber")
	MessageHandler mySubscriptionActivatingSubscriber;
	@Autowired
	@Qualifier("subscriptionRegisteringSubscriber")
	MessageHandler subscriptionRegisteringSubscriber;
	@Autowired
	@Qualifier("subscriptionMatchingSubscriber")
	MessageHandler subscriptionMatchingSubscriber;

	@Autowired
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Autowired
	IInterceptorService myInterceptorRegistry;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionLoader mySubscriptionLoader;
	@Autowired
	private ISubscriptionDeliveryChannelNamer mySubscriptionDeliveryChannelNamer;
	@Autowired
	protected PartitionSettings myPartitionSettings;
	@Autowired
	protected DaoConfig myDaoConfig;

	protected String myCode = "1000000050";

	private static int ourListenerPort;
	private static RestfulServer ourListenerRestServer;
	private static Server ourListenerServer;
	protected static String ourListenerServerBase;
	protected static final List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static final List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static final List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	private static SubscribableChannel ourSubscribableChannel;
	protected final PointcutLatch mySubscriptionMatchingPost = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED);
	protected final PointcutLatch mySubscriptionActivatedPost = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED);
	protected final PointcutLatch mySubscriptionAfterDelivery = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_DELIVERY);
	protected final PointcutLatch mySubscriptionResourceMatched = new PointcutLatch(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED);
	protected final PointcutLatch mySubscriptionResourceNotMatched = new PointcutLatch(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS);

	@BeforeEach
	public void beforeReset() {
		ourCreatedObservations.clear();
		ourUpdatedObservations.clear();
		ourContentTypes.clear();
		CanonicalSubscription canonicalSubscription = new CanonicalSubscription();
		canonicalSubscription.setIdElement(new IdDt("test"));
		canonicalSubscription.setChannelType(CanonicalSubscriptionChannelType.RESTHOOK);
		mySubscriptionRegistry.unregisterAllSubscriptions();
		ourSubscribableChannel = mySubscriptionChannelFactory.newDeliveryReceivingChannel(mySubscriptionDeliveryChannelNamer.nameFromSubscription(canonicalSubscription), CONSUMER_OPTIONS);
		ourSubscribableChannel.subscribe(mySubscriptionActivatingSubscriber);
		ourSubscribableChannel.subscribe(subscriptionMatchingSubscriber);
		ourSubscribableChannel.subscribe(subscriptionRegisteringSubscriber);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED, mySubscriptionMatchingPost);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED, mySubscriptionActivatedPost);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_DELIVERY, mySubscriptionAfterDelivery);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED, mySubscriptionResourceMatched);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS, mySubscriptionResourceNotMatched);
	}

	@AfterEach
	public void cleanup() {
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptorRegistry.unregisterAllInterceptors();
		mySubscriptionMatchingPost.clear();
		mySubscriptionActivatedPost.clear();
		ourObservationListener.clear();
		super.clearRegistry();
	}

	public <T extends IBaseResource> T sendResource(T theResource) throws InterruptedException {
		return sendResource(theResource, null);
	}

	public <T extends IBaseResource> T sendResource(T theResource, RequestPartitionId theRequestPartitionId) throws InterruptedException {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, null, theRequestPartitionId);
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(msg);
		mySubscriptionMatchingPost.setExpectedCount(1);
		ourSubscribableChannel.send(message);
		mySubscriptionMatchingPost.awaitExpected();
		return theResource;
	}

	protected Subscription sendSubscription(Subscription theSubscription, RequestPartitionId theRequestPartitionId, Boolean mockDao) throws InterruptedException {
		mySubscriptionActivatedPost.setExpectedCount(1);
		Subscription retVal = sendResource(theSubscription, theRequestPartitionId);
		mySubscriptionActivatedPost.awaitExpected();
		return retVal;
	}

	protected Observation sendObservation(String code, String system) throws InterruptedException {
		return sendObservation(code, system, null);
	}

	protected Observation sendObservation(String code, String system, RequestPartitionId theRequestPartitionId) throws InterruptedException {
		Observation observation = new Observation();
		IdType id = new IdType("Observation", nextId());
		observation.setId(id);

		CodeableConcept codeableConcept = new CodeableConcept();
		observation.setCode(codeableConcept);
		Coding coding = codeableConcept.addCoding();
		coding.setCode(code);
		coding.setSystem(system);

		observation.setStatus(Observation.ObservationStatus.FINAL);

		return sendResource(observation, theRequestPartitionId);
	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		ourListenerRestServer = new RestfulServer(FhirContext.forDstu3());

		ourObservationListener = new ObservationListener();
		ourListenerRestServer.setResourceProviders(ourObservationListener);

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
		FhirContext context = ourListenerRestServer.getFhirContext();
		//Preload structure definitions so the load doesn't happen during the test (first load can be a little slow)
		context.getValidationSupport().fetchAllStructureDefinitions();
	}

	@AfterAll
	public static void stopListenerServer() throws Exception {
		JettyUtil.closeServer(ourListenerServer);
	}

	public static class ObservationListener implements IResourceProvider, IPointcutLatch {

		private final PointcutLatch updateLatch = new PointcutLatch("Observation Update");

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
		public void clear() {
			updateLatch.clear();
		}
	}
}
