package ca.uhn.fhir.jpa.subscription;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.interceptor.api.Hook;
import ca.uhn.fhir.jpa.model.interceptor.api.Interceptor;
import ca.uhn.fhir.jpa.model.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 *
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
@Service
@Lazy
@Interceptor(manualRegistration = true)
public class SubscriptionActivatingInterceptor {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingInterceptor.class);

	private static boolean ourWaitForSubscriptionActivationSynchronouslyForUnitTest;

	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired
	@Qualifier(BaseConfig.TASK_EXECUTOR_NAME)
	private AsyncTaskExecutor myTaskExecutor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoConfig myDaoConfig;
	@Autowired
	private SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	public boolean activateOrRegisterSubscriptionIfRequired(final IBaseResource theSubscription) {
		// Grab the value for "Subscription.channel.type" so we can see if this
		// subscriber applies..
		String subscriptionChannelTypeCode = myFhirContext
			.newTerser()
			.getSingleValueOrNull(theSubscription, SubscriptionConstants.SUBSCRIPTION_TYPE, IPrimitiveType.class)
			.getValueAsString();

		Subscription.SubscriptionChannelType subscriptionChannelType = Subscription.SubscriptionChannelType.fromCode(subscriptionChannelTypeCode);
		// Only activate supported subscriptions
		if (!myDaoConfig.getSupportedSubscriptionTypes().contains(subscriptionChannelType)) {
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

	private boolean activateSubscription(String theActiveStatus, final IBaseResource theSubscription, String theRequestedStatus) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		IBaseResource subscription = subscriptionDao.read(theSubscription.getIdElement());
		subscription.setId(subscription.getIdElement().toVersionless());

		ourLog.info("Activating subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus);
		try {
			SubscriptionUtil.setStatus(myFhirContext, subscription, theActiveStatus);
			subscription = subscriptionDao.update(subscription).getResource();
			submitResourceModifiedForUpdate(subscription);
			return true;
		} catch (final UnprocessableEntityException e) {
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myFhirContext, subscription, "error");
			SubscriptionUtil.setReason(myFhirContext, subscription, e.getMessage());
			subscriptionDao.update(subscription);
			return false;
		}
	}

	void submitResourceModifiedForUpdate(IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	@Hook(Pointcut.OP_PRESTORAGE_RESOURCE_CREATED)
	public void addStrategyTagCreated(IBaseResource theResource) {
		if (isSubscription(theResource)) {
			validateCriteriaAndAddStrategy(theResource);
		}
	}

	@Hook(Pointcut.OP_PRESTORAGE_RESOURCE_UPDATED)
	public void addStrategyTagUpdated(IBaseResource theOldResource, IBaseResource theNewResource) {
		if (isSubscription(theNewResource)) {
			validateCriteriaAndAddStrategy(theNewResource);
		}
	}

	// TODO KHS add third type of strategy DISABLED if that subscription type is disabled on this server
	public void validateCriteriaAndAddStrategy(final IBaseResource theResource) {
		String criteria = mySubscriptionCanonicalizer.getCriteria(theResource);
		try {
			SubscriptionMatchingStrategy strategy = mySubscriptionStrategyEvaluator.determineStrategy(criteria);
			mySubscriptionCanonicalizer.setMatchingStrategyTag(theResource, strategy);
		} catch (InvalidRequestException | DataFormatException e) {
			throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + criteria + " " + e.getMessage());
		}
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Hook(Pointcut.OP_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	private void submitResourceModified(IBaseResource theNewResource, ResourceModifiedMessage.OperationTypeEnum theOperationType) {
		if (isSubscription(theNewResource)) {
			submitResourceModified(new ResourceModifiedMessage(myFhirContext, theNewResource, theOperationType));
		}
	}

	private boolean isSubscription(IBaseResource theNewResource) {
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theNewResource);
		return ResourceTypeEnum.SUBSCRIPTION.getCode().equals(resourceDefinition.getName());
	}

	private void submitResourceModified(final ResourceModifiedMessage theMsg) {
		switch (theMsg.getOperationType()) {
			case DELETE:
				mySubscriptionRegistry.unregisterSubscription(theMsg.getId(myFhirContext));
				break;
			case CREATE:
			case UPDATE:
				activateAndRegisterSubscriptionIfRequiredInTransaction(theMsg.getNewPayload(myFhirContext));
				break;
			default:
				break;
		}
	}

	private void activateAndRegisterSubscriptionIfRequiredInTransaction(IBaseResource theSubscription) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				activateOrRegisterSubscriptionIfRequired(theSubscription);
			}
		});
	}


	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
