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
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unchecked")
public class SubscriptionActivatingSubscriber {
	private static boolean ourWaitForSubscriptionActivationSynchronouslyForUnitTest;
	private final IFhirResourceDao mySubscriptionDao;
	private final BaseSubscriptionInterceptor mySubscriptionInterceptor;
	private final PlatformTransactionManager myTransactionManager;
	private final AsyncTaskExecutor myTaskExecutor;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);
	private FhirContext myCtx;
	private Subscription.SubscriptionChannelType myChannelType;


	/**
	 * Constructor
	 */
	public SubscriptionActivatingSubscriber(IFhirResourceDao<? extends IBaseResource> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor, PlatformTransactionManager theTransactionManager, AsyncTaskExecutor theTaskExecutor) {
		mySubscriptionDao = theSubscriptionDao;
		mySubscriptionInterceptor = theSubscriptionInterceptor;
		myChannelType = theChannelType;
		myCtx = theSubscriptionDao.getContext();
		myTransactionManager = theTransactionManager;
		myTaskExecutor = theTaskExecutor;
		Validate.notNull(theTaskExecutor);
	}

	public synchronized boolean activateOrRegisterSubscriptionIfRequired(final IBaseResource theSubscription) {
		// Grab the value for "Subscription.channel.type" so we can see if this
		// subscriber applies..
		String subscriptionChannelType = myCtx
			.newTerser()
			.getSingleValueOrNull(theSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_TYPE, IPrimitiveType.class)
			.getValueAsString();
		boolean subscriptionTypeApplies = BaseSubscriptionSubscriber.subscriptionTypeApplies(subscriptionChannelType, myChannelType);
		if (subscriptionTypeApplies == false) {
			return false;
		}

		final IPrimitiveType<?> status = myCtx.newTerser().getSingleValueOrNull(theSubscription, BaseSubscriptionInterceptor.SUBSCRIPTION_STATUS, IPrimitiveType.class);
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
		if (mySubscriptionInterceptor.hasSubscription(theSubscription.getIdElement()) != null) {
			ourLog.info("Removing {} subscription {}", theStatusString, theSubscription.getIdElement().toUnqualified().getValue());
			mySubscriptionInterceptor.unregisterSubscription(theSubscription.getIdElement());
			return true;
		}
		return false;
	}

	private boolean activateSubscription(String theActiveStatus, final IBaseResource theSubscription, String theRequestedStatus) {
		IBaseResource subscription = mySubscriptionDao.read(theSubscription.getIdElement());

		ourLog.info("Activating subscription {} from status {} to {} for channel {}", subscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus, myChannelType);
		try {
			SubscriptionUtil.setStatus(myCtx, subscription, theActiveStatus);
			subscription = mySubscriptionDao.update(subscription).getResource();
			mySubscriptionInterceptor.submitResourceModifiedForUpdate(subscription);
			return true;
		} catch (final UnprocessableEntityException e) {
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myCtx, subscription, "error");
			SubscriptionUtil.setReason(myCtx, subscription, e.getMessage());
			mySubscriptionDao.update(subscription);
			return false;
		}

	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	public void handleMessage(RestOperationTypeEnum theOperationType, IIdType theId, final IBaseResource theSubscription) throws MessagingException {

		switch (theOperationType) {
			case DELETE:
				mySubscriptionInterceptor.unregisterSubscription(theId);
				return;
			case CREATE:
			case UPDATE:
				if (!theId.getResourceType().equals("Subscription")) {
					return;
				}
				activateAndRegisterSubscriptionIfRequiredInTransaction(theSubscription);
				break;
			default:
				break;
		}

	}

	private synchronized void activateAndRegisterSubscriptionIfRequiredInTransaction(IBaseResource theSubscription) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				activateOrRegisterSubscriptionIfRequired(theSubscription);
			}
		});
	}

	protected boolean registerSubscriptionUnlessAlreadyRegistered(IBaseResource theSubscription) {
		CanonicalSubscription existingSubscription = mySubscriptionInterceptor.hasSubscription(theSubscription.getIdElement());
		CanonicalSubscription newSubscription = mySubscriptionInterceptor.canonicalize(theSubscription);

		if (existingSubscription != null) {
			if (newSubscription.equals(existingSubscription)) {
				// No changes
				return false;
			}
		}

		if (existingSubscription != null) {
			ourLog.info("Updating already-registered active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
			mySubscriptionInterceptor.unregisterSubscription(theSubscription.getIdElement());
		} else {
			ourLog.info("Registering active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
		}
		mySubscriptionInterceptor.registerSubscription(theSubscription.getIdElement(), theSubscription);
		return true;
	}

	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
