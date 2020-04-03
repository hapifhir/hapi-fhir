package ca.uhn.fhir.jpa.subscription.process.matcher.subscriber;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.process.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.process.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 * <p>
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
public class SubscriptionActivatingSubscriber extends BaseSubscriberForSubscriptionResources implements MessageHandler {
	private static boolean ourWaitForSubscriptionActivationSynchronouslyForUnitTest;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);
	@Autowired
	private PlatformTransactionManager myTransactionManager;
	// FIXME: use constant if this is still needed
	@Autowired
	@Qualifier("hapiJpaTaskExecutor")
	private AsyncTaskExecutor myTaskExecutor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	/**
	 * Constructor
	 */
	public SubscriptionActivatingSubscriber() {
		super();
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Received message of unexpected type on matching channel: {}", theMessage);
			return;
		}

		ResourceModifiedMessage payload = ((ResourceModifiedJsonMessage) theMessage).getPayload();
		if (!isSubscription(payload)) {
			return;
		}

		switch (payload.getOperationType()) {
			case CREATE:
			case UPDATE:
				activateAndRegisterSubscriptionIfRequiredInTransaction(payload.getNewPayload(myFhirContext));
				break;
			case DELETE:
			case MANUALLY_TRIGGERED:
			default:
				break;
		}

	}

	public boolean activateOrRegisterSubscriptionIfRequired(final IBaseResource theSubscription) {
		// Grab the value for "Subscription.channel.type" so we can see if this
		// subscriber applies..
		CanonicalSubscriptionChannelType subscriptionChannelType = mySubscriptionCanonicalizer.getChannelType(theSubscription);

		// Only activate supported subscriptions
		if (subscriptionChannelType == null || !myDaoConfig.getSupportedSubscriptionTypes().contains(subscriptionChannelType.toCanonical())) {
			return false;
		}

		String statusString = mySubscriptionCanonicalizer.getSubscriptionStatus(theSubscription);

		if (SubscriptionConstants.REQUESTED_STATUS.equals(statusString)) {
			if (TransactionSynchronizationManager.isSynchronizationActive()) {
				/*
				 * If we're in a transaction, we don't want to try and change the status from
				 * requested to active within the same transaction because it's too late by
				 * the time we get here to make modifications to the payload.
				 *
				 * So, we register a synchronization, meaning that when the transaction is
				 * finished, we'll schedule a task to do this in a separate worker thread
				 * to avoid any possibility of conflict.
				 */
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						Future<?> activationFuture = myTaskExecutor.submit(new Runnable() {
							@Override
							public void run() {
								activateSubscription(SubscriptionConstants.ACTIVE_STATUS, theSubscription, SubscriptionConstants.REQUESTED_STATUS);
							}
						});

						/*
						 * If we're running in a unit test, it's nice to be predictable in
						 * terms of order... In the real world it's a recipe for deadlocks
						 */
						if (ourWaitForSubscriptionActivationSynchronouslyForUnitTest) {
							try {
								activationFuture.get(5, TimeUnit.SECONDS);
							} catch (Exception e) {
								ourLog.error("Failed to activate subscription", e);
							}
						}
					}
				});
				return true;
			} else {
				return activateSubscription(SubscriptionConstants.ACTIVE_STATUS, theSubscription, SubscriptionConstants.REQUESTED_STATUS);
			}
		} else if (SubscriptionConstants.ACTIVE_STATUS.equals(statusString)) {
			return mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(theSubscription);
		} else {
			// Status isn't "active" or "requested"
			return mySubscriptionRegistry.unregisterSubscriptionIfRegistered(theSubscription, statusString);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean activateSubscription(String theActiveStatus, final IBaseResource theSubscription, String theRequestedStatus) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		IBaseResource subscription = subscriptionDao.read(theSubscription.getIdElement());
		subscription.setId(subscription.getIdElement().toVersionless());

		ourLog.info("Activating subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus);
		try {
			SubscriptionUtil.setStatus(myFhirContext, subscription, theActiveStatus);
			subscriptionDao.update(subscription);
			return true;
		} catch (final UnprocessableEntityException e) {
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myFhirContext, subscription, "error");
			SubscriptionUtil.setReason(myFhirContext, subscription, e.getMessage());
			subscriptionDao.update(subscription);
			return false;
		}
	}


	private boolean isSubscription(IBaseResource theNewResource) {
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theNewResource);
		return ResourceTypeEnum.SUBSCRIPTION.getCode().equals(resourceDefinition.getName());
	}

	private void activateAndRegisterSubscriptionIfRequiredInTransaction(IBaseResource theSubscription) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				activateOrRegisterSubscriptionIfRequired(theSubscription);
			}
		});
	}

	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
