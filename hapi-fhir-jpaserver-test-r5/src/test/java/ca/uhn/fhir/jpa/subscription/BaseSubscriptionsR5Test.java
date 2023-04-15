package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.r5.BaseResourceProviderR5Test;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import com.google.common.collect.Lists;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Subscription;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("abstract")
public abstract class BaseSubscriptionsR5Test extends BaseResourceProviderR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSubscriptionsR5Test.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "http://example.com/topic/test";


	protected static int ourListenerPort;
	protected static List<String> ourContentTypes = Collections.synchronizedList(new ArrayList<>());
	protected static List<String> ourHeaders = Collections.synchronizedList(new ArrayList<>());
	protected static List<Observation> ourCreatedObservations = Collections.synchronizedList(Lists.newArrayList());
	protected static List<Observation> ourUpdatedObservations = Collections.synchronizedList(Lists.newArrayList());
	private static Server ourListenerServer;
	private static SingleQueryCountHolder ourCountHolder;
	private static String ourListenerServerBase;
	protected static RestfulServer ourListenerRestServer;
	@Autowired
	protected SubscriptionTestUtil mySubscriptionTestUtil;
	@Autowired
	protected SubscriptionMatcherInterceptor mySubscriptionMatcherInterceptor;
	protected CountingInterceptor myCountingInterceptor;
	protected List<IIdType> mySubscriptionIds = Collections.synchronizedList(new ArrayList<>());
	@Autowired
	private SingleQueryCountHolder myCountHolder;
	@Autowired
	protected SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	protected SubscriptionTopicLoader mySubscriptionTopicLoader;
	@Autowired
	private IInterceptorService myInterceptorService;
	private static final SubscriptionTopicR5Test.TestSystemProvider ourTestSystemProvider = new SubscriptionTopicR5Test.TestSystemProvider();
	protected IFhirResourceDao<SubscriptionTopic> mySubscriptionTopicDao;
	private final PointcutLatch mySubscriptionTopicsCheckedLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED);
	private final PointcutLatch mySubscriptionDeliveredLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);


	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		mySubscriptionTopicDao = myDaoRegistry.getResourceDao(SubscriptionTopic.class);
		mySubscriptionTestUtil.registerRestHookInterceptor();
		ourListenerRestServer.unregisterProvider(mySystemProvider);
		ourListenerRestServer.registerProvider(ourTestSystemProvider);

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
		myInterceptorService.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED, mySubscriptionTopicsCheckedLatch);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, mySubscriptionDeliveredLatch);

	}

	@AfterEach
	public void afterUnregisterRestHookListener() {
		myInterceptorService.unregisterAllAnonymousInterceptors();
		for (IIdType next : mySubscriptionIds) {
			IIdType nextId = next.toUnqualifiedVersionless();
			ourLog.info("Deleting: {}", nextId);
			myClient.delete().resourceById(nextId).execute();
		}
		mySubscriptionIds.clear();

		myStorageSettings.setAllowMultipleDelete(true);
		ourLog.info("Deleting all subscriptions");
		myClient.delete().resourceConditionalByUrl("Subscription?status=active").execute();
		myClient.delete().resourceConditionalByUrl("Observation?code:missing=false").execute();
		ourLog.info("Done deleting all subscriptions");
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());

		mySubscriptionTestUtil.unregisterSubscriptionInterceptor();
		ourListenerRestServer.unregisterProvider(ourTestSystemProvider);
		ourListenerRestServer.registerProvider(mySystemProvider);
		mySubscriptionTopicsCheckedLatch.clear();
		mySubscriptionDeliveredLatch.clear();
	}

	protected int getSystemProviderCount() {
		return ourTestSystemProvider.getCount();
	}

	protected Bundle getLastSystemProviderBundle() {
		return ourTestSystemProvider.lastBundle;
	}

	protected String getLastSystemProviderContentType() {
		return ourTestSystemProvider.lastContentType;
	}



	protected Subscription createSubscription(String theTopic, String thePayload) throws InterruptedException {
		// WIP STR5 will likely require matching TopicSubscription
		Subscription subscription = newTopicSubscription(theTopic, thePayload);

		return postSubscription(subscription);
	}

	@Nonnull
	protected Subscription postSubscription(Subscription subscription) throws InterruptedException {
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		MethodOutcome methodOutcome = myClient.create().resource(subscription).execute();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		subscription.setId(methodOutcome.getId().toVersionless());
		mySubscriptionIds.add(methodOutcome.getId());

		return subscription;
	}

	protected Subscription newTopicSubscription(String theTopicUrl, String thePayload) {

		Subscription subscription = new Subscription();
		subscription.setTopic(theTopicUrl);
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.REQUESTED);

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

	// WIP STR5 consolidate with lambda
	protected IIdType createResource(IBaseResource theResource, boolean theExpectDelivery) throws InterruptedException {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		IIdType id = dao.create(theResource, mySrd).getId();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return id;
	}

	protected IIdType updateResource(IBaseResource theResource, boolean theExpectDelivery) throws InterruptedException {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		IIdType id = dao.update(theResource, mySrd).getId();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return id;
	}

	protected Bundle sendTransaction(Bundle theBundle, boolean theExpectDelivery) throws InterruptedException {
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		Bundle retval = mySystemDao.transaction(mySrd, theBundle);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return retval;
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


	protected void waitForRegisteredSubscriptionTopicCount(int theTarget) throws Exception {
		await().until(() -> subscriptionTopicRegistryHasSize(theTarget));
	}

	private boolean subscriptionTopicRegistryHasSize(int theTarget) {
		int size = mySubscriptionTopicRegistry.size();
		if (size == theTarget) {
			return true;
		}
		mySubscriptionTopicLoader.doSyncResourcessForUnitTest();
		return mySubscriptionTopicRegistry.size() == theTarget;
	}

	protected void createSubscriptionTopic(SubscriptionTopic theSubscriptionTopic) throws InterruptedException {
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		mySubscriptionTopicDao.create(theSubscriptionTopic, mySrd);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
	}

	protected static void validateSubscriptionStatus(Subscription subscription, IBaseResource sentResource, SubscriptionStatus ss) {
		assertEquals(Enumerations.SubscriptionStatusCodes.ACTIVE, ss.getStatus());
		assertEquals(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION, ss.getType());
		assertEquals("1", ss.getEventsSinceSubscriptionStartElement().getValueAsString());

		List<SubscriptionStatus.SubscriptionStatusNotificationEventComponent> notificationEvents = ss.getNotificationEvent();
		assertEquals(1, notificationEvents.size());
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent = notificationEvents.get(0);
		assertEquals(1, notificationEvent.getEventNumber());
		assertEquals(sentResource.getIdElement().toUnqualifiedVersionless(), notificationEvent.getFocus().getReferenceElement());

		assertEquals(subscription.getIdElement().toUnqualifiedVersionless(), ss.getSubscription().getReferenceElement());
		assertEquals(SUBSCRIPTION_TOPIC_TEST_URL, ss.getTopic());
	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		ourListenerRestServer = new RestfulServer(FhirContext.forR5Cached());

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


	static class TestSystemProvider {
		AtomicInteger count = new AtomicInteger(0);
		Bundle lastBundle;
		String lastContentType;

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theBundle, HttpServletRequest theRequest) {
			ourLog.info("Received Transaction with {} entries", theBundle.getEntry().size());
			count.incrementAndGet();
			lastContentType = theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", "");
			lastBundle = theBundle;
			return theBundle;
		}

		int getCount() {
			return count.get();
		}
	}
}
