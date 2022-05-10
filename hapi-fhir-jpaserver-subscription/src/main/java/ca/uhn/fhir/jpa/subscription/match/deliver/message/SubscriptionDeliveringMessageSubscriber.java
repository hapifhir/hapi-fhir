package ca.uhn.fhir.jpa.subscription.match.deliver.message;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.match.deliver.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessagingException;

import java.net.URI;
import java.net.URISyntaxException;

@Scope("prototype")
public class SubscriptionDeliveringMessageSubscriber extends BaseSubscriptionDeliverySubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringMessageSubscriber.class);

	private final IChannelFactory myChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionDeliveringMessageSubscriber(IChannelFactory theChannelFactory) {
		super();
		myChannelFactory = theChannelFactory;
	}

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, IChannelProducer theChannelProducer) {
		IBaseResource payloadResource = theMsg.getPayload(myFhirContext);

		// Regardless of whether we have a payload, the message should be sent.
		doDelivery(theMsg, theSubscription, theChannelProducer, payloadResource);
	}

	protected void doDelivery(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, IChannelProducer theChannelProducer, IBaseResource thePayloadResource) {
		ResourceModifiedMessage payload = new ResourceModifiedMessage(myFhirContext, thePayloadResource, theMsg.getOperationType());
		payload.setMessageKey(theMsg.getMessageKeyOrNull());
		payload.setTransactionId(theMsg.getTransactionId());
		payload.setPartitionId(theMsg.getRequestPartitionId());
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(payload);
		theChannelProducer.send(message);
		ourLog.debug("Delivering {} message payload {} for {}", theMsg.getOperationType(), theMsg.getPayloadId(), theSubscription.getIdElement(myFhirContext).toUnqualifiedVersionless().getValue());
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException, URISyntaxException {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// Interceptor call: SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY, params)) {
			return;
		}
		// Grab the endpoint from the subscription
		String endpointUrl = subscription.getEndpointUrl();

		String queueName = extractQueueNameFromEndpoint(endpointUrl);

		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		channelSettings.setQualifyChannelName(false);

		IChannelProducer channelProducer = myChannelFactory.getOrCreateProducer(queueName, ResourceModifiedJsonMessage.class, channelSettings);

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
			payloadType = EncodingEnum.forContentType(payloadString);
		}

		if (payloadType != EncodingEnum.JSON) {
			throw new UnsupportedOperationException(Msg.code(4) + "Only JSON payload type is currently supported for Message Subscriptions");
		}

		deliverPayload(theMessage, subscription, channelProducer);

		// Interceptor call: SUBSCRIPTION_AFTER_MESSAGE_DELIVERY
		params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_AFTER_MESSAGE_DELIVERY, params)) {
			//noinspection UnnecessaryReturnStatement
			return;
		}
	}

	private String extractQueueNameFromEndpoint(String theEndpointUrl) throws URISyntaxException {
		URI uri = new URI(theEndpointUrl);
		return uri.getSchemeSpecificPart();
	}
}
