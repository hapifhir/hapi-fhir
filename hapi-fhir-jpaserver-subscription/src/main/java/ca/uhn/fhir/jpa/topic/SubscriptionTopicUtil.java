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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.BaseReference;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.SubscriptionStatus;
import org.hl7.fhir.r5.model.SubscriptionTopic;

import java.util.List;
import java.util.Objects;

public class SubscriptionTopicUtil {
	public static boolean matches(
			BaseResourceMessage.OperationTypeEnum theOperationType,
			List<Enumeration<SubscriptionTopic.InteractionTrigger>> theSupportedInteractions) {
		for (Enumeration<SubscriptionTopic.InteractionTrigger> next : theSupportedInteractions) {
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.CREATE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.CREATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.UPDATE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.UPDATE) {
				return true;
			}
			if (next.getValue() == SubscriptionTopic.InteractionTrigger.DELETE
					&& theOperationType == BaseResourceMessage.OperationTypeEnum.DELETE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Extracts source resource from bundle contained in {@link ResourceModifiedJsonMessage} payload.
	 * Used for R5 resource modified message handling.
	 */
	public static IBaseResource extractResourceFromBundle(FhirContext myFhirContext, IBaseBundle theBundle) {
		List<IBaseResource> resources = BundleUtil.toListOfResources(myFhirContext, theBundle);

		return resources.stream()
				.filter(SubscriptionStatus.class::isInstance)
				.map(SubscriptionStatus.class::cast)
				.flatMap(subscriptionStatus -> subscriptionStatus.getNotificationEvent().stream())
				.filter(SubscriptionStatus.SubscriptionStatusNotificationEventComponent::hasFocus)
				.map(SubscriptionStatus.SubscriptionStatusNotificationEventComponent::getFocus)
				.map(BaseReference::getResource)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

	/**
	 * Checks if {@link CanonicalSubscription} has EMPTY {@link org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent}
	 * Used for R5/R4B/R4 Notification Status object building.
	 */
	public static boolean isEmptyContentTopicSubscription(CanonicalSubscription theCanonicalSubscription) {
		return theCanonicalSubscription.isTopicSubscription()
				&& org.hl7.fhir.r5.model.Subscription.SubscriptionPayloadContent.EMPTY
						== theCanonicalSubscription.getTopicSubscription().getContent();
	}
}
