/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
