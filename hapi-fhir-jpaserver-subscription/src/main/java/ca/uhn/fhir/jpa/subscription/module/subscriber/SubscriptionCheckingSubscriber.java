package ca.uhn.fhir.jpa.subscription.module.subscriber;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.matcher.ISubscriptionMatcher;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

@Service
public class SubscriptionCheckingSubscriber implements MessageHandler {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionCheckingSubscriber.class);

	@Autowired
	private ISubscriptionMatcher mySubscriptionMatcher;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	@Override
	public void handleMessage(Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		switch (msg.getOperationType()) {
			case CREATE:
			case UPDATE:
			case MANUALLY_TRIGGERED:
				break;
			case DELETE:
			default:
				ourLog.trace("Not processing modified message for {}", msg.getOperationType());
				// ignore anything else
				return;
		}

		IIdType id = msg.getId(myFhirContext);
		String resourceType = id.getResourceType();

		Collection<ActiveSubscription> subscriptions = mySubscriptionRegistry.getAll();

		ourLog.trace("Testing {} subscriptions for applicability", subscriptions.size());

		for (ActiveSubscription nextActiveSubscription : subscriptions) {

			String nextSubscriptionId = nextActiveSubscription.getIdElement(myFhirContext).toUnqualifiedVersionless().getValue();
			String nextCriteriaString = nextActiveSubscription.getCriteriaString();

			if (isNotBlank(msg.getSubscriptionId())) {
				if (!msg.getSubscriptionId().equals(nextSubscriptionId)) {
					ourLog.debug("Ignoring subscription {} because it is not {}", nextSubscriptionId, msg.getSubscriptionId());
					continue;
				}
			}

			if (StringUtils.isBlank(nextCriteriaString)) {
				continue;
			}

			// see if the criteria matches the created object
			ourLog.trace("Checking subscription {} for {} with criteria {}", nextSubscriptionId, resourceType, nextCriteriaString);
			String criteriaResource = nextCriteriaString;
			int index = criteriaResource.indexOf("?");
			if (index != -1) {
				criteriaResource = criteriaResource.substring(0, criteriaResource.indexOf("?"));
			}

			if (resourceType != null && nextCriteriaString != null && !criteriaResource.equals(resourceType)) {
				ourLog.trace("Skipping subscription search for {} because it does not match the criteria {}", resourceType, nextCriteriaString);
				continue;
			}

			if (!mySubscriptionMatcher.match(nextCriteriaString, msg).matched()) {
				continue;
			}

			ourLog.debug("Found match: queueing rest-hook notification for resource: {}", id.toUnqualifiedVersionless().getValue());

			ResourceDeliveryMessage deliveryMsg = new ResourceDeliveryMessage();
			deliveryMsg.setPayload(myFhirContext, msg.getNewPayload(myFhirContext));
			deliveryMsg.setSubscription(nextActiveSubscription.getSubscription());
			deliveryMsg.setOperationType(msg.getOperationType());
			deliveryMsg.setPayloadId(msg.getId(myFhirContext));

			ResourceDeliveryJsonMessage wrappedMsg = new ResourceDeliveryJsonMessage(deliveryMsg);
			MessageChannel deliveryChannel = nextActiveSubscription.getSubscribableChannel();
			if (deliveryChannel != null) {
				deliveryChannel.send(wrappedMsg);
			} else {
				ourLog.warn("Do not have deliovery channel for subscription {}", nextActiveSubscription.getIdElement(myFhirContext));
			}
		}
	}
}
