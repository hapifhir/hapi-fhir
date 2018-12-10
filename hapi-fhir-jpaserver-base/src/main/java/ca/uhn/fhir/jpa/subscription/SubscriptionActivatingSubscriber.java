package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.search.warm.CacheWarmingSvcImpl;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionCannonicalizer;
import ca.uhn.fhir.jpa.subscription.cache.SubscriptionRegistry;
import ca.uhn.fhir.model.dstu2.valueset.ResourceTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.Subscription;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
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
@Component
// FIXME KHS remove prototype
@Scope("prototype")
public class SubscriptionActivatingSubscriber {
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);

	private static boolean ourWaitForSubscriptionActivationSynchronouslyForUnitTest;

	@Autowired
	private PlatformTransactionManager myTransactionManager;
	@Autowired
	private AsyncTaskExecutor myTaskExecutor;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionCannonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionCannonicalizer mySubscriptionCannonicalizer;
	@Autowired
	private MatchUrlService myMatchUrlService;

//	private final BaseSubscriptionInterceptor mySubscriptionInterceptor;

	public SubscriptionActivatingSubscriber(BaseSubscriptionInterceptor theSubscriptionInterceptor) {
		mySubscriptionInterceptor = theSubscriptionInterceptor;
	}

	public boolean activateOrRegisterSubscriptionIfRequired(final IBaseResource theSubscription) {
		// Grab the value for "Subscription.channel.type" so we can see if this
		// subscriber applies..
		String subscriptionChannelType = myFhirContext
			.newTerser()
			.getSingleValueOrNull(theSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_TYPE, IPrimitiveType.class)
			.getValueAsString();

		final IPrimitiveType<?> status = myFhirContext.newTerser().getSingleValueOrNull(theSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_STATUS, IPrimitiveType.class);
		String statusString = status.getValueAsString();

		final String requestedStatus = Subscription.SubscriptionStatus.REQUESTED.toCode();
		final String activeStatus = Subscription.SubscriptionStatus.ACTIVE.toCode();
		if (requestedStatus.equals(statusString)) {
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
								activateSubscription(activeStatus, theSubscription, requestedStatus);
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
				return activateSubscription(activeStatus, theSubscription, requestedStatus);
			}
		} else if (activeStatus.equals(statusString)) {
			return registerSubscriptionUnlessAlreadyRegistered(theSubscription);
		} else {
			// Status isn't "active" or "requested"
			return unregisterSubscriptionIfRegistered(theSubscription, statusString);
		}
	}

	protected boolean unregisterSubscriptionIfRegistered(IBaseResource theSubscription, String theStatusString) {
		if (mySubscriptionRegistry.hasSubscription(theSubscription.getIdElement()) != null) {
			ourLog.info("Removing {} subscription {}", theStatusString, theSubscription.getIdElement().toUnqualified().getValue());
			mySubscriptionRegistry.unregisterSubscription(theSubscription.getIdElement());
			return true;
		}
		return false;
	}

	private boolean activateSubscription(String theActiveStatus, final IBaseResource theSubscription, String theRequestedStatus) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		IBaseResource subscription = subscriptionDao.read(theSubscription.getIdElement());

		ourLog.info("Activating subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus);
		try {
			SubscriptionUtil.setStatus(myFhirContext, subscription, theActiveStatus);
			subscription = subscriptionDao.update(subscription).getResource();
			mySubscriptionInterceptor.submitResourceModifiedForUpdate(subscription);
			return true;
		} catch (final UnprocessableEntityException e) {
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myFhirContext, subscription, "error");
			SubscriptionUtil.setReason(myFhirContext, subscription, e.getMessage());
			subscriptionDao.update(subscription);
			return false;
		}
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	public void handleMessage(ResourceModifiedMessage.OperationTypeEnum theOperationType, IIdType theId, final IBaseResource theSubscription) throws MessagingException {
		if (!theId.getResourceType().equals(ResourceTypeEnum.SUBSCRIPTION.getCode())) {
			return;
		}
		switch (theOperationType) {
			case DELETE:
				mySubscriptionRegistry.unregisterSubscription(theId);
				break;
			case CREATE:
			case UPDATE:
				validateCriteria(theSubscription);
				activateAndRegisterSubscriptionIfRequiredInTransaction(theSubscription);
				break;
			default:
				break;
		}
	}

	public void validateCriteria(final IBaseResource theResource) {
		CanonicalSubscription subscription = mySubscriptionCannonicalizer.canonicalize(theResource);
		String criteria = subscription.getCriteriaString();
		try {
			RuntimeResourceDefinition resourceDef = CacheWarmingSvcImpl.parseUrlResourceType(myFhirContext, criteria);
			myMatchUrlService.translateMatchUrl(criteria, resourceDef);
		} catch (InvalidRequestException e) {
			throw new UnprocessableEntityException("Invalid subscription criteria submitted: " + criteria + " " + e.getMessage());
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

	protected synchronized boolean registerSubscriptionUnlessAlreadyRegistered(IBaseResource theSubscription) {
		CanonicalSubscription existingSubscription = mySubscriptionRegistry.hasSubscription(theSubscription.getIdElement());
		CanonicalSubscription newSubscription = mySubscriptionCanonicalizer.canonicalize(theSubscription);

		if (existingSubscription != null) {
			if (newSubscription.equals(existingSubscription)) {
				// No changes
				return false;
			}
		}

		if (existingSubscription != null) {
			ourLog.info("Updating already-registered active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
			mySubscriptionRegistry.unregisterSubscription(theSubscription.getIdElement());
		} else {
			ourLog.info("Registering active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
		}
		mySubscriptionRegistry.registerSubscription(theSubscription.getIdElement(), theSubscription, mySubscriptionInterceptor);
		return true;
	}

	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
