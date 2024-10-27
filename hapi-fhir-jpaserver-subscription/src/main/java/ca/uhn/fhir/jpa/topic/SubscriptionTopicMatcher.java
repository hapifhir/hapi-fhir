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

import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.List;

public class SubscriptionTopicMatcher {
	private final SubscriptionTopicSupport mySubscriptionTopicSupport;
	private final SubscriptionTopic myTopic;
	private final MemoryCacheService myMemoryCacheService;

	public SubscriptionTopicMatcher(
			SubscriptionTopicSupport theSubscriptionTopicSupport,
			SubscriptionTopic theTopic,
			MemoryCacheService memoryCacheService) {
		mySubscriptionTopicSupport = theSubscriptionTopicSupport;
		myTopic = theTopic;
		myMemoryCacheService = memoryCacheService;
	}

	public InMemoryMatchResult match(ResourceModifiedMessage theMsg) {
		IBaseResource resource = theMsg.getPayload(mySubscriptionTopicSupport.getFhirContext());
		String resourceName = resource.fhirType();

		List<SubscriptionTopic.SubscriptionTopicResourceTriggerComponent> triggers = myTopic.getResourceTrigger();
		for (SubscriptionTopic.SubscriptionTopicResourceTriggerComponent next : triggers) {
			if (resourceName.equals(next.getResource())) {
				SubscriptionTriggerMatcher matcher =
						new SubscriptionTriggerMatcher(mySubscriptionTopicSupport, theMsg, next, myMemoryCacheService);
				InMemoryMatchResult result = matcher.match();
				if (result.matched()) {
					// as soon as one trigger matches, we're done
					return result;
				}
			}
		}
		return InMemoryMatchResult.noMatch();
	}
}
