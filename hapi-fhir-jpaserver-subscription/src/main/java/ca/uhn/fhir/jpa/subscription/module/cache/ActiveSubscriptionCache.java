package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

	public void put(String theSubscriptionId, ActiveSubscription theValue) {
		myCache.put(theSubscriptionId, theValue);
	}

	public synchronized void remove(String theSubscriptionId) {
		Validate.notBlank(theSubscriptionId);

		ActiveSubscription activeSubscription = myCache.get(theSubscriptionId);
		if (activeSubscription == null) {
			return;
		}

		activeSubscription.close();
		myCache.remove(theSubscriptionId);
	}

	public void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		for (String next : new ArrayList<>(myCache.keySet())) {
			ActiveSubscription activeSubscription = myCache.get(next);
			if (theAllIds.contains(next)) {
				// In case we got a false positive from a race condition on a previous sync, unset the flag.
				activeSubscription.setFlagForDeletion(false);
			} else {
				if (activeSubscription.isFlagForDeletion()) {
					ourLog.info("Unregistering Subscription/{}", next);
					remove(next);
				} else {
					activeSubscription.setFlagForDeletion(true);
				}
			}
		}
	}
}
