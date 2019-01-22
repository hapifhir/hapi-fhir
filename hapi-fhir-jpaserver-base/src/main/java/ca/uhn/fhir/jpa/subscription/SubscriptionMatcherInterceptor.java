package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.LinkedBlockingQueueSubscribableChannel;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.module.subscriber.SubscriptionMatchingSubscriber;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

@Component
@Lazy
@Interceptor(manualRegistration = true)
public class SubscriptionMatcherInterceptor implements IResourceModifiedConsumer {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);

	private static final String SUBSCRIPTION_MATCHING_CHANNEL_NAME = "subscription-matching";
	static final String SUBSCRIPTION_STATUS = "Subscription.status";
	static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	private SubscribableChannel myProcessingChannel;

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	public void start() {
		if (myProcessingChannel == null) {
			myProcessingChannel = mySubscriptionChannelFactory.newMatchingChannel(SUBSCRIPTION_MATCHING_CHANNEL_NAME);
		}
		myProcessingChannel.subscribe(mySubscriptionMatchingSubscriber);
		ourLog.info("Subscription Matching Subscriber subscribed to Matching Channel {} with name {}", myProcessingChannel.getClass().getName(), SUBSCRIPTION_MATCHING_CHANNEL_NAME);

	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {

		if (myProcessingChannel != null) {
			myProcessingChannel.unsubscribe(mySubscriptionMatchingSubscriber);
		}
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);
		submitResourceModified(msg);
	}

	private void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myProcessingChannel, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myProcessingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		sendToProcessingChannel(theMsg);
	}

	@VisibleForTesting
	LinkedBlockingQueueSubscribableChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingQueueSubscribableChannel) myProcessingChannel;
	}
}
