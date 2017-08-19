package ca.uhn.fhir.jpa.subscription;

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

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.gclient.IClientExecutable;
import org.apache.commons.lang3.ObjectUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SubscriptionDeliveringRestHookSubscriber extends BaseSubscriptionSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveringRestHookSubscriber.class);

	public SubscriptionDeliveringRestHookSubscriber(IFhirResourceDao theSubscriptionDao, ConcurrentHashMap<String, IBaseResource> theIdToSubscription, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		super(theSubscriptionDao, theIdToSubscription, theChannelType, theSubscriptionInterceptor);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			return;
		}

		ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();

		if (!subscriptionTypeApplies(getContext(), msg.getSubscription())) {
			return;
		}

		IBaseResource subscription = msg.getSubscription();


		// Grab the endpoint from the subscription
		IPrimitiveType<?> endpoint = getContext().newTerser().getSingleValueOrNull(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_ENDPOINT, IPrimitiveType.class);
		String endpointUrl = endpoint.getValueAsString();

		// Grab the payload type (encoding mimetype) from the subscription
		IPrimitiveType<?> payload = getContext().newTerser().getSingleValueOrNull(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_PAYLOAD, IPrimitiveType.class);
		String payloadString = payload.getValueAsString();
		if (payloadString.contains(";")) {
			payloadString = payloadString.substring(0, payloadString.indexOf(';'));
		}
		payloadString = payloadString.trim();
		EncodingEnum payloadType = EncodingEnum.forContentType(payloadString);
		payloadType = ObjectUtils.defaultIfNull(payloadType, EncodingEnum.XML);

		// Create the client request
		getContext().getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = getContext().newRestfulGenericClient(endpointUrl);

		// Additional headers specified in the subscription
		List<IPrimitiveType> headers = getContext().newTerser().getValues(subscription, BaseSubscriptionInterceptor.SUBSCRIPTION_HEADER, IPrimitiveType.class);
		for (IPrimitiveType next : headers) {
			if (isNotBlank(next.getValueAsString())) {
				client.registerInterceptor(new SimpleRequestHeaderInterceptor(next.getValueAsString()));
			}
		}

		msg = massage(msg);

		IBaseResource payloadResource = msg.getPayoad();
		IClientExecutable<?, ?> operation;
		switch (msg.getOperationType()) {
			case CREATE:
				operation = client.create().resource(payloadResource);
				break;
			case UPDATE:
				operation = client.update().resource(payloadResource);
				break;
			case DELETE:
				operation = client.delete().resourceById(msg.getPayloadId());
				break;
			default:
				ourLog.warn("Ignoring delivery message of type: {}", msg.getOperationType());
				return;
		}

		operation.encoded(payloadType);

		ourLog.info("Delivering {} rest-hook payload {} for {}", msg.getOperationType(), payloadResource.getIdElement().toUnqualified().getValue(), subscription.getIdElement().toUnqualifiedVersionless().getValue());

		operation.execute();

	}

	/**
	 * Subclasses may override
	 */
	protected ResourceDeliveryMessage massage(ResourceDeliveryMessage theMsg) {
		return theMsg;
	}
}
