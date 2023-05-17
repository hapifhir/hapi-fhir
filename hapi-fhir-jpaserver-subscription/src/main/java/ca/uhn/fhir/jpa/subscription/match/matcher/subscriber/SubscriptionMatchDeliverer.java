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
package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
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

	public SubscriptionMatchDeliverer(FhirContext theFhirContext, IInterceptorBroadcaster theInterceptorBroadcaster, SubscriptionChannelRegistry theSubscriptionChannelRegistry) {
		myFhirContext = theFhirContext;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
		mySubscriptionChannelRegistry = theSubscriptionChannelRegistry;
	}

	public boolean deliverPayload(IBaseResource thePayload, ResourceModifiedMessage theMsg, ActiveSubscription theActiveSubscription, InMemoryMatchResult theInMemoryMatchResult) {
			EncodingEnum encoding = null;

		CanonicalSubscription subscription = theActiveSubscription.getSubscription();
		String subscriptionId = theActiveSubscription.getId();;

		if (subscription != null && subscription.getPayloadString() != null && !subscription.getPayloadString().isEmpty()) {
			encoding = EncodingEnum.forContentType(subscription.getPayloadString());
		}
		encoding = defaultIfNull(encoding, EncodingEnum.JSON);

		ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
		deliveryMsg.setPartitionId(theMsg.getPartitionId());

		if (thePayload != null) {
			deliveryMsg.setPayload(myFhirContext, thePayload, encoding);
		} else {
			deliveryMsg.setPayloadId(theMsg.getPayloadId(myFhirContext));
		}
		deliveryMsg.setSubscription(subscription);
		deliveryMsg.setOperationType(theMsg.getOperationType());
		deliveryMsg.setTransactionId(theMsg.getTransactionId());
		deliveryMsg.copyAdditionalPropertiesFrom(theMsg);

		// Interceptor call: SUBSCRIPTION_RESOURCE_MATCHED
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, theActiveSubscription.getSubscription())
			.add(ResourceDeliveryMessage.class, deliveryMsg)
			.add(InMemoryMatchResult.class, theInMemoryMatchResult);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_MATCHED, params)) {
			ourLog.info("Interceptor has decided to abort processing of subscription {}", subscriptionId);
			return false;
		}

		return sendToDeliveryChannel(theActiveSubscription, deliveryMsg);
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
}
