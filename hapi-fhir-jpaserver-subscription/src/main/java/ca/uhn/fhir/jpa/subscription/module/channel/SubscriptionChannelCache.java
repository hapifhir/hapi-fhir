package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriptionChannelCache {
	private final Map<String, SubscriptionChannelWithHandlers> myCache = new ConcurrentHashMap<>();

	public SubscriptionChannelWithHandlers get(String theChannelName) {
		return myCache.get(theChannelName);
	}

	public Collection<SubscriptionChannelWithHandlers> getAll() {
		return Collections.unmodifiableCollection(myCache.values());
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theChannelName, SubscriptionChannelWithHandlers theValue) {
		myCache.put(theChannelName, theValue);
	}

	public synchronized void remove(String theChannelName) {
		Validate.notBlank(theChannelName);

		SubscriptionChannelWithHandlers subscriptionChannelWithHandlers = myCache.get(theChannelName);
		if (subscriptionChannelWithHandlers == null) {
			return;
		}

		subscriptionChannelWithHandlers.close();
		myCache.remove(theChannelName);
	}

	public boolean containsKey(String theChannelName) {
		return myCache.containsKey(theChannelName);
	}
}
