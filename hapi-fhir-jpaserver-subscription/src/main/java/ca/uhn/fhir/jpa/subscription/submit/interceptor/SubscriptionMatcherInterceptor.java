package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

@Interceptor
public class SubscriptionMatcherInterceptor implements IResourceModifiedConsumer {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	private volatile MessageChannel myMatchingChannel;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@EventListener(classes = {ContextRefreshedEvent.class})
	public void startIfNeeded() {
		if (myDaoConfig.getSupportedSubscriptionTypes().isEmpty()) {
			ourLog.debug("Subscriptions are disabled on this server.  Skipping {} channel creation.", SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			return;
		}
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingSendingChannel(SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME, null);
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {
		startIfNeeded();
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {
		startIfNeeded();
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		startIfNeeded();
		if (!myDaoConfig.isTriggerSubscriptionsForNonVersioningChanges()) {
			if (theOldResource != null && theNewResource != null) {
				String oldVersion = theOldResource.getIdElement().getVersionIdPart();
				String newVersion = theNewResource.getIdElement().getVersionIdPart();
				if (isNotBlank(oldVersion) && isNotBlank(newVersion) && oldVersion.equals(newVersion)) {
					return;
				}
			}
		}

		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE, theRequest);
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {
		// Even though the resource is being written, the subscription will be interacting with it by effectively "reading" it so we set the RequestPartitionId as a read request
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(theRequest, theNewResource.getIdElement().getResourceType(), theNewResource.getIdElement());
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType, theRequest, requestPartitionId);

		// Interceptor call: SUBSCRIPTION_RESOURCE_MODIFIED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		boolean outcome = CompositeInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED, params);
		if (!outcome) {
			return;
		}

		submitResourceModified(msg);
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

	protected void sendToProcessingChannel(final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMatchingChannel, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	@VisibleForTesting
	public LinkedBlockingChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingChannel) myMatchingChannel;
	}
}
