package ca.uhn.fhir.jpa.subscription.submit.svc;

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

import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessagePK;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingSubscriber.SUBSCRIPTION_MATCHING_CHANNEL_NAME;

/**
 * This service provides two distinct contexts in which it submits messages to the subscription pipeline.
 *
 * It implements {@link IResourceModifiedConsumer} for synchronous submissions where retry upon failures is not required.
 *
 * It implements {@link IResourceModifiedConsumerWithRetries} for synchronous submissions performed as part of processing
 * an operation on a resource (see {@link SubscriptionMatcherInterceptor}).  Submissions in such context require retries
 * upon submission failure.
 *
 *
 */
public class ResourceModifiedSubmitterSvc implements IResourceModifiedConsumer, IResourceModifiedConsumerWithRetries {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceModifiedSubmitterSvc.class);
	private volatile MessageChannel myMatchingChannel;

	private final SubscriptionSettings mySubscriptionSettings;
	private final SubscriptionChannelFactory mySubscriptionChannelFactory;
	private final IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	private final IHapiTransactionService myHapiTransactionService;

	@EventListener(classes = {ContextRefreshedEvent.class})
	public void startIfNeeded() {
		if (!mySubscriptionSettings.hasSupportedSubscriptionTypes()) {
			ourLog.debug(
					"Subscriptions are disabled on this server.  Skipping {} channel creation.",
					SUBSCRIPTION_MATCHING_CHANNEL_NAME);
			return;
		}
		if (myMatchingChannel == null) {
			myMatchingChannel = mySubscriptionChannelFactory.newMatchingSendingChannel(
					SUBSCRIPTION_MATCHING_CHANNEL_NAME, getChannelProducerSettings());
		}
	}

	public ResourceModifiedSubmitterSvc(
			SubscriptionSettings theSubscriptionSettings,
			SubscriptionChannelFactory theSubscriptionChannelFactory,
			IResourceModifiedMessagePersistenceSvc resourceModifiedMessagePersistenceSvc,
			IHapiTransactionService theHapiTransactionService) {
		mySubscriptionSettings = theSubscriptionSettings;
		mySubscriptionChannelFactory = theSubscriptionChannelFactory;
		myResourceModifiedMessagePersistenceSvc = resourceModifiedMessagePersistenceSvc;
		myHapiTransactionService = theHapiTransactionService;
	}

	/**
	 * @inheritDoc
	 * Submit a message to the broker without retries.
	 *
	 * Implementation of the {@link IResourceModifiedConsumer}
	 *
	 */
	@Override
	public void submitResourceModified(ResourceModifiedMessage theMsg) {
		startIfNeeded();

		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(
				myMatchingChannel,
				"A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		myMatchingChannel.send(new ResourceModifiedJsonMessage(theMsg));
	}

	/**
	 * This method will inflate the ResourceModifiedMessage represented by the IPersistedResourceModifiedMessage and attempts
	 * to submit it to the subscription processing pipeline.
	 *
	 * If submission succeeds, the IPersistedResourceModifiedMessage is deleted and true is returned.  In the event where submission
	 * fails, we return false and the IPersistedResourceModifiedMessage is rollback for later re-submission.
	 *
	 * @param thePersistedResourceModifiedMessage A ResourceModifiedMessage in it's IPersistedResourceModifiedMessage that requires submission.
	 * @return Whether the message was successfully submitted to the broker.
	 */
	@Override
	public boolean submitPersisedResourceModifiedMessage(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage) {
		return myHapiTransactionService
				.withSystemRequest()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(doProcessResourceModifiedInTransaction(thePersistedResourceModifiedMessage));
	}

	/**
	 * This method is the cornerstone in the submit and retry upon failure mechanism for messages needing submission to the subscription processing pipeline.
	 * It requires execution in a transaction for rollback of deleting the persistedResourceModifiedMessage pointed to by <code>thePersistedResourceModifiedMessage<code/>
	 * in the event where submission would fail.
	 *
	 * @param thePersistedResourceModifiedMessage the primary key pointing to the persisted version (IPersistedResourceModifiedMessage) of a ResourceModifiedMessage needing submission
	 * @return true upon successful submission, false otherwise.
	 */
	protected TransactionCallback<Boolean> doProcessResourceModifiedInTransaction(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage) {
		return theStatus -> {
			boolean processed = true;
			ResourceModifiedMessage resourceModifiedMessage = null;

			try {
				// delete the entry to lock the row to ensure unique processing
				boolean wasDeleted = deletePersistedResourceModifiedMessage(
						thePersistedResourceModifiedMessage.getPersistedResourceModifiedMessagePk());

				// submit the resource modified message with empty payload, actual inflation is done by the matcher.
				resourceModifiedMessage =
						createResourceModifiedMessageWithoutInflation(thePersistedResourceModifiedMessage);

				if (wasDeleted) {
					submitResourceModified(resourceModifiedMessage);
				}
			} catch (MessageDeliveryException exception) {
				// we encountered an issue when trying to send the message so mark the transaction for rollback
				String payloadId = "[unknown]";
				String subscriptionId = "[unknown]";
				if (resourceModifiedMessage != null) {
					payloadId = resourceModifiedMessage.getPayloadId();
					subscriptionId = resourceModifiedMessage.getSubscriptionId();
				}
				ourLog.error(
						"Channel submission failed for resource with id {} matching subscription with id {}.  Further attempts will be performed at later time.",
						payloadId,
						subscriptionId,
						exception);
				processed = false;
				theStatus.setRollbackOnly();
			} catch (Exception ex) {
				// catch other errors
				ourLog.error(
						"Unexpected error encountered while processing resource modified message. Marking as processed to prevent further errors.",
						ex);
				processed = true;
			}

			return processed;
		};
	}

	private ResourceModifiedMessage createResourceModifiedMessageWithoutInflation(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage) {
		return myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(
				thePersistedResourceModifiedMessage);
	}

	private boolean deletePersistedResourceModifiedMessage(IPersistedResourceModifiedMessagePK theResourceModifiedPK) {
		try {
			// delete the entry to lock the row to ensure unique processing
			return myResourceModifiedMessagePersistenceSvc.deleteByPK(theResourceModifiedPK);
		} catch (ResourceNotFoundException exception) {
			ourLog.warn(
					"thePersistedResourceModifiedMessage with {} and version {} could not be deleted as it may have already been deleted.",
					theResourceModifiedPK.getResourcePid(),
					theResourceModifiedPK.getResourceVersion());
			// we were not able to delete the pk.  this implies that someone else did read/delete the PK and processed
			// the message
			// successfully before we did.

			return false;
		} catch (Exception ex) {
			ourLog.error("Unknown exception when deleting persisted resource modified message. Returning false.", ex);
			return false;
		}
	}

	private ChannelProducerSettings getChannelProducerSettings() {
		ChannelProducerSettings channelProducerSettings = new ChannelProducerSettings();
		channelProducerSettings.setQualifyChannelName(
				mySubscriptionSettings.isQualifySubscriptionMatchingChannelName());
		return channelProducerSettings;
	}

	public IChannelProducer getProcessingChannelForUnitTest() {
		startIfNeeded();
		return (IChannelProducer) myMatchingChannel;
	}
}
