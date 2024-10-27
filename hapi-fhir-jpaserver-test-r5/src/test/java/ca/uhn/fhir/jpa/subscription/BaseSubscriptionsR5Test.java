package ca.uhn.fhir.jpa.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.provider.r5.BaseResourceProviderR5Test;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.test.util.SubscriptionTestUtil;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicLoader;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicRegistry;
import ca.uhn.fhir.rest.annotation.Transaction;
import ca.uhn.fhir.rest.annotation.TransactionParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import net.ttddyy.dsproxy.QueryCount;
import net.ttddyy.dsproxy.listener.SingleQueryCountHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
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

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Disabled("abstract")
public abstract class BaseSubscriptionsR5Test extends BaseResourceProviderR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseSubscriptionsR5Test.class);
	public static final String SUBSCRIPTION_TOPIC_TEST_URL = "http://example.com/topic/test";


	protected static int ourListenerPort;
	private static Server ourListenerServer;
	private static SingleQueryCountHolder ourCountHolder;
	private static String ourListenerServerBase;
	protected static RestfulServer ourListenerRestServer;
	@Autowired
	protected SubscriptionTestUtil mySubscriptionTestUtil;
	@Autowired
	protected ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;
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
	protected final PointcutLatch mySubscriptionTopicsCheckedLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED);
	protected final PointcutLatch mySubscriptionDeliveredLatch = new PointcutLatch(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY);

	@Override
	@BeforeEach
	protected void before() throws Exception {
		super.before();
		mySubscriptionTopicDao = myDaoRegistry.getResourceDao(SubscriptionTopic.class);
		mySubscriptionTestUtil.registerRestHookInterceptor();
		mySubscriptionTestUtil.registerSubscriptionLoggingInterceptor();
		ourListenerRestServer.unregisterProvider(mySystemProvider);
		ourListenerRestServer.registerProvider(ourTestSystemProvider);

		ourTestSystemProvider.clear();

		// Delete all Subscriptions
		if (myClient != null) {
			Bundle allSubscriptions = myClient.search().forResource(Subscription.class).returnBundle(Bundle.class).execute();
			for (IBaseResource next : BundleUtil.toListOfResources(myFhirCtx, allSubscriptions)) {
				myClient.delete().resource(next).execute();
			}
			waitForActivatedSubscriptionCount(0);
		}

		LinkedBlockingChannel processingChannel = (LinkedBlockingChannel) myResourceModifiedSubmitterSvc.getProcessingChannelForUnitTest();
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
		mySubscriptionTestUtil.unregisterSubscriptionLoggingInterceptor();

		ourListenerRestServer.unregisterProvider(ourTestSystemProvider);
		ourListenerRestServer.registerProvider(mySystemProvider);
		mySubscriptionTopicsCheckedLatch.clear();
		mySubscriptionDeliveredLatch.clear();
	}

	protected int getSystemProviderCount() {
		return ourTestSystemProvider.getCount();
	}

	protected List<String> getLastSystemProviderHeaders() {
		return ourTestSystemProvider.getLastHeaders();
	}

	protected Bundle getLastSystemProviderBundle() {
		return ourTestSystemProvider.getLastBundle();
	}

	protected String getLastSystemProviderContentType() {
		return ourTestSystemProvider.getLastContentType();
	}

	protected Set<Observation> getReceivedObservations() {
		return ourTestSystemProvider.receivedBundles.stream()
			.flatMap(t -> t.getEntry().stream())
			.filter(t -> t.getResource() instanceof Observation)
			.map(t -> (Observation) t.getResource())
			.collect(Collectors.toSet());
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

	protected Subscription newTopicSubscription(String theTopicUrl, String thePayload, String... theFilters) {

		Subscription subscription = new Subscription();
		subscription.setTopic(theTopicUrl);
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);

		for (String nextFilter : theFilters) {
			filterComponentFromQueryString(nextFilter).forEach(subscription::addFilterBy);
		}

		subscription.getChannelType()
			.setSystem(CanonicalSubscriptionChannelType.RESTHOOK.getSystem())
			.setCode(CanonicalSubscriptionChannelType.RESTHOOK.toCode());
		subscription.setContentType(thePayload);
		subscription.setEndpoint(ourListenerServerBase);
		return subscription;
	}

	private Stream<Subscription.SubscriptionFilterByComponent> filterComponentFromQueryString(String theNextFilter) {
		return CanonicalTopicSubscriptionFilter.fromQueryUrl(theNextFilter).stream().map(CanonicalTopicSubscriptionFilter::toSubscriptionFilterByComponent);
	}

	@PostConstruct
	public void initializeOurCountHolder() {
		ourCountHolder = myCountHolder;
	}

	protected IIdType createResource(IBaseResource theResource, boolean theExpectDelivery) throws InterruptedException {
		return createResource(theResource, theExpectDelivery, 1);
	}

	// TODO KHS consolidate with lambda
	protected IIdType createResource(IBaseResource theResource, boolean theExpectDelivery, int theCount) throws InterruptedException {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(theCount);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		IIdType id = dao.create(theResource, mySrd).getId();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return id;
	}

	protected DaoMethodOutcome updateResource(IBaseResource theResource, boolean theExpectDelivery) throws InterruptedException {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResource.getClass());
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		DaoMethodOutcome retval = dao.update(theResource, mySrd);

		List<HookParams> hookParams = mySubscriptionTopicsCheckedLatch.awaitExpected();
		ResourceModifiedMessage lastMessage = PointcutLatch.getInvocationParameterOfType(hookParams, ResourceModifiedMessage.class);
		assertEquals(theResource.getIdElement().toVersionless().toString(), lastMessage.getPayloadId());

		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return retval;
	}

	protected Bundle sendTransaction(Bundle theBundle, boolean theExpectDelivery) throws InterruptedException {
		int expectedChecks = theBundle.getEntry().size();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.setExpectedCount(1);
		}
		mySubscriptionTopicsCheckedLatch.setExpectedCount(expectedChecks);
		Bundle retval = mySystemDao.transaction(mySrd, theBundle);
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		if (theExpectDelivery) {
			mySubscriptionDeliveredLatch.awaitExpected();
		}
		return retval;
	}

	@AfterAll
	public static void reportTotalSelects() {
		ourLog.info("Total database select queries: {}", getQueryCount().getSelect());
	}

	private static QueryCount getQueryCount() {
		return ourCountHolder.getQueryCountMap().get("");
	}


	protected void waitForRegisteredSubscriptionTopicCount(int theTarget) {
		await().until(() -> subscriptionTopicRegistryHasSize(theTarget));
	}

	private boolean subscriptionTopicRegistryHasSize(int theTarget) {
		int size = mySubscriptionTopicRegistry.size();
		if (size == theTarget) {
			return true;
		}
		mySubscriptionTopicLoader.doSyncResourcesForUnitTest();
		return mySubscriptionTopicRegistry.size() == theTarget;
	}

	protected SubscriptionTopic createSubscriptionTopic(SubscriptionTopic theSubscriptionTopic) throws InterruptedException {
		mySubscriptionTopicsCheckedLatch.setExpectedCount(1);
		SubscriptionTopic retval = (SubscriptionTopic) myClient.create().resource(theSubscriptionTopic).execute().getResource();
		mySubscriptionTopicsCheckedLatch.awaitExpected();
		return retval;
	}

	protected static void validateSubscriptionStatus(Subscription subscription, IBaseResource sentResource, SubscriptionStatus ss, Long theExpectedEventNumber) {
		assertEquals(Enumerations.SubscriptionStatusCodes.ACTIVE, ss.getStatus());
		assertEquals(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION, ss.getType());
		assertEquals(theExpectedEventNumber.toString(), ss.getEventsSinceSubscriptionStartElement().getValueAsString());

		List<SubscriptionStatus.SubscriptionStatusNotificationEventComponent> notificationEvents = ss.getNotificationEvent();
		assertThat(notificationEvents).hasSize(1);
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent notificationEvent = notificationEvents.get(0);
		assertEquals(theExpectedEventNumber, notificationEvent.getEventNumber());
		assertEquals(sentResource.getIdElement().toUnqualifiedVersionless(), notificationEvent.getFocus().getReferenceElement());

		assertEquals(subscription.getIdElement().toUnqualifiedVersionless(), ss.getSubscription().getReferenceElement());
		assertEquals(subscription.getTopic(), ss.getTopic());
	}

	@BeforeAll
	public static void startListenerServer() throws Exception {
		ourListenerRestServer = new RestfulServer(FhirContext.forR5Cached());
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
		final List<Bundle> receivedBundles = new ArrayList<>();
		final List<String> receivedContentTypes = new ArrayList<>();
		final List<String> myHeaders = new ArrayList<>();

		@Transaction
		public Bundle transaction(@TransactionParam Bundle theBundle, HttpServletRequest theRequest) {
			ourLog.info("Received Transaction with {} entries", theBundle.getEntry().size());
			count.incrementAndGet();
			receivedContentTypes.add(theRequest.getHeader(Constants.HEADER_CONTENT_TYPE).replaceAll(";.*", ""));
			receivedBundles.add(theBundle);
			extractHeaders(theRequest);
			return theBundle;
		}

		private void extractHeaders(HttpServletRequest theRequest) {
			Enumeration<String> headerNamesEnum = theRequest.getHeaderNames();
			while (headerNamesEnum.hasMoreElements()) {
				String nextName = headerNamesEnum.nextElement();
				Enumeration<String> valueEnum = theRequest.getHeaders(nextName);
				while (valueEnum.hasMoreElements()) {
					String nextValue = valueEnum.nextElement();
					myHeaders.add(nextName + ": " + nextValue);
				}
			}
		}

		int getCount() {
			return count.get();
		}

		public String getLastContentType() {
			return receivedContentTypes.get(receivedContentTypes.size() - 1);
		}

		public Bundle getLastBundle() {
			return receivedBundles.get(receivedBundles.size() - 1);
		}

		public List<String> getLastHeaders() {
			return Collections.unmodifiableList(myHeaders);
		}

		public void clear() {
			count.set(0);
			receivedBundles.clear();
			receivedContentTypes.clear();
			myHeaders.clear();
		}
	}
}
