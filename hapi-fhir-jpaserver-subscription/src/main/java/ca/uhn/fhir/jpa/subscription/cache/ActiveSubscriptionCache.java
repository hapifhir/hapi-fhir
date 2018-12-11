package ca.uhn.fhir.jpa.subscription.cache;

import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveSubscriptionCache {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ActiveSubscriptionCache.class);

	private final Map<String, ActiveSubscription> myCache = new ConcurrentHashMap<>();

	public ActiveSubscription get(String theIdPart) {
		return myCache.get(theIdPart);
	}

	public Collection<ActiveSubscription> getAll() {
		return Collections.unmodifiableCollection(myCache.values());
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theSubscriptionId, ActiveSubscription theValue) {
		myCache.put(theSubscriptionId, theValue);
	}

	public void remove(String theSubscriptionId) {
		Validate.notBlank(theSubscriptionId);

		ActiveSubscription activeSubscription = myCache.get(theSubscriptionId);
		if (activeSubscription == null) {
			return;
		}

		activeSubscription.unregisterAll();
		myCache.remove(theSubscriptionId);
	}

	public void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		for (String next : new ArrayList<>(myCache.keySet())) {
			if (!theAllIds.contains(next)) {
				ourLog.info("Unregistering Subscription/{}", next);
				remove(next);
			}
		}
	}
}
