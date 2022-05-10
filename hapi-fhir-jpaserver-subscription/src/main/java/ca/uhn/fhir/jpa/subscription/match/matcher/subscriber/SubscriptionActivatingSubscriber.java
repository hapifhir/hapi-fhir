package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionConstants;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.SubscriptionUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import javax.annotation.Nonnull;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 * <p>
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
public class SubscriptionActivatingSubscriber extends BaseSubscriberForSubscriptionResources implements MessageHandler {
	private final Logger ourLog = LoggerFactory.getLogger(SubscriptionActivatingSubscriber.class);
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoConfig myDaoConfig;

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
				activateSubscriptionIfRequired(payload.getNewPayload(myFhirContext));
				break;
			case DELETE:
			case MANUALLY_TRIGGERED:
			default:
				break;
		}

	}

	/**
	 * Note: This is synchronized because this is called both by matching channel messages
	 * as well as from Subscription Loader (which periodically refreshes from the DB to make
	 * sure nothing got missed). If these two mechanisms try to activate the same subscription
	 * at the same time they can get a constraint error.
	 */
	public synchronized boolean activateSubscriptionIfRequired(final IBaseResource theSubscription) {
		// Grab the value for "Subscription.channel.type" so we can see if this
		// subscriber applies..
		CanonicalSubscriptionChannelType subscriptionChannelType = mySubscriptionCanonicalizer.getChannelType(theSubscription);

		// Only activate supported subscriptions
		if (subscriptionChannelType == null
				|| !myDaoConfig.getSupportedSubscriptionTypes().contains(subscriptionChannelType.toCanonical())) {
			return false;
		}

		String statusString = mySubscriptionCanonicalizer.getSubscriptionStatus(theSubscription);

		if (SubscriptionConstants.REQUESTED_STATUS.equals(statusString)) {
			return activateSubscription(theSubscription);
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private boolean activateSubscription(final IBaseResource theSubscription) {
		IFhirResourceDao subscriptionDao = myDaoRegistry.getSubscriptionDao();
		SystemRequestDetails srd = SystemRequestDetails.forAllPartitions();

		IBaseResource subscription = null;
		try {
			// read can throw ResourceGoneException
			// if this happens, we will treat this as a failure to activate
			subscription =  subscriptionDao.read(theSubscription.getIdElement(), SystemRequestDetails.forAllPartitions());
			subscription.setId(subscription.getIdElement().toVersionless());

			ourLog.info("Activating subscription {} from status {} to {}", subscription.getIdElement().toUnqualified().getValue(), SubscriptionConstants.REQUESTED_STATUS, SubscriptionConstants.ACTIVE_STATUS);
			SubscriptionUtil.setStatus(myFhirContext, subscription, SubscriptionConstants.ACTIVE_STATUS);
			subscriptionDao.update(subscription, srd);
			return true;
		} catch (final UnprocessableEntityException | ResourceGoneException e) {
			subscription = subscription != null ? subscription : theSubscription;
			ourLog.error("Failed to activate subscription "
				+ subscription.getIdElement()
				+ " : " + e.getMessage());
			ourLog.info("Changing status of {} to ERROR", subscription.getIdElement());
			SubscriptionUtil.setStatus(myFhirContext, subscription, SubscriptionConstants.ERROR_STATUS);
			SubscriptionUtil.setReason(myFhirContext, subscription, e.getMessage());
			subscriptionDao.update(subscription, srd);
			return false;
		}
	}

}
