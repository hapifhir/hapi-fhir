package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class SubscriptionChannelCache {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegistry.class);

	private final Map<String, SubscriptionChannelWithHandlers> myCache = new ConcurrentHashMap<>();

	public SubscriptionChannelWithHandlers get(String theChannelName) {
		return myCache.get(theChannelName);
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theChannelName, SubscriptionChannelWithHandlers theValue) {
		myCache.put(theChannelName, theValue);
	}

	synchronized void closeAndRemove(String theChannelName) {
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

	void logForUnitTest() {
		for (String key : myCache.keySet()) {
			ourLog.info("SubscriptionChannelCache: {}", key);
		}
	}
}
