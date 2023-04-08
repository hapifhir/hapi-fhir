package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r4b.model.SubscriptionTopic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveSubscriptionTopicCache {
	// We'll canonicalize on R5 SubscriptionTopic
	private final Map<String, SubscriptionTopic> myCache = new ConcurrentHashMap<>();

	public int size() {
		return myCache.size();
	}

	/**
	 * @return true if the subscription topic was added, false if it was already present
	 */
	public boolean add(SubscriptionTopic theSubscriptionTopic) {
		String key = theSubscriptionTopic.getIdElement().getIdPart();
		SubscriptionTopic previousValue = myCache.put(key, theSubscriptionTopic);
		return previousValue == null;
	}

	public void removeIdsNotInCollection(Set<String> theIdsToRetain) {
		HashSet<String> safeCopy = new HashSet<>(myCache.keySet());

		for (String next : safeCopy) {
			if (!theIdsToRetain.contains(next)) {
				myCache.remove(next);
			}
		}
	}

	public Collection<SubscriptionTopic> getAll() {
		return myCache.values();
	}
}
