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
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_43_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4b.model.SubscriptionStatus;

import java.util.List;

public class R4BNotificationStatusBuilder implements INotificationStatusBuilder<SubscriptionStatus> {
	private final R5NotificationStatusBuilder myR5NotificationStatusBuilder;

	public R4BNotificationStatusBuilder(FhirContext theFhirContext) {
		myR5NotificationStatusBuilder = new R5NotificationStatusBuilder(theFhirContext);
	}

	@Override
	public SubscriptionStatus buildNotificationStatus(
			List<IBaseResource> theResources, ActiveSubscription theActiveSubscription, String theTopicUrl) {
		org.hl7.fhir.r5.model.SubscriptionStatus subscriptionStatus =
				myR5NotificationStatusBuilder.buildNotificationStatus(theResources, theActiveSubscription, theTopicUrl);
		return (SubscriptionStatus) VersionConvertorFactory_43_50.convertResource(subscriptionStatus);
	}
}
