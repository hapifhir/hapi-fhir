/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchDeliverer;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.util.Logs;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class SubscriptionTopicMatchingSubscriber implements MessageHandler {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	private final FhirContext myFhirContext;
	@Autowired
	SubscriptionTopicSupport mySubscriptionTopicSupport;
	@Autowired
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;
	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionMatchDeliverer mySubscriptionMatchDeliverer;
	@Autowired
	SubscriptionTopicPayloadBuilder mySubscriptionTopicPayloadBuilder;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public SubscriptionTopicMatchingSubscriber(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();

		// Interceptor call: SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED, params)) {
			return;
		}
		try {
			matchActiveSubscriptionTopicsAndDeliver(msg);
		} finally {
			// Interceptor call: SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED
			myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_TOPIC_AFTER_PERSISTED_RESOURCE_CHECKED, params);
		}
	}

	private void matchActiveSubscriptionTopicsAndDeliver(ResourceModifiedMessage theMsg) {

		Collection<SubscriptionTopic> topics = mySubscriptionTopicRegistry.getAll();
		for (SubscriptionTopic topic : topics) {
			SubscriptionTopicMatcher matcher = new SubscriptionTopicMatcher(mySubscriptionTopicSupport, topic);
			InMemoryMatchResult result = matcher.match(theMsg);
			if (result.matched()) {
				ourLog.info("Matched topic {} to message {}", topic.getUrl(), theMsg);
				deliverToTopicSubscriptions(theMsg, topic, result);
			}
		}
	}

	private void deliverToTopicSubscriptions(ResourceModifiedMessage theMsg, SubscriptionTopic topic, InMemoryMatchResult result) {
		List<ActiveSubscription> topicSubscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(topic.getUrl());
		if (!topicSubscriptions.isEmpty()) {
			IBaseResource matchedResource = theMsg.getNewPayload(myFhirContext);

			for (ActiveSubscription activeSubscription : topicSubscriptions) {
				// WIP STR5 apply subscription filters
				IBaseBundle bundlePayload = mySubscriptionTopicPayloadBuilder.buildPayload(matchedResource, theMsg, activeSubscription, topic);
				// WIP STR5 do we need to add a total?  If so can do that with R5BundleFactory
				bundlePayload.setId(UUID.randomUUID().toString());
				mySubscriptionMatchDeliverer.deliverPayload(bundlePayload, theMsg, activeSubscription, result);
			}
		}
	}
}
