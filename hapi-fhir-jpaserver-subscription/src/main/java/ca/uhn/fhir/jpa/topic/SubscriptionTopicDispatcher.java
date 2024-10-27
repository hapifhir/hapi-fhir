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
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionDeliveryRequest;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalTopicSubscription;
import ca.uhn.fhir.jpa.topic.filter.ISubscriptionTopicFilterMatcher;
import ca.uhn.fhir.jpa.topic.filter.SubscriptionTopicFilterUtil;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

import java.util.List;
import java.util.UUID;

/**
 * Subscription topic notifications are natively supported in R5, R4B.  They are also partially supported and in R4
 * via the subscription backport spec <a href="http://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/components.html">Subscription Backport</a>.
 * In all versions, it is possible for a FHIR Repository to submit topic subscription notifications triggered by some
 * arbitrary "business event".  In R5 and R4B most subscription topic notifications will be triggered by a SubscriptionTopic
 * match.  However, in the R4 backport, the SubscriptionTopic is not supported and the SubscriptionTopicDispatcher service
 * is provided to generate those notifications instead.  Any custom java extension to the FHIR repository can @Autowire this service to
 * send topic notifications to all Subscription resources subscribed to that topic.
 */
public class SubscriptionTopicDispatcher {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();
	private final FhirContext myFhirContext;
	private final SubscriptionRegistry mySubscriptionRegistry;
	private final SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;
	private final SubscriptionTopicPayloadBuilder mySubscriptionTopicPayloadBuilder;

	public SubscriptionTopicDispatcher(
			FhirContext theFhirContext,
			SubscriptionRegistry theSubscriptionRegistry,
			SubscriptionMatchDeliverer theSubscriptionMatchDeliverer,
			SubscriptionTopicPayloadBuilder theSubscriptionTopicPayloadBuilder) {
		myFhirContext = theFhirContext;
		mySubscriptionRegistry = theSubscriptionRegistry;
		mySubscriptionMatchDeliverer = theSubscriptionMatchDeliverer;
		mySubscriptionTopicPayloadBuilder = theSubscriptionTopicPayloadBuilder;
	}

	/**
	 * Deliver a Subscription topic notification to all subscriptions for the given topic.
	 *
	 * @param theTopicUrl    Deliver to subscriptions for this topic
	 * @param theResources   The list of resources to deliver.  The first resource will be the primary "focus" resource per the Subscription documentation.
	 *                       This list should _not_ include the SubscriptionStatus.  The SubscriptionStatus will be added as the first element to
	 *                       the delivered bundle.  The reason for this is that the SubscriptionStatus needs to reference the subscription ID, which is
	 *                       not known until the bundle is delivered.
	 * @param theRequestType The type of request that led to this dispatch.  This determines the request type of the bundle entries
	 * @return The number of subscription notifications that were successfully queued for delivery
	 */
	public int dispatch(String theTopicUrl, List<IBaseResource> theResources, RestOperationTypeEnum theRequestType) {
		SubscriptionTopicDispatchRequest subscriptionTopicDispatchRequest = new SubscriptionTopicDispatchRequest(
				theTopicUrl,
				theResources,
				(f, r) -> InMemoryMatchResult.successfulMatch(),
				theRequestType,
				null,
				null,
				null);
		return dispatch(subscriptionTopicDispatchRequest);
	}

	/**
	 * Deliver a Subscription topic notification to all subscriptions for the given topic.
	 *
	 * @param theSubscriptionTopicDispatchRequest contains the topic URL, the list of resources to deliver, and the request type
	 * @return The number of subscription notifications that were successfully queued for delivery
	 */
	public int dispatch(SubscriptionTopicDispatchRequest theSubscriptionTopicDispatchRequest) {
		int count = 0;

		List<ActiveSubscription> topicSubscriptions =
				mySubscriptionRegistry.getTopicSubscriptionsByTopic(theSubscriptionTopicDispatchRequest.getTopicUrl());
		if (!topicSubscriptions.isEmpty()) {
			for (ActiveSubscription activeSubscription : topicSubscriptions) {
				boolean success = matchFiltersAndDeliver(theSubscriptionTopicDispatchRequest, activeSubscription);
				if (success) {
					count++;
				}
			}
		}
		return count;
	}

	private boolean matchFiltersAndDeliver(
			SubscriptionTopicDispatchRequest theSubscriptionTopicDispatchRequest,
			ActiveSubscription theActiveSubscription) {

		String topicUrl = theSubscriptionTopicDispatchRequest.getTopicUrl();
		List<IBaseResource> resources = theSubscriptionTopicDispatchRequest.getResources();
		ISubscriptionTopicFilterMatcher subscriptionTopicFilterMatcher =
				theSubscriptionTopicDispatchRequest.getSubscriptionTopicFilterMatcher();

		if (resources.size() > 0) {
			IBaseResource firstResource = resources.get(0);
			String resourceType = myFhirContext.getResourceType(firstResource);
			CanonicalSubscription subscription = theActiveSubscription.getSubscription();
			CanonicalTopicSubscription topicSubscription = subscription.getTopicSubscription();
			if (topicSubscription.hasFilters()) {
				ourLog.debug(
						"Checking if resource {} matches {} subscription filters on {}",
						firstResource.getIdElement().toUnqualifiedVersionless().getValue(),
						topicSubscription.getFilters().size(),
						subscription
								.getIdElement(myFhirContext)
								.toUnqualifiedVersionless()
								.getValue());

				if (!SubscriptionTopicFilterUtil.matchFilters(
						firstResource, resourceType, subscriptionTopicFilterMatcher, topicSubscription)) {
					return false;
				}
			}
		}
		theActiveSubscription.incrementDeliveriesCount();
		IBaseBundle bundlePayload = mySubscriptionTopicPayloadBuilder.buildPayload(
				resources, theActiveSubscription, topicUrl, theSubscriptionTopicDispatchRequest.getRequestType());
		bundlePayload.setId(UUID.randomUUID().toString());
		SubscriptionDeliveryRequest subscriptionDeliveryRequest = new SubscriptionDeliveryRequest(
				bundlePayload, theActiveSubscription, theSubscriptionTopicDispatchRequest);
		return mySubscriptionMatchDeliverer.deliverPayload(
				subscriptionDeliveryRequest, theSubscriptionTopicDispatchRequest.getInMemoryMatchResult());
	}
}
