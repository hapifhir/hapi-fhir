package ca.uhn.fhir.jpa.subscription.resthook;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.*;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionDeliverySubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);

	public SubscriptionDeliveringRestHookSubscriber(IFhirResourceDao<?> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		super(theSubscriptionDao, theChannelType, theSubscriptionInterceptor);
	}

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient) {
		IBaseResource payloadResource = theMsg.getPayload(getContext());

		IClientExecutable<?, ?> operation;
		switch (theMsg.getOperationType()) {
			case CREATE:
				if (payloadResource == null || payloadResource.isEmpty()) {
					if (thePayloadType != null ) {
						operation = theClient.create().resource(payloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				} else {
					if (thePayloadType != null ) {
						operation = theClient.update().resource(payloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				}
				break;
			case UPDATE:
				if (payloadResource == null || payloadResource.isEmpty()) {
					if (thePayloadType != null ) {
						operation = theClient.create().resource(payloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				} else {
					if (thePayloadType != null ) {
						operation = theClient.update().resource(payloadResource);
					} else {
						sendNotification(theMsg);
						return;
					}
				}
				break;
			case DELETE:
				operation = theClient.delete().resourceById(theMsg.getPayloadId(getContext()));
				break;
			default:
				ourLog.warn("Ignoring delivery message of type: {}", theMsg.getOperationType());
				return;
		}

		if (thePayloadType != null) {
			operation.encoded(thePayloadType);
		}

		ourLog.info("Delivering {} rest-hook payload {} for {}", theMsg.getOperationType(), payloadResource.getIdElement().toUnqualified().getValue(), theSubscription.getIdElement(getContext()).toUnqualifiedVersionless().getValue());

		try {
			operation.execute();
		} catch (ResourceNotFoundException e) {
			ourLog.error("Cannot reach "+ theMsg.getSubscription().getEndpointUrl());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException {
			CanonicalSubscription subscription = theMessage.getSubscription();

			// Grab the endpoint from the subscription
			String endpointUrl = subscription.getEndpointUrl();

			// Grab the payload type (encoding mimetype) from the subscription
			String payloadString = subscription.getPayloadString();
			EncodingEnum payloadType = null;
			if(payloadString != null) {
				if (payloadString.contains(";")) {
					payloadString = payloadString.substring(0, payloadString.indexOf(';'));
				}
				payloadString = payloadString.trim();
				payloadType = EncodingEnum.forContentType(payloadString);
			}

			// Create the client request
			getContext().getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
			IGenericClient client = null;
			if (isNotBlank(endpointUrl)) {
				client = getContext().newRestfulGenericClient(endpointUrl);

				// Additional headers specified in the subscription
				List<String> headers = subscription.getHeaders();
				for (String next : headers) {
					if (isNotBlank(next)) {
						client.registerInterceptor(new SimpleRequestHeaderInterceptor(next));
					}
				}
			}

			deliverPayload(theMessage, subscription, payloadType, client);
	}

	/**
	 * Sends a POST notification without a payload
	 * @param theMsg
	 */
	protected void sendNotification(ResourceDeliveryMessage theMsg) {
		FhirContext context= getContext();
		Map<String, List<String>> params = new HashMap();
		List<Header> headers = new ArrayList<>();
		StringBuilder url = new StringBuilder(theMsg.getSubscription().getEndpointUrl());
		IHttpClient client = context.getRestfulClientFactory().getHttpClient(url, params, "", RequestTypeEnum.POST, headers);
		IHttpRequest request = client.createParamRequest(context, params, null);
		try {
			IHttpResponse response = request.execute();
		} catch (IOException e) {
			ourLog.error("Error trying to reach "+ theMsg.getSubscription().getEndpointUrl());
			e.printStackTrace();
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
}
