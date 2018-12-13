package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;


@Service
@Lazy
public class SubscriptionLoader {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionLoader.class);

	@Autowired
	private ISubscriptionProvider mySubscriptionProvidor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	private final Object myInitSubscriptionsLock = new Object();
	private Semaphore myInitSubscriptionsSemaphore = new Semaphore(1);

	@PostConstruct
	public void start() {
		initSubscriptions();
	}

	/**
	 * Read the existing subscriptions from the database
	 */
	@SuppressWarnings("unused")
	@Scheduled(fixedDelay = 60000)
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

	@VisibleForTesting
	public int doInitSubscriptions() {
		synchronized (myInitSubscriptionsLock) {
			ourLog.debug("Starting init subscriptions");
			SearchParameterMap map = new SearchParameterMap();
			map.add(Subscription.SP_STATUS, new TokenOrListParam()
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.REQUESTED.toCode()))
				.addOr(new TokenParam(null, Subscription.SubscriptionStatus.ACTIVE.toCode())));
			map.setLoadSynchronousUpTo(SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS);

			IBundleProvider subscriptionBundleList = mySubscriptionProvidor.search(map);

			if (subscriptionBundleList.size() >= SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS) {
				ourLog.error("Currently over " + SubscriptionConstants.MAX_SUBSCRIPTION_RESULTS + " subscriptions.  Some subscriptions have not been loaded.");
			}

			List<IBaseResource> resourceList = subscriptionBundleList.getResources(0, subscriptionBundleList.size());

			Set<String> allIds = new HashSet<>();
			int changesCount = 0;
			for (IBaseResource resource : resourceList) {
				String nextId = resource.getIdElement().getIdPart();
				allIds.add(nextId);
				boolean changed = mySubscriptionProvidor.loadSubscription(resource);
				if (changed) {
					changesCount++;
				}
			}

			mySubscriptionRegistry.unregisterAllSubscriptionsNotInCollection(allIds);
			ourLog.trace("Finished init subscriptions - found {}", resourceList.size());

			return changesCount;
		}
	}
}

