package ca.uhn.fhir.jpa.subscription;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@SuppressWarnings("unchecked")
public class SubscriptionActivatingSubscriber {
	private final IFhirResourceDao mySubscriptionDao;
	private final BaseSubscriptionInterceptor mySubscriptionInterceptor;
	private final PlatformTransactionManager myTransactionManager;
	private Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);
	private FhirContext myCtx;
	private Subscription.SubscriptionChannelType myChannelType;

	/**
	 * Constructor
	 */
	public SubscriptionActivatingSubscriber(IFhirResourceDao<? extends IBaseResource> theSubscriptionDao, Subscription.SubscriptionChannelType theChannelType, BaseSubscriptionInterceptor theSubscriptionInterceptor, PlatformTransactionManager theTransactionManager) {
		mySubscriptionDao = theSubscriptionDao;
		mySubscriptionInterceptor = theSubscriptionInterceptor;
		myChannelType = theChannelType;
		myCtx = theSubscriptionDao.getContext();
		myTransactionManager = theTransactionManager;
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
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						activateSubscription(status, activeStatus, theSubscription, requestedStatus);
					}
				});
			} else {
				activateSubscription(status, activeStatus, theSubscription, requestedStatus);
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

	private void activateSubscription(IPrimitiveType<?> theStatus, String theActiveStatus, IBaseResource theSubscription, String theRequestedStatus) {
		theStatus.setValueAsString(theActiveStatus);
		ourLog.info("Activating and registering subscription {} from status {} to {}", theSubscription.getIdElement().toUnqualified().getValue(), theRequestedStatus, theActiveStatus);
		mySubscriptionDao.update(theSubscription);
		mySubscriptionInterceptor.registerSubscription(theSubscription.getIdElement(), theSubscription);
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

}
