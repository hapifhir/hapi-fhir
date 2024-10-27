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
package ca.uhn.fhir.jpa.topic.status;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.topic.SubscriptionTopicUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SubscriptionStatus;

import java.util.List;
import java.util.UUID;

public class R5NotificationStatusBuilder implements INotificationStatusBuilder<SubscriptionStatus> {
	private final FhirContext myFhirContext;

	public R5NotificationStatusBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public SubscriptionStatus buildNotificationStatus(
			List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl) {
		long eventNumber = theActiveSubscription.getDeliveriesCount();
		CanonicalSubscription canonicalSubscription = theActiveSubscription.getSubscription();

		SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
		subscriptionStatus.setId(UUID.randomUUID().toString());
		subscriptionStatus.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscriptionStatus.setType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION);
		// WIP STR5 events-since-subscription-start should be read from the database
		subscriptionStatus.setEventsSinceSubscriptionStart(eventNumber);
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent event =
				subscriptionStatus.addNotificationEvent();
		event.setEventNumber(eventNumber);
		if (!theResources.isEmpty() && !SubscriptionTopicUtil.isEmptyContentTopicSubscription(canonicalSubscription)) {
			event.setFocus(new Reference(theResources.get(0).getIdElement()));
		}
		subscriptionStatus.setSubscription(
				new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopicUrl);
		return subscriptionStatus;
	}
}
