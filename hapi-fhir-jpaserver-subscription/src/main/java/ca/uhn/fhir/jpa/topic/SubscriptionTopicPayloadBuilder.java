/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SubscriptionStatus;

import java.util.List;
import java.util.UUID;

public class SubscriptionTopicPayloadBuilder {
	private final FhirContext myFhirContext;

	public SubscriptionTopicPayloadBuilder(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	public IBaseBundle buildPayload(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl, RestOperationTypeEnum theRestOperationType) {
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// WIP STR5 set eventsSinceSubscriptionStart from the database
		int eventsSinceSubscriptionStart = 1;
		IBaseResource subscriptionStatus = buildSubscriptionStatus(theResources, theActiveSubscription, theTopicUrl, eventsSinceSubscriptionStart);

		FhirVersionEnum fhirVersion = myFhirContext.getVersion().getVersion();

		// WIP STR5 add support for notificationShape include, revinclude

		if (fhirVersion == FhirVersionEnum.R4B) {
			bundleBuilder.setType(Bundle.BundleType.HISTORY.toCode());
			subscriptionStatus = VersionConvertorFactory_43_50.convertResource((org.hl7.fhir.r5.model.SubscriptionStatus) subscriptionStatus);
		} else if (fhirVersion == FhirVersionEnum.R5) {
			bundleBuilder.setType(Bundle.BundleType.SUBSCRIPTIONNOTIFICATION.toCode());
		} else {
			throw new IllegalStateException(Msg.code(2331) + "SubscriptionTopic subscriptions are not supported on FHIR version: " + fhirVersion);
		}
		// WIP STR5 is this the right type of entry? see http://hl7.org/fhir/subscriptionstatus-examples.html
		// WIP STR5 Also see http://hl7.org/fhir/R4B/notification-full-resource.json.html need to conform to these
		bundleBuilder.addCollectionEntry(subscriptionStatus);
		for (IBaseResource resource : theResources) {
			switch(theRestOperationType) {
				case CREATE:
					bundleBuilder.addTransactionCreateEntry(resource);
					break;
				case UPDATE:
					bundleBuilder.addTransactionUpdateEntry(resource);
					break;
				case DELETE:
					bundleBuilder.addTransactionDeleteEntry(resource);
					break;
			}
		}

		return bundleBuilder.getBundle();
	}


	private SubscriptionStatus buildSubscriptionStatus(List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl, int theEventsSinceSubscriptionStart) {
		SubscriptionStatus subscriptionStatus = new SubscriptionStatus();
		subscriptionStatus.setId(UUID.randomUUID().toString());
		subscriptionStatus.setStatus(Enumerations.SubscriptionStatusCodes.ACTIVE);
		subscriptionStatus.setType(SubscriptionStatus.SubscriptionNotificationType.EVENTNOTIFICATION);
		// WIP STR5 count events since subscription start and set eventsSinceSubscriptionStart
		// store counts by subscription id
		subscriptionStatus.setEventsSinceSubscriptionStart(theEventsSinceSubscriptionStart);
		SubscriptionStatus.SubscriptionStatusNotificationEventComponent event = subscriptionStatus.addNotificationEvent();
		event.setEventNumber(theEventsSinceSubscriptionStart);
		if (theResources.size() > 0) {
			event.setFocus(new Reference(theResources.get(0).getIdElement()));
		}
		subscriptionStatus.setSubscription(new Reference(theActiveSubscription.getSubscription().getIdElement(myFhirContext)));
		subscriptionStatus.setTopic(theTopicUrl);
		return subscriptionStatus;
	}

}
