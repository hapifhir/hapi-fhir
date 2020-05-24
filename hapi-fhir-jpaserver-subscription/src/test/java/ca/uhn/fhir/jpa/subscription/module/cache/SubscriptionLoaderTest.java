package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
