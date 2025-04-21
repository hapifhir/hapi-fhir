/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.topic.filter.InMemoryTopicFilterMatcher;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SubscriptionTopicMatchingListener implements IMessageListener<ResourceModifiedMessage> {
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

	public SubscriptionTopicMatchingListener(FhirContext theFhirContext, MemoryCacheService memoryCacheService) {
		myFhirContext = theFhirContext;
		this.myMemoryCacheService = memoryCacheService;
	}

	public Class<ResourceModifiedMessage> getPayloadType() {
		return ResourceModifiedMessage.class;
	}

	@Override
	public void handleMessage(@Nonnull IMessage<ResourceModifiedMessage> theMessage) {
		ResourceModifiedMessage payload = theMessage.getPayload();

		if (payload.getResource(myFhirContext) == null) {
			// inflate the message and ignore any resource that cannot be found.
			Optional<ResourceModifiedMessage> inflatedPayload =
					myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessageOrNull(payload);
			if (inflatedPayload.isEmpty()) {
				return;
			}
			payload = inflatedPayload.get();
		}

		// Interceptor call: SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED
		HookParams params = new HookParams().add(ResourceModifiedMessage.class, payload);
		if (!myInterceptorBroadcaster.callHooks(
				Pointcut.SUBSCRIPTION_TOPIC_BEFORE_PERSISTED_RESOURCE_CHECKED, params)) {
			return;
		}
		try {
			matchActiveSubscriptionTopicsAndDeliver(payload);
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
			ResourceModifiedMessage thePayload,
			SubscriptionTopic theSubscriptionTopic,
			InMemoryMatchResult theInMemoryMatchResult) {
		String topicUrl = theSubscriptionTopic.getUrl();
		IBaseResource matchedResource = thePayload.getNewResource(myFhirContext);
		List<IBaseResource> matchedResourceList = Collections.singletonList(matchedResource);
		RestOperationTypeEnum restOperationType = thePayload.getOperationType().asRestOperationType();

		return mySubscriptionTopicDispatcher.dispatch(new SubscriptionTopicDispatchRequest(
				topicUrl,
				matchedResourceList,
				myInMemoryTopicFilterMatcher,
				restOperationType,
				theInMemoryMatchResult,
				thePayload.getPartitionId(),
				thePayload.getTransactionId()));
	}
}
