package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SubscriptionLoaderTest extends BaseBlockingQueueSubscribableChannelDstu3Test {

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
