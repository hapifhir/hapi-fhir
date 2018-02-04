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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
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

	public void activateAndRegisterSubscriptionIfRequired(final IBaseResource theSubscription) {
		boolean subscriptionTypeApplies = BaseSubscriptionSubscriber.subscriptionTypeApplies(myCtx, theSubscription, myChannelType);
		if (subscriptionTypeApplies == false) {
			return;
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
			} else {
				activateSubscription(activeStatus, theSubscription, requestedStatus);
			}
		} else if (activeStatus.equals(statusString)) {
			if (!mySubscriptionInterceptor.hasSubscription(theSubscription.getIdElement())) {
				ourLog.info("Registering active subscription {}", theSubscription.getIdElement().toUnqualified().getValue());
			}
			mySubscriptionInterceptor.registerSubscription(theSubscription.getIdElement(), theSubscription);
		} else {
			if (mySubscriptionInterceptor.hasSubscription(theSubscription.getIdElement())) {
				ourLog.info("Removing {} subscription {}", statusString, theSubscription.getIdElement().toUnqualified().getValue());
			}
			mySubscriptionInterceptor.unregisterSubscription(theSubscription.getIdElement());
		}
	}

	private void activateSubscription(String theActiveStatus, final IBaseResource theSubscription, String theRequestedStatus) {
		IBaseResource subscription = mySubscriptionDao.read(theSubscription.getIdElement());

		ourLog.info("Activating and registering subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus);
		try {
			SubscriptionUtil.setStatus(myCtx, subscription, theActiveStatus);
			mySubscriptionDao.update(subscription);
			mySubscriptionInterceptor.registerSubscription(subscription.getIdElement(), subscription);
		} catch (final UnprocessableEntityException e) {
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myCtx, subscription, "error");
			SubscriptionUtil.setReason(myCtx, subscription, e.getMessage());
			mySubscriptionDao.update(subscription);
		}
	}

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
				TransactionTemplate txTemplate = new TransactionTemplate(myTransactionManager);
				txTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						activateAndRegisterSubscriptionIfRequired(theSubscription);
					}
				});
				break;
			default:
				break;
		}

	}

	@VisibleForTesting
	public static void setWaitForSubscriptionActivationSynchronouslyForUnitTest(boolean theWaitForSubscriptionActivationSynchronouslyForUnitTest) {
		ourWaitForSubscriptionActivationSynchronouslyForUnitTest = theWaitForSubscriptionActivationSynchronouslyForUnitTest;
	}

}
