package ca.uhn.fhir.jpa.subscription.match.deliver.message;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.match.deliver.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionValidatingInterceptor;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

// FIXME KHS subs fair amount of copy/paste from rest-hook
@Scope("prototype")
public class SubscriptionDeliveringMessageSubscriber extends BaseSubscriptionDeliverySubscriber {

	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IChannelFactory myChannelFactory;
	@Autowired
	private SubscriptionValidatingInterceptor mySubscriptionValidatingInterceptor;

	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringMessageSubscriber.class);

	/**
	 * Constructor
	 */
	public SubscriptionDeliveringMessageSubscriber() {
		super();
	}

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, IChannelProducer theChannelProducer) {
		IBaseResource payloadResource = getAndMassagePayload(theMsg, theSubscription);

		// Regardless of whether we have a payload, the rest-hook should be sent.
		doDelivery(theMsg, theSubscription, theChannelProducer, payloadResource);
	}

	protected void doDelivery(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, IChannelProducer theChannelProducer, IBaseResource thePayloadResource) {
		ResourceModifiedMessage payload = new ResourceModifiedMessage(myFhirContext, thePayloadResource, theMsg.getOperationType());
		ResourceModifiedJsonMessage message = new ResourceModifiedJsonMessage(payload);
		theChannelProducer.send(message);
		ourLog.info("Delivering {} message payload {} for {}", theMsg.getOperationType(), theMsg.getPayloadId(), theSubscription.getIdElement(myFhirContext).toUnqualifiedVersionless().getValue());
	}

	public IBaseResource getResource(IIdType payloadId) throws ResourceGoneException {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(payloadId.getResourceType());
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceDef.getImplementingClass());
		return dao.read(payloadId.toVersionless());
	}

	protected IBaseResource getAndMassagePayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription) {
		IBaseResource payloadResource = theMsg.getPayload(myFhirContext);

		if (payloadResource == null || theSubscription.getRestHookDetails().isDeliverLatestVersion()) {
			IIdType payloadId = theMsg.getPayloadId(myFhirContext);

			try {
				if (payloadId != null) {
					payloadResource = getResource(payloadId.toVersionless());
				} else {
					return null;
				}
			} catch (ResourceGoneException e) {
				ourLog.warn("Resource {} is deleted, not going to deliver for subscription {}", payloadId.toVersionless(), theSubscription.getIdElement(myFhirContext));
				return null;
			}
		}

		IIdType resourceId = payloadResource.getIdElement();
		if (theSubscription.getRestHookDetails().isStripVersionId()) {
			resourceId = resourceId.toVersionless();
			payloadResource.setId(resourceId);
		}
		return payloadResource;
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException, URISyntaxException {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// Interceptor call: SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY
		HookParams params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, params)) {
			return;
		}

		// Grab the endpoint from the subscription
		String endpointUrl = subscription.getEndpointUrl();

		String queueName = extractQueueNameFromEndpoint(endpointUrl);

		ChannelConsumerSettings config = new ChannelConsumerSettings();
		config.setConcurrentConsumers(SubscriptionConstants.DELIVERY_CHANNEL_CONCURRENT_CONSUMERS);
		IChannelProducer channelProducer = myChannelFactory.getOrCreateProducer(queueName, ResourceModifiedJsonMessage.class, config);

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
			payloadType = EncodingEnum.forContentType(payloadString);
		}

		if (payloadType != EncodingEnum.JSON) {
			throw new UnsupportedOperationException("Only JSON payload type is currently supported for Message Subscriptions");
		}

		deliverPayload(theMessage, subscription, channelProducer);

		// Interceptor call: SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY
		params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, params)) {
			//noinspection UnnecessaryReturnStatement
			return;
		}

	}

	private String extractQueueNameFromEndpoint(String theEndpointUrl) throws URISyntaxException {
		mySubscriptionValidatingInterceptor.validateMessageSubscriptionEndpoint(theEndpointUrl);
		URI uri = new URI(theEndpointUrl);
		String jmsPath = uri.getSchemeSpecificPart();
		return jmsPath.substring("queue:".length());
	}

	/**
	 * Sends a POST notification without a payload
	 */
	protected void sendNotification(ResourceDeliveryMessage theMsg) {
		Map<String, List<String>> params = new HashMap<>();
		List<Header> headers = new ArrayList<>();
		if (theMsg.getSubscription().getHeaders() != null) {
			theMsg.getSubscription().getHeaders().stream().filter(Objects::nonNull).forEach(h -> {
				final int sep = h.indexOf(':');
				if (sep > 0) {
					final String name = h.substring(0, sep);
					final String value = h.substring(sep + 1);
					if (StringUtils.isNotBlank(name)) {
						headers.add(new Header(name.trim(), value.trim()));
					}
				}
			});
		}

		StringBuilder url = new StringBuilder(theMsg.getSubscription().getEndpointUrl());
		IHttpClient client = myFhirContext.getRestfulClientFactory().getHttpClient(url, params, "", RequestTypeEnum.POST, headers);
		IHttpRequest request = client.createParamRequest(myFhirContext, params, null);
		try {
			IHttpResponse response = request.execute();
			// close connection in order to return a possible cached connection to the connection pool
			response.close();
		} catch (IOException e) {
			ourLog.error("Error trying to reach " + theMsg.getSubscription().getEndpointUrl());
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
}
