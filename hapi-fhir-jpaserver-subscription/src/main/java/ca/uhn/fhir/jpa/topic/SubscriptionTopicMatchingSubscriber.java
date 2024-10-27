/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.topic.filter.InMemoryTopicFilterMatcher;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SubscriptionTopicMatchingSubscriber implements MessageHandler {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	private final FhirContext myFhirContext;

	@Autowired
	SubscriptionTopicSupport mySubscriptionTopicSupport;

	@Autowired
	SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private SubscriptionTopicDispatcher mySubscriptionTopicDispatcher;

	@Autowired
	private InMemoryTopicFilterMatcher myInMemoryTopicFilterMatcher;

	@Autowired
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	private MemoryCacheService myMemoryCacheService;

	public SubscriptionTopicMatchingSubscriber(FhirContext theFhirContext, MemoryCacheService memoryCacheService) {
		myFhirContext = theFhirContext;
		this.myMemoryCacheService = memoryCacheService;
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		ourLog.trace("Handling resource modified message: {}", theMessage);

		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Unexpected message payload type: {}", theMessage);
			return;
		}

		ResourceModifiedMessage msg = ((ResourceModifiedJsonMessage) theMessage).getPayload();

		if (msg.getPayload(myFhirContext) == null) {
			// inflate the message and ignore any resource that cannot be found.
			Optional<ResourceModifiedMessage> inflatedMsg =
					myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessageOrNull(msg);
			if (inflatedMsg.isEmpty()) {
				return;
			}
			msg = inflatedMsg.get();
		}

		// Interceptor call: SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED
		HookParams params = new HookParams().add(ResourceModifiedMessage.class, msg);
		if (!myInterceptorBroadcaster.callHooks(
				Pointcut.SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED, params)) {
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
			SubscriptionTopicMatcher matcher =
					new SubscriptionTopicMatcher(mySubscriptionTopicSupport, topic, myMemoryCacheService);
			InMemoryMatchResult result = matcher.match(theMsg);
			if (result.matched()) {
				int deliveries = deliverToTopicSubscriptions(theMsg, topic, result);
				ourLog.info(
						"Matched topic {} to message {}.  Notifications sent to {} subscriptions for delivery.",
						topic.getUrl(),
						theMsg,
						deliveries);
			}
		}
	}

	private int deliverToTopicSubscriptions(
			ResourceModifiedMessage theMsg,
			SubscriptionTopic theSubscriptionTopic,
			InMemoryMatchResult theInMemoryMatchResult) {
		String topicUrl = theSubscriptionTopic.getUrl();
		IBaseResource matchedResource = theMsg.getNewPayload(myFhirContext);
		List<IBaseResource> matchedResourceList = Collections.singletonList(matchedResource);
		RestOperationTypeEnum restOperationType = theMsg.getOperationType().asRestOperationType();

		return mySubscriptionTopicDispatcher.dispatch(new SubscriptionTopicDispatchRequest(
				topicUrl,
				matchedResourceList,
				myInMemoryTopicFilterMatcher,
				restOperationType,
				theInMemoryMatchResult,
				theMsg.getPartitionId(),
				theMsg.getTransactionId()));
	}
}
