package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.*;
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
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
@Interceptor()
public class SubscriptionMatcherInterceptor implements IResourceModifiedConsumer {
	public static final String SUBSCRIPTION_MATCHING_CHANNEL_NAME = "subscription-matching";
	protected SubscribableChannel myMatchingChannel;
	@Autowired
	protected SubscriptionChannelFactory mySubscriptionChannelFactory;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionMatchingSubscriber mySubscriptionMatchingSubscriber;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	public void start() {
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingChannel(SUBSCRIPTION_MATCHING_CHANNEL_NAME);
		}
		myMatchingChannel.subscribe(mySubscriptionMatchingSubscriber);
		ourLog.info("Subscription Matching Subscriber subscribed to Matching Channel {} with name {}", myMatchingChannel.getClass().getName(), SUBSCRIPTION_MATCHING_CHANNEL_NAME);

	}

	@SuppressWarnings("unused")
	@PreDestroy
	public void preDestroy() {

		if (myMatchingChannel != null) {
			myMatchingChannel.unsubscribe(mySubscriptionMatchingSubscriber);
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);
		// Interceptor call: SUBSCRIPTION_RESOURCE_MODIFIED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		if (!myInterceptorBroadcaster.callHooks(Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED, params)) {
			return;
		}

		submitResourceModified(msg);
	}

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMatchingChannel, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(final ResourceModifiedMessage theMsg) {
		/*
		 * We only want to submit the message to the processing queue once the
		 * transaction is committed. We do this in order to make sure that the
		 * data is actually in the DB, in case it's the database matcher.
		 */
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public void afterCommit() {
					sendToProcessingChannel(theMsg);
				}
			});
		} else {
			sendToProcessingChannel(theMsg);
		}
	}

	@VisibleForTesting
	LinkedBlockingQueueSubscribableChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingQueueSubscribableChannel) myMatchingChannel;
	}
}
