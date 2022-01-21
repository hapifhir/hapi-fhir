package ca.uhn.fhir.jpa.subscription.channel.subscription;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
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
