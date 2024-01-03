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
package ca.uhn.fhir.jpa.subscription.match.deliver.message;

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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessagingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

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

	protected void doDelivery(
			ResourceDeliveryMessage theSourceMessage,
			CanonicalSubscription theSubscription,
			IChannelProducer theChannelProducer,
			ResourceModifiedJsonMessage theWrappedMessageToSend) {
		String payloadId = theSourceMessage.getPayloadId();
		if (isNotBlank(theSubscription.getPayloadSearchCriteria())) {
			IBaseResource payloadResource = createDeliveryBundleForPayloadSearchCriteria(
					theSubscription, theWrappedMessageToSend.getPayload().getPayload(myFhirContext));
			ResourceModifiedJsonMessage newWrappedMessageToSend =
					convertDeliveryMessageToResourceModifiedJsonMessage(theSourceMessage, payloadResource);
			theWrappedMessageToSend.setPayload(newWrappedMessageToSend.getPayload());
			payloadId =
					payloadResource.getIdElement().toUnqualifiedVersionless().getValue();
		}
		theChannelProducer.send(theWrappedMessageToSend);
		ourLog.debug(
				"Delivering {} message payload {} for {}",
				theSourceMessage.getOperationType(),
				payloadId,
				theSubscription
						.getIdElement(myFhirContext)
						.toUnqualifiedVersionless()
						.getValue());
	}

	private ResourceModifiedJsonMessage convertDeliveryMessageToResourceModifiedJsonMessage(
			ResourceDeliveryMessage theMsg, IBaseResource thePayloadResource) {
		ResourceModifiedMessage payload =
				new ResourceModifiedMessage(myFhirContext, thePayloadResource, theMsg.getOperationType());
		payload.setMessageKey(theMsg.getMessageKeyOrDefault());
		payload.setTransactionId(theMsg.getTransactionId());
		payload.setPartitionId(theMsg.getRequestPartitionId());
		return new ResourceModifiedJsonMessage(payload);
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException, URISyntaxException {
		CanonicalSubscription subscription = theMessage.getSubscription();
		IBaseResource payloadResource = theMessage.getPayload(myFhirContext);
		if (payloadResource == null) {
			Optional<ResourceModifiedMessage> inflatedMsg =
					inflateResourceModifiedMessageFromDeliveryMessage(theMessage);
			if (inflatedMsg.isEmpty()) {
				return;
			}
			payloadResource = inflatedMsg.get().getPayload(myFhirContext);
		}

		ResourceModifiedJsonMessage messageWrapperToSend =
				convertDeliveryMessageToResourceModifiedJsonMessage(theMessage, payloadResource);

		// Interceptor call: SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY
		HookParams params = new HookParams()
				.add(CanonicalSubscription.class, subscription)
				.add(ResourceDeliveryMessage.class, theMessage)
				.add(ResourceModifiedJsonMessage.class, messageWrapperToSend);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_BEFORE_MESSAGE_DELIVERY, params)) {
			return;
		}
		// Grab the endpoint from the subscription
		String endpointUrl = subscription.getEndpointUrl();

		String queueName = extractQueueNameFromEndpoint(endpointUrl);

		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		channelSettings.setQualifyChannelName(false);

		IChannelProducer channelProducer =
				myChannelFactory.getOrCreateProducer(queueName, ResourceModifiedJsonMessage.class, channelSettings);

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
			payloadType = EncodingEnum.forContentType(payloadString);
		}

		if (payloadType != EncodingEnum.JSON) {
			throw new UnsupportedOperationException(
					Msg.code(4) + "Only JSON payload type is currently supported for Message Subscriptions");
		}

		doDelivery(theMessage, subscription, channelProducer, messageWrapperToSend);

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
