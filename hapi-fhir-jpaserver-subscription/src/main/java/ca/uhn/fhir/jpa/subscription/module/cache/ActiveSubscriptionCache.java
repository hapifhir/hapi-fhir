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

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveSubscriptionCache {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ActiveSubscriptionCache.class);

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

	public void remove(String theSubscriptionId) {
		Validate.notBlank(theSubscriptionId);

		ActiveSubscription activeSubscription = myCache.get(theSubscriptionId);
		if (activeSubscription == null) {
			return;
		}

		activeSubscription.unregisterAll();
		myCache.remove(theSubscriptionId);
	}

	public void unregisterAllSubscriptionsNotInCollection(Collection<String> theAllIds) {
		for (String next : new ArrayList<>(myCache.keySet())) {
			if (!theAllIds.contains(next)) {
				ourLog.info("Unregistering Subscription/{}", next);
				remove(next);
			}
		}
	}

	@VisibleForTesting
	public void clearForUnitTests() {
		myCache.clear();
	}
}
