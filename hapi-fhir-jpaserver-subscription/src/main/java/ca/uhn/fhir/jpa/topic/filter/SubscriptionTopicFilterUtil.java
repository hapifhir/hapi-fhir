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
package ca.uhn.fhir.jpa.topic.filter;

import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscriptionFilter;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

public final class SubscriptionTopicFilterUtil {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	private SubscriptionTopicFilterUtil() {}

	public static boolean matchFilters(
			@Nonnull IBaseResource theResource,
			@Nonnull String theResourceType,
			@Nonnull ISubscriptionTopicFilterMatcher theSubscriptionTopicFilterMatcher,
			@Nonnull CanonicalTopicSubscription topicSubscription) {
		boolean match = true;
		for (CanonicalTopicSubscriptionFilter filter : topicSubscription.getFilters()) {
			if (filter.getResourceType() == null
					|| "Resource".equals(filter.getResourceType())
					|| !filter.getResourceType().equals(theResourceType)) {
				continue;
			}
			if (!theSubscriptionTopicFilterMatcher.match(filter, theResource).matched()) {
				match = false;
				ourLog.debug(
						"Resource {} did not match filter {}.  Skipping remaining filters.",
						theResource.getIdElement().toUnqualifiedVersionless().getValue(),
						filter.asCriteriaString());
				break;
			}
			ourLog.debug(
					"Resource {} matches filter {}",
					theResource.getIdElement().toUnqualifiedVersionless().getValue(),
					filter.asCriteriaString());
		}
		return match;
	}
}
