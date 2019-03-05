package ca.uhn.fhir.jpa.subscription.module.subscriber;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.interceptor.api.IInterceptorRegistry;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
@Scope("prototype")
public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionDeliverySubscriber {
	@Autowired
	IResourceRetriever myResourceRetriever;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient) {
		IBaseResource payloadResource = getAndMassagePayload(theMsg, theSubscription);
		if (payloadResource == null) {
			return;
		}

		doDelivery(theMsg, theSubscription, thePayloadType, theClient, payloadResource);
	}

	protected void doDelivery(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient, IBaseResource thePayloadResource) {
		IClientExecutable<?, ?> operation;
		switch (theMsg.getOperationType()) {
			case CREATE:
			case UPDATE:
				if (thePayloadResource == null || thePayloadResource.isEmpty()) {
					if (thePayloadType != null) {
						operation = theClient.create().resource(thePayloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				} else {
					if (thePayloadType != null) {
						operation = theClient.update().resource(thePayloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				}
				break;
			case DELETE:
				operation = theClient.delete().resourceById(theMsg.getPayloadId(myFhirContext));
				break;
			default:
				ourLog.warn("Ignoring delivery message of type: {}", theMsg.getOperationType());
				return;
		}

		if (thePayloadType != null) {
			operation.encoded(thePayloadType);
		}

		String payloadId = null;
		if (thePayloadResource != null) {
			payloadId = thePayloadResource.getIdElement().toUnqualified().getValue();
		}
		ourLog.info("Delivering {} rest-hook payload {} for {}", theMsg.getOperationType(), payloadId, theSubscription.getIdElement(myFhirContext).toUnqualifiedVersionless().getValue());

		try {
			operation.execute();
		} catch (ResourceNotFoundException e) {
			ourLog.error("Cannot reach {} ", theMsg.getSubscription().getEndpointUrl());
			ourLog.error("Exception: ", e);
			throw e;
		}
	}

	protected IBaseResource getAndMassagePayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription) {
		IBaseResource payloadResource = theMsg.getPayload(myFhirContext);

		if (payloadResource == null || theSubscription.getRestHookDetails().isDeliverLatestVersion()) {
			IIdType payloadId = theMsg.getPayloadId(myFhirContext);
			try {
				payloadResource = myResourceRetriever.getResource(payloadId.toVersionless());
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
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException {
		CanonicalSubscription subscription = theMessage.getSubscription();

		// Interceptor call: SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY, theMessage, subscription)) {
			return;
		}

		// Grab the endpoint from the subscription
		String endpointUrl = subscription.getEndpointUrl();

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
			if (payloadString.contains(";")) {
				payloadString = payloadString.substring(0, payloadString.indexOf(';'));
			}
			payloadString = payloadString.trim();
			payloadType = EncodingEnum.forContentType(payloadString);
		}

		// Create the client request
		myFhirContext.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = null;
		if (isNotBlank(endpointUrl)) {
			client = myFhirContext.newRestfulGenericClient(endpointUrl);

			// Additional headers specified in the subscription
			List<String> headers = subscription.getHeaders();
			for (String next : headers) {
				if (isNotBlank(next)) {
					client.registerInterceptor(new SimpleRequestHeaderInterceptor(next));
				}
			}
		}

		deliverPayload(theMessage, subscription, payloadType, client);

		// Interceptor call: SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, theMessage, subscription)) {
			//noinspection UnnecessaryReturnStatement
			return;
		}

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
