package ca.uhn.fhir.jpa.subscription.submit.svc;

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

import ca.uhn.fhir.jpa.model.entity.IResourceModifiedPK;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetry;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME;

public class ResourceModifiedSubmitterSvc implements IResourceModifiedConsumer, IResourceModifiedConsumerWithRetry {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceModifiedSubmitterSvc.class);
	private volatile MessageChannel myMatchingChannel;

	private StorageSettings myStorageSettings;
	private SubscriptionChannelFactory mySubscriptionChannelFactory;
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	private PlatformTransactionManager myTxManager;

	@EventListener(classes = {ContextRefreshedEvent.class})
	public void startIfNeeded() {
		if (!myStorageSettings.hasSupportedSubscriptionTypes()) {
			ourLog.debug("Subscriptions are disabled on this server.  Skipping {} channel creation.", SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			return;
		}
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingSendingChannel(SUBSCRIPTION_MATCHING_CHANNEL_NAME, getChannelProducerSettings());
		}
	}

	public ResourceModifiedSubmitterSvc(StorageSettings theStorageSettings, SubscriptionChannelFactory theSubscriptionChannelFactory, IResourceModifiedMessagePersistenceSvc theResourceModifiedMessagePersistenceSvc, PlatformTransactionManager theTxManager) {
		myStorageSettings = theStorageSettings;
		mySubscriptionChannelFactory = theSubscriptionChannelFactory;
		myResourceModifiedMessagePersistenceSvc = theResourceModifiedMessagePersistenceSvc;
		myTxManager = theTxManager;
	}

	/**
	 * Submit a message to the broker without retries if submission fails.
	 *
	 * @param theMsg the ResourceModifiedMessage to be processed.
	 */
	@Override
	public void processResourceModified(ResourceModifiedMessage theMsg) {
		startIfNeeded();

		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(myMatchingChannel, "A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMsg));
	}

	@Override
	public boolean processResourceModified(IResourceModifiedPK theResourceModifiedPK){

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);

		return (boolean) txTemplate.execute(doProcessResourceModifiedInTransaction(theResourceModifiedPK));

	}

	protected TransactionCallback doProcessResourceModifiedInTransaction(IResourceModifiedPK theResourceModifiedPK) {
		return theStatus -> {
			boolean processed = true;

			ResourceModifiedMessage resourceModifiedMessage = null;

			try {
				// we start by deleting the entity to lock the table.
				boolean wasDeleted = myResourceModifiedMessagePersistenceSvc.deleteByPK(theResourceModifiedPK);

				if(wasDeleted) {
					resourceModifiedMessage = myResourceModifiedMessagePersistenceSvc.inflateResourceModifiedMessageFromPK(theResourceModifiedPK);

					processResourceModified(resourceModifiedMessage);
				}

			} catch (Throwable theThrowable) {
				// we encountered an issue (again) when trying to send the message so mark the transaction for rollback
				ourLog.warn("Channel submission failed for resource with id {} matching subscription with id {}.  Further attempts will be performed at later time.", resourceModifiedMessage.getPayloadId(), resourceModifiedMessage.getSubscriptionId());
				processed = false;
				theStatus.setRollbackOnly();
			}

			return processed;
		};

	}

	private ChannelProducerSettings getChannelProducerSettings() {
		ChannelProducerSettings channelProducerSettings= new ChannelProducerSettings();
		channelProducerSettings.setQualifyChannelName(myStorageSettings.isQualifySubscriptionMatchingChannelName());
		return channelProducerSettings;
	}

	@VisibleForTesting
	public LinkedBlockingChannel getProcessingChannelForUnitTest() {
		return (LinkedBlockingChannel) myMatchingChannel;
	}

	public void setMatchingChannel(MessageChannel theMatchingChannel){
		myMatchingChannel = theMatchingChannel;
	}
}
