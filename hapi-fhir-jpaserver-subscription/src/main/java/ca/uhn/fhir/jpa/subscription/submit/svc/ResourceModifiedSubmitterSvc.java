package ca.uhn.fhir.jpa.subscription.submit.svc;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.ISendResult;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessagePK;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.interceptor.SubscriptionMatcherInterceptor;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import ca.uhn.fhir.util.IoUtils;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionMatchingListener.SUBSCRIPTION_MATCHING_CHANNEL_NAME;

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
	private volatile IChannelProducer<ResourceModifiedMessage> myMatchingChannelProducer;

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
		if (myMatchingChannelProducer == null) {
			myMatchingChannelProducer = mySubscriptionChannelFactory.newMatchingProducer(
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
	 * @inheritDoc Submit a message to the broker without retries.
	 * <p>
	 * Implementation of the {@link IResourceModifiedConsumer}
	 * @return the result of the send operation
	 */
	@Override
	public ISendResult submitResourceModified(ResourceModifiedMessage theMsg) {
		startIfNeeded();

		ourLog.trace("Sending resource modified message to processing channel");
		Validate.notNull(
				myMatchingChannelProducer,
				"A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		return myMatchingChannelProducer.send(new ResourceModifiedJsonMessage(theMsg));
	}

	/**
	 * Submit a whole batch of messages to the broker in a single call so that the cost of synchronizing with the broker
	 * is paid once for the batch rather than once per message.
	 *
	 * @param theMessages the messages to submit
	 * @return the result of each send operation, in the same order as <code>theMessages</code>
	 */
	protected List<ISendResult> submitResourceModifiedBatch(List<IMessage<ResourceModifiedMessage>> theMessages) {
		startIfNeeded();

		ourLog.trace("Sending {} resource modified messages to processing channel", theMessages.size());
		Validate.notNull(
				myMatchingChannelProducer,
				"A SubscriptionMatcherInterceptor has been registered without calling start() on it.");
		return myMatchingChannelProducer.sendAll(theMessages);
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
	 * This method drains a whole batch of IPersistedResourceModifiedMessage in a single unit of work: one transaction,
	 * one batched delete and one batched submission to the broker.  It is the batched counterpart of
	 * {@link #submitPersisedResourceModifiedMessage(IPersistedResourceModifiedMessage)}.
	 * <p>
	 * The batch is all-or-nothing.  If the broker rejects any message of the batch, whether by throwing or by returning
	 * an unsuccessful {@link ISendResult}, the transaction is rolled back so that every row remains available for
	 * re-submission at a later time.  Rows are never deleted unless the whole batch was acknowledged by the broker,
	 * since the pipeline tolerates delivering a message more than once but never tolerates losing one.
	 * </p>
	 *
	 * @param thePersistedResourceModifiedMessages the batch of messages requiring submission
	 * @return the number of messages which were successfully processed, which is <code>0</code> when the batch was
	 * rolled back.
	 */
	@Override
	public int submitPersistedResourceModifiedMessages(
			List<IPersistedResourceModifiedMessage> thePersistedResourceModifiedMessages) {
		if (thePersistedResourceModifiedMessages.isEmpty()) {
			// nothing to do: never open a transaction, hit the database or talk to the broker for an empty batch
			return 0;
		}

		return myHapiTransactionService
				.withSystemRequest()
				.withPropagation(Propagation.REQUIRES_NEW)
				.execute(doProcessResourceModifiedBatchInTransaction(thePersistedResourceModifiedMessages));
	}

	/**
	 * The batched counterpart of {@link #doProcessResourceModifiedInTransaction(IPersistedResourceModifiedMessage)}.  It
	 * requires execution in a transaction so that the batched deletion of the persistedResourceModifiedMessage pointed
	 * to by <code>thePersistedResourceModifiedMessages</code> can be rolled back in the event where submission fails.
	 *
	 * @param thePersistedResourceModifiedMessages the batch of messages requiring submission
	 * @return the number of messages which were successfully processed, which is <code>0</code> when the batch was
	 * rolled back.
	 */
	protected TransactionCallback<Integer> doProcessResourceModifiedBatchInTransaction(
			List<IPersistedResourceModifiedMessage> thePersistedResourceModifiedMessages) {
		return theStatus -> {
			int batchSize = thePersistedResourceModifiedMessages.size();
			List<IMessage<ResourceModifiedMessage>> messagesToSend;

			try {
				deletePersistedResourceModifiedMessages(thePersistedResourceModifiedMessages);

				messagesToSend = createMessagesToSend(thePersistedResourceModifiedMessages);
			} catch (Exception ex) {
				// catching Exception is deliberate here, despite this generally being frowned upon: any failure while
				// preparing the batch must roll the transaction back, since we cannot tell how much of the batch was
				// affected and losing a row is not acceptable.
				ourLog.error(
						Msg.code(3017)
								+ "Failed to prepare a batch of {} resource modified messages for submission.  Further attempts will be performed at later time.",
						batchSize,
						ex);
				theStatus.setRollbackOnly();
				return 0;
			}

			if (messagesToSend.isEmpty()) {
				// every row of the batch was unusable, but they have been deleted, so the pass may keep going
				return batchSize;
			}

			try {
				List<ISendResult> sendResults = submitResourceModifiedBatch(messagesToSend);

				if (!isEverySendSuccessful(sendResults, messagesToSend.size())) {
					// a producer is allowed to report a failed send without throwing.  The single message path discards
					// its ISendResult, so this is a failure mode which only the batch API can observe; it means the
					// broker did not acknowledge the messages, so it is handled exactly like a
					// MessageDeliveryException.
					ourLog.error(
							Msg.code(3018)
									+ "Channel submission was not acknowledged for a batch of {} resource modified messages.  Further attempts will be performed at later time.",
							batchSize);
					theStatus.setRollbackOnly();
					return 0;
				}
			} catch (Exception exception) {
				// we encountered an issue when trying to send the batch so mark the transaction for rollback.  We
				// cannot tell which messages of the batch were acknowledged, so no row of the batch may be deleted.
				ourLog.error(
						Msg.code(3019)
								+ "Channel submission failed for a batch of {} resource modified messages.  Further attempts will be performed at later time.",
						batchSize,
						exception);
				theStatus.setRollbackOnly();
				return 0;
			}

			return batchSize;
		};
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

	/**
	 * Build the outgoing message for every row of the batch.  A row whose stored summary cannot be turned into a
	 * message can never be submitted, so it is dropped from the batch and left deleted - exactly what the single
	 * message path does - rather than blocking every other message of the batch forever.
	 */
	private List<IMessage<ResourceModifiedMessage>> createMessagesToSend(
			List<IPersistedResourceModifiedMessage> thePersistedResourceModifiedMessages) {
		List<IMessage<ResourceModifiedMessage>> retVal = new ArrayList<>(thePersistedResourceModifiedMessages.size());

		for (IPersistedResourceModifiedMessage nextPersistedResourceModifiedMessage :
				thePersistedResourceModifiedMessages) {
			try {
				retVal.add(new ResourceModifiedJsonMessage(
						createResourceModifiedMessageWithoutInflation(nextPersistedResourceModifiedMessage)));
			} catch (Exception ex) {
				ourLog.error(
						Msg.code(3020)
								+ "Unexpected error encountered while processing resource modified message {}. Marking as processed to prevent further errors.",
						nextPersistedResourceModifiedMessage.getPersistedResourceModifiedMessagePk(),
						ex);
			}
		}

		return retVal;
	}

	private static boolean isEverySendSuccessful(List<ISendResult> theSendResults, int theExpectedResultCount) {
		if (theSendResults == null || theSendResults.size() != theExpectedResultCount) {
			return false;
		}
		return theSendResults.stream().allMatch(ISendResult::isSuccessful);
	}

	private void deletePersistedResourceModifiedMessages(
			List<IPersistedResourceModifiedMessage> thePersistedResourceModifiedMessages) {
		List<IPersistedResourceModifiedMessagePK> pks = thePersistedResourceModifiedMessages.stream()
				.map(IPersistedResourceModifiedMessage::getPersistedResourceModifiedMessagePk)
				.collect(Collectors.toList());

		int deletedCount = myResourceModifiedMessagePersistenceSvc.deleteByPKs(pks);

		if (deletedCount < pks.size()) {
			ourLog.warn(
					"Only {} of {} persisted resource modified messages were deleted, the remainder had already been deleted.  The whole batch will be submitted.",
					deletedCount,
					pks.size());
		}
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

	@PreDestroy
	public void shutdown() throws Exception {
		if (myMatchingChannelProducer instanceof AutoCloseable producer) {
			IoUtils.closeQuietly(producer, ourLog);
		}
	}

	@VisibleForTesting
	public IChannelProducer<ResourceModifiedMessage> getMatchingChannelProducerForUnitTest() {
		startIfNeeded();
		return myMatchingChannelProducer;
	}
}
