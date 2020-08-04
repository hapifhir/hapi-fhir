package ca.uhn.fhir.jpa.empi.helper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.empi.broker.EmpiQueueConsumerLoader;
import ca.uhn.fhir.jpa.empi.config.EmpiSubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.when;

/**
 * How to use this Rule:
 * <p>
 * This rule is to be used whenever you want to have the EmpiInterceptor loaded, and be able
 * to execute creates/updates/deletes while being assured that all EMPI work has been done before exiting.
 * Provides two types of method:
 * <p>
 * 1. doUpdate/doCreate. These methods do not wait for Asynchronous EMPI work to be done. Use these when you are expecting
 * the calls to fail, as those hooks will never be called.
 * <p>
 * 2. createWithLatch/updateWithLatch. These methods will await the EMPI hooks, which are only triggered post-EMPI processing
 * You should use these when you are expecting successful processing of the resource, and need to wait for async EMPI linking
 * work to be done.
 * <p>
 * Note: all create/update functions take an optional isExternalHttpRequest boolean, to make it appear as though the request's
 * origin is an HTTP request.
 */
public abstract class BaseEmpiHelper implements BeforeEachCallback, AfterEachCallback {
	@Mock
	protected ServletRequestDetails myMockSrd;
	@Mock
	protected HttpServletRequest myMockServletRequest;
	@Mock
	protected RestfulServer myMockRestfulServer;
	@Mock
	protected FhirContext myMockFhirContext;
	protected PointcutLatch myAfterEmpiLatch = new PointcutLatch(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED);
	@Autowired
	EmpiQueueConsumerLoader myEmpiQueueConsumerLoader;
	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionLoader mySubscriptionLoader;
	@Autowired
	EmpiSubscriptionLoader myEmpiSubscriptionLoader;
	@Mock
	private IInterceptorBroadcaster myMockInterceptorBroadcaster;
	@Autowired
	private IInterceptorService myInterceptorService;

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		myInterceptorService.unregisterInterceptor(myAfterEmpiLatch);
		myAfterEmpiLatch.clear();
		waitUntilEmpiQueueIsEmpty();
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		//This sets up mock servlet request details, which allows our DAO requests to appear as though
		//they are coming from an external HTTP Request.
		MockitoAnnotations.initMocks(this);
		when(myMockSrd.getInterceptorBroadcaster()).thenReturn(myMockInterceptorBroadcaster);
		when(myMockSrd.getServletRequest()).thenReturn(myMockServletRequest);
		when(myMockSrd.getServer()).thenReturn(myMockRestfulServer);
		when(myMockSrd.getRequestId()).thenReturn("MOCK_REQUEST");
		when(myMockRestfulServer.getFhirContext()).thenReturn(myMockFhirContext);

		//This sets up our basic interceptor, and also attached the latch so we can await the hook calls.
		myInterceptorService.registerAnonymousInterceptor(Pointcut.EMPI_AFTER_PERSISTED_RESOURCE_CHECKED, myAfterEmpiLatch);

		// We need to call this because subscriptions will get deleted in @After cleanup
		waitForActivatedSubscriptionCount(0);
		myEmpiSubscriptionLoader.daoUpdateEmpiSubscriptions();
		mySubscriptionLoader.syncSubscriptions();
		waitForActivatedSubscriptionCount(2);
	}


	protected void waitForActivatedSubscriptionCount(int theSize) {
		await("Active Subscription Count has reached " + theSize).until(() -> mySubscriptionRegistry.size() >= theSize);
	}

	private void waitUntilEmpiQueueIsEmpty() {
		await().until(() -> getExecutorQueueSize() == 0);
	}

	public int getExecutorQueueSize() {
		LinkedBlockingChannel channel = (LinkedBlockingChannel) myEmpiQueueConsumerLoader.getEmpiChannelForUnitTest();
		return channel.getQueueSizeForUnitTest();
	}


}
