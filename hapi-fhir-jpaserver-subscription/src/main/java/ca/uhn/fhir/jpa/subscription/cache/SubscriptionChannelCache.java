package ca.uhn.fhir.jpa.subscription.cache;

import ca.uhn.fhir.jpa.subscription.CanonicalSubscription;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

class SubscriptionChannelCache {
	private SubscriptionCacheById<SubscribableChannel> cache = new SubscriptionCacheById<>();

	public SubscribableChannel get(String theIdPart) {
		return cache.get(theIdPart);
	}

	public void put(String theSubscriptionId, SubscribableChannel theDeliveryChannel) {
		cache.put(theSubscriptionId, theDeliveryChannel);
	}

	public void remove(String theSubscriptionId) {
		cache.remove(theSubscriptionId);
	}
}
