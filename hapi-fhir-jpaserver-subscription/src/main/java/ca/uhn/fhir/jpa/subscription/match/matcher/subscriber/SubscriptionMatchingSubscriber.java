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
package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Collection;
import java.util.Optional;

import static ca.uhn.fhir.rest.server.messaging.BaseResourceMessage.OperationTypeEnum.DELETE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SubscriptionMatchingSubscriber implements MessageHandler {
	private final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchingSubscriber.class);
	public static final String SUBSCRIPTION_MATCHING_CHANNEL_NAME = "subscription-matching";

	@Autowired
	private ISubscriptionMatcher mySubscriptionMatcher;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;

	@Autowired
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	/**
	 * Constructor
	 */
	public SubscriptionMatchingSubscriber() {
		super();
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		matchActiveSubscriptionsAndDeliver(msg);
	}

	public void matchActiveSubscriptionsAndDeliver(ResourceModifiedMessage theMsg) {
		switch (theMsg.getOperationType()) {
			case CREATE:
			case UPDATE:
			case MANUALLY_TRIGGERED:
			case DELETE:
				break;
			default:
				ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
				// ignore anything else
				return;
		}

		if (theMsg.getPayload(myFhirContext) == null) {
			// inflate the message and ignore any resource that cannot be found.
			Optional<ResourceModifiedMessage> inflatedMsg =
					myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessageOrNull(theMsg);
			if (inflatedMsg.isEmpty()) {
				return;
			}
			theMsg = inflatedMsg.get();
		}

		// Interceptor call: SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED
		HookParams params = new HookParams().add(ResourceModifiedMessage.class, theMsg);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED, params)) {
			return;
		}

		try {
			doMatchActiveSubscriptionsAndDeliver(theMsg);
		} finally {
			// Interceptor call: SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED, params);
		}
	}

	private void doMatchActiveSubscriptionsAndDeliver(ResourceModifiedMessage theMsg) {
		IIdType resourceId = theMsg.getPayloadId(myFhirContext);

		Collection<ActiveSubscription> subscriptions = mySubscriptionRegistry.getAllNonTopicSubscriptions();

		ourLog.trace("Testing {} subscriptions for applicability", subscriptions.size());
		boolean anySubscriptionsMatchedResource = false;

		for (ActiveSubscription nextActiveSubscription : subscriptions) {
			anySubscriptionsMatchedResource |= processSubscription(theMsg, resourceId, nextActiveSubscription);
		}

		if (!anySubscriptionsMatchedResource) {
			// Interceptor call: SUBSCRIPTION_RESOURCE_MATCHED
			HookParams params = new HookParams().add(ResourceModifiedMessage.class, theMsg);
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS, params);
		}
	}

	/**
	 * Returns true if subscription matched, and processing completed successfully, and the message was sent to the delivery channel. False otherwise.
	 *
	 */
	private boolean processSubscription(
			ResourceModifiedMessage theMsg, IIdType theResourceId, ActiveSubscription theActiveSubscription) {
		// skip if the partitions don't match
		CanonicalSubscription subscription = theActiveSubscription.getSubscription();
		if (subscription != null
				&& theMsg.getPartitionId() != null
				&& theMsg.getPartitionId().hasPartitionIds()
				&& !subscription.getCrossPartitionEnabled()
				&& !theMsg.getPartitionId().hasPartitionId(subscription.getRequestPartitionId())) {
			return false;
		}
		String nextSubscriptionId = theActiveSubscription.getId();

		if (isNotBlank(theMsg.getSubscriptionId())) {
			if (!theMsg.getSubscriptionId().equals(nextSubscriptionId)) {
				// TODO KHS we should use a hash to look it up instead of this full table scan
				ourLog.debug(
						"Ignoring subscription {} because it is not {}",
						nextSubscriptionId,
						theMsg.getSubscriptionId());
				return false;
			}
		}

		if (!resourceTypeIsAppropriateForSubscription(theActiveSubscription, theResourceId)) {
			return false;
		}

		if (theMsg.getOperationType().equals(DELETE)) {
			if (!theActiveSubscription.getSubscription().getSendDeleteMessages()) {
				ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
				return false;
			}
		}

		InMemoryMatchResult matchResult;
		if (theActiveSubscription.getCriteria().getType() == SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION) {
			matchResult = mySubscriptionMatcher.match(theActiveSubscription.getSubscription(), theMsg);
			if (!matchResult.matched()) {
				ourLog.trace(
						"Subscription {} was not matched by resource {} {}",
						theActiveSubscription.getId(),
						theResourceId.toUnqualifiedVersionless().getValue(),
						matchResult.isInMemory() ? "in-memory" : "by querying the repository");
				return false;
			}
			ourLog.debug(
					"Subscription {} was matched by resource {} {}",
					theActiveSubscription.getId(),
					theResourceId.toUnqualifiedVersionless().getValue(),
					matchResult.isInMemory() ? "in-memory" : "by querying the repository");
		} else {
			ourLog.trace(
					"Subscription {} was not matched by resource {} - No search expression",
					theActiveSubscription.getId(),
					theResourceId.toUnqualifiedVersionless().getValue());
			matchResult = InMemoryMatchResult.successfulMatch();
			matchResult.setInMemory(true);
		}

		IBaseResource payload = theMsg.getNewPayload(myFhirContext);
		return mySubscriptionMatchDeliverer.deliverPayload(payload, theMsg, theActiveSubscription, matchResult);
	}

	private boolean resourceTypeIsAppropriateForSubscription(
			ActiveSubscription theActiveSubscription, IIdType theResourceId) {
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = theActiveSubscription.getCriteria();
		String subscriptionId = theActiveSubscription.getId();
		String resourceType = theResourceId.getResourceType();

		// see if the criteria matches the created object
		ourLog.trace("Checking subscription {} for {} with criteria {}", subscriptionId, resourceType, criteria);

		if (criteria == null) {
			ourLog.trace("Subscription {} has no criteria - Not matching", subscriptionId);
			return false;
		}

		switch (criteria.getType()) {
			default:
			case SEARCH_EXPRESSION:
			case MULTITYPE_EXPRESSION:
				boolean contains = criteria.getApplicableResourceTypes().contains(resourceType);
				ourLog.trace("Subscription {} applicable resource type check: {}", subscriptionId, contains);
				return contains;
			case STARTYPE_EXPRESSION:
				boolean match = !resourceType.equals("Subscription");
				ourLog.trace("Subscription {} start resource type check: {}", subscriptionId, match);
				return match;
		}
	}
}
