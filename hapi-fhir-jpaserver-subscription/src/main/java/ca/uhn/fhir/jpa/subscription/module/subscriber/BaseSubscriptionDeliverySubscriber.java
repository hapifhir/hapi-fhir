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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

public abstract class BaseSubscriptionDeliverySubscriber implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSubscriptionDeliverySubscriber.class);

	@Autowired
	protected FhirContext myFhirContext;
	@Autowired
	protected SubscriptionRegistry mySubscriptionRegistry;

	@Override
	public void handleMessage(Message theMessage) throws MessagingException {
		if (!(theMessage.getPayload() instanceof ResourceDeliveryMessage)) {
			ourLog.warn("Unexpected payload type: {}", theMessage.getPayload());
			return;
		}

		String subscriptionId = "(unknown?)";

		try {
			ResourceDeliveryMessage msg = (ResourceDeliveryMessage) theMessage.getPayload();
			subscriptionId = msg.getSubscription().getIdElement(myFhirContext).getValue();

			ActiveSubscription updatedSubscription = mySubscriptionRegistry.get(msg.getSubscription().getIdElement(myFhirContext).getIdPart());
			if (updatedSubscription != null) {
				msg.setSubscription(updatedSubscription.getSubscription());
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
