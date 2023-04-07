package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveSubscriptionTopicCache {
	// We'll canonicalize on R5 SubscriptionTopic
	private final Map<String, SubscriptionTopic> myCache = new ConcurrentHashMap<>();

	public int size() {
		return myCache.size();
	}
}
