package ca.uhn.fhir.jpa.subscription.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class SubscriptionCacheById<T> {
	private Map<String, T> myCache = new ConcurrentHashMap<>();

	public T get(String theIdPart) {
		return myCache.get(theIdPart);
	}

	public Collection<T> getAll() {
		return myCache.values();
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theSubscriptionId, T theValue) {
		myCache.put(theSubscriptionId, theValue);
	}

	public T remove(String theSubscriptionId) {
		return myCache.remove(theSubscriptionId);
	}

	public Set<String> keySet() {
		return myCache.keySet();
	}
}
