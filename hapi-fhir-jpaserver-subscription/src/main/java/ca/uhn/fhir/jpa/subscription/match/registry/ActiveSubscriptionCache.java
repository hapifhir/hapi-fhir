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
package ca.uhn.fhir.jpa.subscription.match.registry;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

class ActiveSubscriptionCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ActiveSubscriptionCache.class);

	private final Map<String, ActiveSubscription> myCache = new ConcurrentHashMap<>();

	public ActiveSubscription get(String theIdPart) {
		return myCache.get(theIdPart);
	}

	public Collection<ActiveSubscription> getAll() {
		return Collections.unmodifiableCollection(myCache.values());
	}

	public int size() {
		return myCache.size();
	}

	public void put(String theSubscriptionId, ActiveSubscription theActiveSubscription) {
		myCache.put(theSubscriptionId, theActiveSubscription);
	}

	public synchronized ActiveSubscription remove(String theSubscriptionId) {
		Validate.notBlank(theSubscriptionId);

		ActiveSubscription activeSubscription = myCache.get(theSubscriptionId);
		if (activeSubscription == null) {
			return null;
		}

		myCache.remove(theSubscriptionId);
		return activeSubscription;
	}

	List<String> markAllSubscriptionsNotInCollectionForDeletionAndReturnIdsToDelete(Collection<String> theAllIds) {
		List<String> retval = new ArrayList<>();
		for (String next : new ArrayList<>(myCache.keySet())) {
			ActiveSubscription activeSubscription = myCache.get(next);
			if (theAllIds.contains(next)) {
				// In case we got a false positive from a race condition on a previous sync, unset the flag.
				activeSubscription.setFlagForDeletion(false);
			} else {
				if (activeSubscription.isFlagForDeletion()) {
					ourLog.info("Unregistering Subscription/{}", next);
					retval.add(next);
				} else {
					activeSubscription.setFlagForDeletion(true);
				}
			}
		}
		return retval;
	}

	/**
	 * R4B and R5 only
	 * @param theTopic
	 * @return a list of all subscriptions that are subscribed to the given topic
	 */
	public List<ActiveSubscription> getTopicSubscriptionsForTopic(String theTopic) {
		assert !isBlank(theTopic);
		return getAll().stream()
				.filter(as -> as.getSubscription().isTopicSubscription())
				.filter(as -> theTopic.equals(as.getSubscription().getTopic()))
				.collect(Collectors.toList());
	}

	public List<ActiveSubscription> getAllNonTopicSubscriptions() {
		return getAll().stream()
				.filter(as -> !as.getSubscription().isTopicSubscription())
				.collect(Collectors.toList());
	}
}
