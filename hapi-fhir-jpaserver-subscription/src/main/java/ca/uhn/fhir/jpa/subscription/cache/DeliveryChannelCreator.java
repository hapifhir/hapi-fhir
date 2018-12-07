package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class DeliveryChannelCreator {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DeliveryChannelCreator.class);

	@Autowired
	FhirContext myFhirContext;

	protected SubscribableChannel createDeliveryChannel(CanonicalSubscription theSubscription) {
		String subscriptionId = theSubscription.getIdElement(myFhirContext).getIdPart();

		LinkedBlockingQueue<Runnable> executorQueue = new LinkedBlockingQueue<>(1000);
		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder()
			.namingPattern("subscription-delivery-" + subscriptionId + "-%d")
			.daemon(false)
			.priority(Thread.NORM_PRIORITY)
			.build();
		RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable theRunnable, ThreadPoolExecutor theExecutor) {
				ourLog.info("Note: Executor queue is full ({} elements), waiting for a slot to become available!", executorQueue.size());
				StopWatch sw = new StopWatch();
				try {
					executorQueue.put(theRunnable);
				} catch (InterruptedException theE) {
					throw new RejectedExecutionException("Task " + theRunnable.toString() +
						" rejected from " + theE.toString());
				}
				ourLog.info("Slot become available after {}ms", sw.getMillis());
			}
		};
		ThreadPoolExecutor deliveryExecutor = new ThreadPoolExecutor(
			1,
			SubscriptionConstants.EXECUTOR_THREAD_COUNT,
			0L,
			TimeUnit.MILLISECONDS,
			executorQueue,
			threadFactory,
			rejectedExecutionHandler);

		return new ExecutorSubscribableChannel(deliveryExecutor);
	}

}
