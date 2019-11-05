package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SubscriptionLoaderTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
	private static final int MOCK_FHIR_CLIENT_FAILURES = 3;
	@Autowired
	private MockFhirClientSubscriptionProvider myMockFhirClientSubscriptionProvider;

	@Before
	public void setFailCount() {
		myMockFhirClientSubscriptionProvider.setFailCount(MOCK_FHIR_CLIENT_FAILURES);
	}

	@After
	public void restoreFailCount() {
		myMockFhirClientSubscriptionProvider.setFailCount(0);
	}

	@Test
	public void testSubscriptionLoaderFhirClientDown() throws Exception {
		String payload = "application/fhir+json";

		String criteria1 = "Observation?code=SNOMED-CT|" + myCode + "&_format=xml";
		String criteria2 = "Observation?code=SNOMED-CT|" + myCode + "111&_format=xml";

		List<Subscription> subs = new ArrayList<>();
		subs.add(makeActiveSubscription(criteria1, payload, ourListenerServerBase));
		subs.add(makeActiveSubscription(criteria2, payload, ourListenerServerBase));

		mySubscriptionActivatedPost.setExpectedCount(2);
		initSubscriptionLoader(subs, "uuid");
		mySubscriptionActivatedPost.awaitExpected();
		assertEquals(0, myMockFhirClientSubscriptionProvider.getFailCount());
	}


	@Test
	public void testMultipleThreadsDontBlock() throws InterruptedException {
		SubscriptionLoader svc = new SubscriptionLoader();
		CountDownLatch latch = new CountDownLatch(1);
		new Thread(()->{
			try {
				svc.acquireSemaphoreForUnitTest();
				latch.countDown();
			} catch (InterruptedException theE) {
				// ignore
			}
		}).start();

		latch.await(10, TimeUnit.SECONDS);
		svc.syncSubscriptions();
	}

}
