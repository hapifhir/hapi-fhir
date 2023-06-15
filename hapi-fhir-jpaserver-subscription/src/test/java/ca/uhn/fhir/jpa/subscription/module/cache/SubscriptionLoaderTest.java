package ca.uhn.fhir.jpa.subscription.module.cache;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.standalone.BaseBlockingQueueSubscribableChannelDstu3Test;

public class SubscriptionLoaderTest extends BaseBlockingQueueSubscribableChannelDstu3Test {
    @Test
    public void testMultipleThreadsDontBlock() throws InterruptedException {
        SubscriptionLoader svc = new SubscriptionLoader();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(
                        () -> {
                            try {
                                svc.acquireSemaphoreForUnitTest();
                                latch.countDown();
                            } catch (InterruptedException theE) {
                                // ignore
                            }
                        })
                .start();

        latch.await(10, TimeUnit.SECONDS);
        svc.syncDatabaseToCache();
    }
}
