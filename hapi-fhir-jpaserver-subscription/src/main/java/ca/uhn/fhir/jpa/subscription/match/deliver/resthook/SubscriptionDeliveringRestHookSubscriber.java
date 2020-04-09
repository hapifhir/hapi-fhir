package ca.uhn.fhir.jpa.subscription.match.deliver.resthook;

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
import ca.uhn.fhir.jpa.subscription.match.deliver.BaseSubscriptionDeliverySubscriber;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Scope("prototype")
public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionDeliverySubscriber {

	@Autowired
	private DaoRegistry myDaoRegistry;

	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);


	/**
	 * Constructor
	 */
	public SubscriptionDeliveringRestHookSubscriber() {
		super();
	}

	protected void deliverPayload(ResourceDeliveryMessage theMsg, CanonicalSubscription theSubscription, EncodingEnum thePayloadType, IGenericClient theClient) {
		IBaseResource payloadResource = getAndMassagePayload(theMsg, theSubscription);

		// Regardless of whether we have a payload, the rest-hook should be sent.
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
	public void handleMessage(ResourceDeliveryMessage theMessage) throws MessagingException {
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

		// Grab the payload type (encoding mimetype) from the subscription
		String payloadString = subscription.getPayloadString();
		EncodingEnum payloadType = null;
		if (payloadString != null) {
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
		params = new HookParams()
			.add(CanonicalSubscription.class, subscription)
			.add(ResourceDeliveryMessage.class, theMessage);
		if (!getInterceptorBroadcaster().callHooks(Pointcut.SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY, params)) {
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
