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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.channel.api.PayloadTooLargeException;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageChannel;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class SubscriptionMatchDeliverer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatchDeliverer.class);
	private final FhirContext myFhirContext;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;
	private final SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	public SubscriptionMatchDeliverer(
			FhirContext theFhirContext,
			IInterceptorBroadcaster theInterceptorBroadcaster,
			SubscriptionChannelRegistry theSubscriptionChannelRegistry) {
		myFhirContext = theFhirContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySubscriptionChannelRegistry = theSubscriptionChannelRegistry;
	}

	public boolean deliverPayload(
			@Nullable IBaseResource thePayload,
			@Nonnull ResourceModifiedMessage theMsg,
			@Nonnull ActiveSubscription theActiveSubscription,
			@Nullable InMemoryMatchResult theInMemoryMatchResult) {
		SubscriptionDeliveryRequest subscriptionDeliveryRequest;
		if (thePayload != null) {
			subscriptionDeliveryRequest = new SubscriptionDeliveryRequest(thePayload, theMsg, theActiveSubscription);
		} else {
			subscriptionDeliveryRequest =
					new SubscriptionDeliveryRequest(theMsg.getPayloadId(myFhirContext), theMsg, theActiveSubscription);
		}
		ResourceDeliveryMessage deliveryMsg = buildResourceDeliveryMessage(subscriptionDeliveryRequest);
		deliveryMsg.copyAdditionalPropertiesFrom(theMsg);

		return sendToDeliveryChannel(theActiveSubscription, theInMemoryMatchResult, deliveryMsg);
	}

	public boolean deliverPayload(
			@Nonnull SubscriptionDeliveryRequest subscriptionDeliveryRequest,
			@Nullable InMemoryMatchResult theInMemoryMatchResult) {
		ResourceDeliveryMessage deliveryMsg = buildResourceDeliveryMessage(subscriptionDeliveryRequest);

		return sendToDeliveryChannel(
				subscriptionDeliveryRequest.getActiveSubscription(), theInMemoryMatchResult, deliveryMsg);
	}

	private boolean sendToDeliveryChannel(
			@Nonnull ActiveSubscription theActiveSubscription,
			@Nullable InMemoryMatchResult theInMemoryMatchResult,
			@Nonnull ResourceDeliveryMessage deliveryMsg) {
		if (!callHooks(theActiveSubscription, theInMemoryMatchResult, deliveryMsg)) {
			return false;
		}

		boolean retVal = false;
		ResourceDeliveryJsonMessage wrappedMsg = new ResourceDeliveryJsonMessage(deliveryMsg);
		MessageChannel deliveryChannel =
				mySubscriptionChannelRegistry.getDeliverySenderChannel(theActiveSubscription.getChannelName());
		if (deliveryChannel != null) {
			retVal = true;
			trySendToDeliveryChannel(wrappedMsg, deliveryChannel);
		} else {
			ourLog.warn("Do not have delivery channel for subscription {}", theActiveSubscription.getId());
		}
		return retVal;
	}

	private ResourceDeliveryMessage buildResourceDeliveryMessage(@Nonnull SubscriptionDeliveryRequest theRequest) {
		EncodingEnum encoding = null;

		CanonicalSubscription subscription = theRequest.getSubscription();

		if (subscription != null
				&& subscription.getPayloadString() != null
				&& !subscription.getPayloadString().isEmpty()) {
			encoding = EncodingEnum.forContentType(subscription.getPayloadString());
		}
		encoding = defaultIfNull(encoding, EncodingEnum.JSON);

		ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
		deliveryMsg.setPartitionId(theRequest.getRequestPartitionId());

		if (theRequest.hasPayload()) {
			deliveryMsg.setPayload(myFhirContext, theRequest.getPayload(), encoding);
		} else {
			deliveryMsg.setPayloadId(theRequest.getPayloadId());
		}
		deliveryMsg.setSubscription(subscription);
		deliveryMsg.setOperationType(theRequest.getOperationType());
		deliveryMsg.setTransactionId(theRequest.getTransactionId());
		return deliveryMsg;
	}

	private boolean callHooks(
			ActiveSubscription theActiveSubscription,
			InMemoryMatchResult theInMemoryMatchResult,
			ResourceDeliveryMessage deliveryMsg) {
		// Interceptor call: SUBSCRIPTION_RESOURCE_MATCHED
		HookParams params = new HookParams()
				.add(CanonicalSubscription.class, theActiveSubscription.getSubscription())
				.add(ResourceDeliveryMessage.class, deliveryMsg)
				.add(InMemoryMatchResult.class, theInMemoryMatchResult);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED, params)) {
			ourLog.info(
					"Interceptor has decided to abort processing of subscription {}", theActiveSubscription.getId());
			return false;
		}
		return true;
	}

	private void trySendToDeliveryChannel(
			ResourceDeliveryJsonMessage theWrappedMsg, MessageChannel theDeliveryChannel) {
		try {
			boolean success = theDeliveryChannel.send(theWrappedMsg);
			if (!success) {
				ourLog.warn("Failed to send message to Delivery Channel.");
			}
		} catch (RuntimeException e) {
			if (e.getCause() instanceof PayloadTooLargeException) {
				ourLog.warn("Failed to send message to Delivery Channel because the payload size is larger than broker "
						+ "max message size. Retry is about to be performed without payload.");
				ResourceDeliveryJsonMessage msgPayloadLess = nullOutPayload(theWrappedMsg);
				trySendToDeliveryChannel(msgPayloadLess, theDeliveryChannel);
			} else {
				ourLog.error("Failed to send message to Delivery Channel", e);
				throw new RuntimeException(Msg.code(7) + "Failed to send message to Delivery Channel", e);
			}
		}
	}

	private ResourceDeliveryJsonMessage nullOutPayload(ResourceDeliveryJsonMessage theWrappedMsg) {
		ResourceDeliveryMessage resourceDeliveryMessage = theWrappedMsg.getPayload();
		resourceDeliveryMessage.setPayloadToNull();
		return new ResourceDeliveryJsonMessage(resourceDeliveryMessage);
	}
}
