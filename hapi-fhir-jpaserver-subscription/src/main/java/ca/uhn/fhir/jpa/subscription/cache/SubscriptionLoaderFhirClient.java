package ca.uhn.fhir.jpa.subscription.cache;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.concurrent.Semaphore;

public class SubscriptionLoaderFhirClient implements ISubscriptionLoader {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SubscriptionRegistry.class);
	// TODO KHS change
	private final Object myInitSubscriptionsLock = new Object();
	private Semaphore myInitSubscriptionsSemaphore = new Semaphore(1);

	@PostConstruct

	public void start() {
		initSubscriptions();
	}


	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 60000)
	@Override
	public void initSubscriptions() {
		if (!myInitSubscriptionsSemaphore.tryAcquire()) {
			return;
		}
		try {
			doInitSubscriptions();
		} finally {
			myInitSubscriptionsSemaphore.release();
		}
	}
	/**
	 * Read the existing subscriptions from the fhir server
	 */
	@Override
	@VisibleForTesting
	public int doInitSubscriptions() {
		// FIXME KHS implement
		return 0;
	}



}
