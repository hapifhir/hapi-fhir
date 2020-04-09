package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.*;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

@Interceptor
// FIXME KHS move and rename
public class SubscriptionMatcherInterceptor implements IResourceModifiedConsumer {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Autowired
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	private volatile Map<String, MessageChannel> myMessageChannels = new HashMap<>();
	private volatile Map<String, Set<String>> mySupportedResourceTypes = new HashMap<>();

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	public void addChannel(String channelName) {
		if (myMessageChannels.get(channelName) == null) {
			myMessageChannels.put(channelName, mySubscriptionChannelFactory.newMatchingSendingChannel(channelName, null));
		}
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE, theRequest);
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType, RequestDetails theRequest) {
		ResourceModifiedMessage msg = new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType);

		// Interceptor call: SUBSCRIPTION_RESOURCE_MODIFIED
		HookParams params = new HookParams()
			.add(ResourceModifiedMessage.class, msg);
		boolean outcome = JpaInterceptorBroadcaster.doCallHooks(myInterceptorBroadcaster, theRequest, Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED, params);
		if (!outcome) {
			return;
		}

		for (Map.Entry<String, MessageChannel> entry : myMessageChannels.entrySet()) {
			try {
				if (resourceTypeSupported(entry.getKey(), theNewResource)) {
					submitResourceModified(entry.getValue(), msg);
				}
			} catch (Exception e) {
				ourLog.error("Failed to send {} to channel {}: {}", msg.getPayloadId(), entry.getKey(), e.getMessage());
				ourLog.error("Send Exception:", e);
			}
		}
	}

	private boolean resourceTypeSupported(String theChannelName, IBaseResource theNewResource) {
		Set<String> supportedResourceTypes = mySupportedResourceTypes.get(theChannelName);
		if (supportedResourceTypes != null) {
			String resourceType = myFhirContext.getResourceDefinition(theNewResource).getName();
			return supportedResourceTypes.contains(resourceType);
		}
		return true;
	}

	// FIXME KHS move this
	public void submitResourceModified(ResourceModifiedMessage theMsg) {
		submitResourceModified(myMessageChannels.get(SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME), theMsg);
	}

	/**
	 * This is an internal API - Use with caution!
	 */
	@Override
	public void submitResourceModified(MessageChannel theChannel, final ResourceModifiedMessage theMsg) {
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
					sendToProcessingChannel(theChannel, theMsg);
				}
			});
		} else {
			sendToProcessingChannel(theChannel, theMsg);
		}
	}

	protected void sendToProcessingChannel(MessageChannel theChannel, final ResourceModifiedMessage theMessage) {
		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMessageChannels, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");

		theChannel.send(new ResourceModifiedJsonMessage(theMessage));
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}

	@VisibleForTesting
	public LinkedBlockingChannel getProcessingChannelForUnitTest(String theChannelName) {
		return (LinkedBlockingChannel) myMessageChannels.get(theChannelName);
	}

	public void addChannel(String theChannelName, Set<String> theResourceTypes) {
		addChannel(theChannelName);
		mySupportedResourceTypes.put(theChannelName, theResourceTypes);
	}
}
