package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.ISubscriptionMatcher;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Collection;

import static ca.uhn.fhir.rest.server.messaging.BaseResourceMessage.OperationTypeEnum.DELETE;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;

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

		// Interceptor call: SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, theMsg);
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

		Collection<ActiveSubscription> subscriptions = mySubscriptionRegistry.getAll();

		ourLog.trace("Testing {} subscriptions for applicability", subscriptions.size());
		boolean resourceMatched = false;

		for (ActiveSubscription nextActiveSubscription : subscriptions) {
			// skip if the partitions don't match
			CanonicalSubscription subscription = nextActiveSubscription.getSubscription();
			if (subscription != null && subscription.getRequestPartitionId() != null && theMsg.getPartitionId() != null &&
				theMsg.getPartitionId().hasPartitionIds() && !subscription.getCrossPartitionEnabled() &&
				!theMsg.getPartitionId().hasPartitionId(subscription.getRequestPartitionId())) {
				continue;
			}
			String nextSubscriptionId = getId(nextActiveSubscription);

			if (isNotBlank(theMsg.getSubscriptionId())) {
				if (!theMsg.getSubscriptionId().equals(nextSubscriptionId)) {
					// TODO KHS we should use a hash to look it up instead of this full table scan
					ourLog.debug("Ignoring subscription {} because it is not {}", nextSubscriptionId, theMsg.getSubscriptionId());
					continue;
				}
			}

			if (!resourceTypeIsAppropriateForSubscription(nextActiveSubscription, resourceId)) {
				continue;
			}

			if (theMsg.getOperationType().equals(DELETE)) {
				if (!nextActiveSubscription.getSubscription().getSendDeleteMessages()) {
					ourLog.trace("Not processing modified message for {}", theMsg.getOperationType());
					return;
				}
			}

			InMemoryMatchResult matchResult;
			if (nextActiveSubscription.getCriteria().getType() == SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION) {
				matchResult = mySubscriptionMatcher.match(nextActiveSubscription.getSubscription(), theMsg);
				if (!matchResult.matched()) {
					ourLog.trace("Subscription {} was not matched by resource {} {}",
						nextActiveSubscription.getId(),
						resourceId.toUnqualifiedVersionless().getValue(),
						matchResult.isInMemory() ? "in-memory" : "by querying the repository");
					continue;
				}
				ourLog.debug("Subscription {} was matched by resource {} {}",
					nextActiveSubscription.getId(),
					resourceId.toUnqualifiedVersionless().getValue(),
					matchResult.isInMemory() ? "in-memory" : "by querying the repository");
			} else {
				ourLog.trace("Subscription {} was not matched by resource {} - No search expression",
					nextActiveSubscription.getId(),
					resourceId.toUnqualifiedVersionless().getValue());
				matchResult = InMemoryMatchResult.successfulMatch();
				matchResult.setInMemory(true);
			}

			IBaseResource payload = theMsg.getNewPayload(myFhirContext);

			EncodingEnum encoding = null;
			if (subscription != null && subscription.getPayloadString() != null && !subscription.getPayloadString().isEmpty()) {
				encoding = EncodingEnum.forContentType(subscription.getPayloadString());
			}
			encoding = defaultIfNull(encoding, EncodingEnum.JSON);

			ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
			deliveryMsg.setPartitionId(theMsg.getPartitionId());

			if (payload != null) {
				deliveryMsg.setPayload(myFhirContext, payload, encoding);
			} else {
				deliveryMsg.setPayloadId(theMsg.getPayloadId(myFhirContext));
			}
			deliveryMsg.setSubscription(subscription);
			deliveryMsg.setOperationType(theMsg.getOperationType());
			deliveryMsg.setTransactionId(theMsg.getTransactionId());
			deliveryMsg.copyAdditionalPropertiesFrom(theMsg);

			// Interceptor call: SUBSCRIPTION_RESOURCE_MATCHED
			HookParams params = new HookParams()
				.add(CanonicalSubscription.class, nextActiveSubscription.getSubscription())
				.add(ResourceDeliveryMessage.class, deliveryMsg)
				.add(InMemoryMatchResult.class, matchResult);
			if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED, params)) {
				return;
			}

			resourceMatched |= sendToDeliveryChannel(nextActiveSubscription, deliveryMsg);
		}

		if (!resourceMatched) {
			// Interceptor call: SUBSCRIPTION_RESOURCE_MATCHED
			HookParams params = new HookParams()
				.add(ResourceModifiedMessage.class, theMsg);
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS, params);
		}
	}

	private boolean sendToDeliveryChannel(ActiveSubscription nextActiveSubscription, ResourceDeliveryMessage theDeliveryMsg) {
		boolean retVal = false;
		ResourceDeliveryJsonMessage wrappedMsg = new ResourceDeliveryJsonMessage(theDeliveryMsg);
		MessageChannel deliveryChannel = mySubscriptionChannelRegistry.getDeliverySenderChannel(nextActiveSubscription.getChannelName());
		if (deliveryChannel != null) {
			retVal = true;
			trySendToDeliveryChannel(wrappedMsg, deliveryChannel);
		} else {
			ourLog.warn("Do not have delivery channel for subscription {}", nextActiveSubscription.getId());
		}
		return retVal;
	}

	private void trySendToDeliveryChannel(ResourceDeliveryJsonMessage theWrappedMsg, MessageChannel theDeliveryChannel) {
		try {
			boolean success = theDeliveryChannel.send(theWrappedMsg);
			if (!success) {
				ourLog.warn("Failed to send message to Delivery Channel.");
			}
		} catch (RuntimeException e) {
			ourLog.error("Failed to send message to Delivery Channel", e);
			throw new RuntimeException(Msg.code(7) + "Failed to send message to Delivery Channel", e);
		}
	}

	private String getId(ActiveSubscription theActiveSubscription) {
		return theActiveSubscription.getId();
	}

	private boolean resourceTypeIsAppropriateForSubscription(ActiveSubscription theActiveSubscription, IIdType theResourceId) {
		SubscriptionCriteriaParser.SubscriptionCriteria criteria = theActiveSubscription.getCriteria();
		String subscriptionId = getId(theActiveSubscription);
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
