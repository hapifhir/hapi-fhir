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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.IDaoRegistry;
import ca.uhn.fhir.jpa.config.BaseConfig;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.dstu2.model.Subscription;
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
@Lazy
@Interceptor
public class SubscriptionActivatingInterceptor {
	private static boolean ourWaitForSubscriptionActivationSynchronouslyForUnitTest;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingInterceptor.class);
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

	/**
	 * Constructor
	 */
	public SubscriptionActivatingInterceptor() {
		super();
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

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdatedPreCommit(IBaseResource theOldResource, IBaseResource theNewResource) {
		submitResourceModified(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreatedPreCommit(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeletedPreCommit(IBaseResource theResource) {
		submitResourceModified(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourceCreatedPreStorage(IBaseResource theResource) {
		if (isSubscription(theResource)) {
			validateSubmittedSubscription(theResource);
		}
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourceUpdatedPreStorage(IBaseResource theOldResource, IBaseResource theNewResource) {
		if (isSubscription(theNewResource)) {
			validateSubmittedSubscription(theNewResource);
		}
	}

	@VisibleForTesting
	@SuppressWarnings("WeakerAccess")
	public void setSubscriptionStrategyEvaluatorForUnitTest(SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		mySubscriptionStrategyEvaluator = theSubscriptionStrategyEvaluator;
	}

	@SuppressWarnings("WeakerAccess")
	public void validateSubmittedSubscription(IBaseResource theSubscription) {

		CanonicalSubscription subscription = mySubscriptionCanonicalizer.canonicalize(theSubscription);
		boolean finished = false;
		if (subscription.getStatus() == null) {
			throw new UnprocessableEntityException("Can not process submitted Subscription - Subscription.status must be populated on this server");
		}

		switch (subscription.getStatus()) {
			case REQUESTED:
			case ACTIVE:
				break;
			case ERROR:
			case OFF:
			case NULL:
				finished = true;
				break;
		}

		mySubscriptionCanonicalizer.setMatchingStrategyTag(theSubscription, null);

		if (!finished) {

			String query = subscription.getCriteriaString();
			if (isBlank(query)) {
				throw new UnprocessableEntityException("Subscription.criteria must be populated");
			}

			int sep = query.indexOf('?');
			if (sep <= 1) {
				throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
			}

			String resType = query.substring(0, sep);
			if (resType.contains("/")) {
				throw new UnprocessableEntityException("Subscription.criteria must be in the form \"{Resource Type}?[params]\"");
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException("Subscription.channel.type must be populated");
			} else if (subscription.getChannelType() == CanonicalSubscriptionChannelType.RESTHOOK) {
				validateChannelPayload(subscription);
				validateChannelEndpoint(subscription);
			}

			if (!myDaoRegistry.isResourceTypeSupported(resType)) {
				throw new UnprocessableEntityException("Subscription.criteria contains invalid/unsupported resource type: " + resType);
			}

			try {
				SubscriptionMatchingStrategy strategy = mySubscriptionStrategyEvaluator.determineStrategy(query);
				mySubscriptionCanonicalizer.setMatchingStrategyTag(theSubscription, strategy);
			} catch (InvalidRequestException | DataFormatException e) {
				throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + query + " " + e.getMessage());
			}

			if (subscription.getChannelType() == null) {
				throw new UnprocessableEntityException("Subscription.channel.type must be populated on this server");
			}

		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelEndpoint(CanonicalSubscription theResource) {
		if (isBlank(theResource.getEndpointUrl())) {
			throw new UnprocessableEntityException("Rest-hook subscriptions must have Subscription.channel.endpoint defined");
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected void validateChannelPayload(CanonicalSubscription theResource) {
		if (!isBlank(theResource.getPayloadString()) && EncodingEnum.forContentType(theResource.getPayloadString()) == null) {
			throw new UnprocessableEntityException("Invalid value for Subscription.channel.payload: " + theResource.getPayloadString());
		}
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
				mySubscriptionRegistry.unregisterSubscription(theMsg.getId(myFhirContext).getIdPart());
				break;
			case CREATE:
			case UPDATE:
				activateAndRegisterSubscriptionIfRequiredInTransaction(theMsg.getNewPayload(myFhirContext));
				break;
			case MANUALLY_TRIGGERED:
			default:
				break;
		}
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

	@SuppressWarnings("WeakerAccess")
	@VisibleForTesting
	public void setSubscriptionCanonicalizerForUnitTest(SubscriptionCanonicalizer theSubscriptionCanonicalizer) {
		mySubscriptionCanonicalizer = theSubscriptionCanonicalizer;
	}

	@SuppressWarnings("WeakerAccess")
	@VisibleForTesting
	public void setDaoRegistryForUnitTest(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}


	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
