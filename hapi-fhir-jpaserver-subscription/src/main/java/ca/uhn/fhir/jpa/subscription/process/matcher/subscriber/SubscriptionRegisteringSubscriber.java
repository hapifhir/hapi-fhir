package ca.uhn.fhir.jpa.subscription.process.matcher.subscriber;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 * <p>
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
public class SubscriptionRegisteringSubscriber extends BaseSubscriberForSubscriptionResources implements MessageHandler {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionRegisteringSubscriber.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	/**
	 * Constructor
	 */
	public SubscriptionRegisteringSubscriber() {
		super();
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Received message of unexpected type on matching channel: {}", theMessage);
			return;
		}

		ResourceModifiedMessage payload = ((ResourceModifiedJsonMessage) theMessage).getPayload();

		if (!isSubscription(payload)) {
			return;
		}

		switch (payload.getOperationType()) {
			case DELETE:
				mySubscriptionRegistry.unregisterSubscription(payload.getId(myFhirContext).getIdPart());
				break;
			case CREATE:
			case UPDATE:
				mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(payload.getNewPayload(myFhirContext));
				break;
			case MANUALLY_TRIGGERED:
			default:
				break;
		}

	}

}
