package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.subscription.cache.SubscriptionRegistry;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

public abstract class BaseSubscriptionDeliverySubscriber extends BaseSubscriptionSubscriber {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliverySubscriber.class);

	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;

	public BaseSubscriptionDeliverySubscriber(Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		super(theChannelType, theSubscriptionInterceptor);
	}

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			ourLog.warn("Unexpected payload type: {}", theMessage.getPayload());
			return;
		}

		String subscriptionId = "(unknown?)";

		try {
			ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();
			subscriptionId = msg.getSubscription().getIdElement(getContext()).getValue();

			CanonicalSubscription updatedSubscription = mySubscriptionRegistry.get(msg.getSubscription().getIdElement(getContext()).getIdPart());
			if (updatedSubscription != null) {
				msg.setSubscription(updatedSubscription);
			}

			if (!subscriptionTypeApplies(msg.getSubscription())) {
				return;
			}

			handleMessage(msg);
		} catch (Exception e) {
			String msg = "Failure handling subscription payload for subscription: " + subscriptionId;
			ourLog.error(msg, e);
			throw new MessagingException(theMessage, msg, e);
		}
	}

	public abstract void handleMessage(ResourceDeliveryMessage theMessage) throws Exception;

}
