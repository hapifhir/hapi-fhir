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
import java.util.Set;

public class SubscriptionTopicRegistry {
	private final ActiveSubscriptionTopicCache myActiveSubscriptionTopicCache = new ActiveSubscriptionTopicCache();

	public SubscriptionTopicRegistry() {}

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

	public void unregister(String theSubscriptionTopicId) {
		myActiveSubscriptionTopicCache.remove(theSubscriptionTopicId);
	}
}
