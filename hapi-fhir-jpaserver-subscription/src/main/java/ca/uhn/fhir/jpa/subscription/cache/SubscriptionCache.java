package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

class SubscriptionCache {
	private SubscriptionCacheById<CanonicalSubscription> myCache = new SubscriptionCacheById<>();

	public CanonicalSubscription get(String theIdPart) {
		return myCache.get(theIdPart);
	}

	public Collection<CanonicalSubscription> getAll() {
		return myCache.getAll();
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theSubscriptionId, CanonicalSubscription theSubscription) {
		myCache.put(theSubscriptionId, theSubscription);
	}

	public Set<String> keySet() {
		return myCache.keySet();
	}

	public CanonicalSubscription remove(String theSubscriptionId) {
		return myCache.remove(theSubscriptionId);
	}
}
