package ca.uhn.fhir.jpa.topic;

import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveSubscriptionTopicCache {
	// We canonicalize on R5 SubscriptionTopic and convert back to R4B when necessary
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

	/**
	 * @return the number of entries removed
	 */
	public int removeIdsNotInCollection(Set<String> theIdsToRetain) {
		int retval = 0;
		HashSet<String> safeCopy = new HashSet<>(myCache.keySet());

		for (String next : safeCopy) {
			if (!theIdsToRetain.contains(next)) {
				myCache.remove(next);
				++retval;
			}
		}
		return retval;
	}

	public Collection<SubscriptionTopic> getAll() {
		return myCache.values();
	}

    public void remove(String theSubscriptionTopicId) {
		myCache.remove(theSubscriptionTopicId);
    }
}
