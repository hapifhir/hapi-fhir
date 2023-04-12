package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.Collection;
import java.util.Set;

public class SubscriptionTopicRegistry {
	private final ActiveSubscriptionTopicCache myActiveSubscriptionTopicCache = new ActiveSubscriptionTopicCache();

	public SubscriptionTopicRegistry() {
	}

	public int size() {
		return myActiveSubscriptionTopicCache.size();
	}

	public boolean register(SubscriptionTopic resource) {
		return myActiveSubscriptionTopicCache.add(resource);
	}

	public void unregisterAllIdsNotInCollection(Set<String> theIdsToRetain) {
		myActiveSubscriptionTopicCache.removeIdsNotInCollection(theIdsToRetain);
	}

	public Collection<SubscriptionTopic> getAll() {
		return myActiveSubscriptionTopicCache.getAll();
	}
}
