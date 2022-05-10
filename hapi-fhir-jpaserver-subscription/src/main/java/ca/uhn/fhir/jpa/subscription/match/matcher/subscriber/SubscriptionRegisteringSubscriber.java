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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
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
public class SubscriptionRegisteringSubscriber extends BaseSubscriberForSubscriptionResources implements MessageHandler {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegisteringSubscriber.class);
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;
	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public SubscriptionRegisteringSubscriber() {
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
			case MANUALLY_TRIGGERED:
			case TRANSACTION:
				return;
			case CREATE:
			case UPDATE:
			case DELETE:
				break;
		}

		// We read the resource back from the DB instead of using the supplied copy for
		// two reasons:
		// - in order to store partition id in the userdata of the resource for partitioned subscriptions
		// - in case we're processing out of order and a create-then-delete has been processed backwards (or vice versa)

		IBaseResource payloadResource;
		IIdType payloadId = payload.getPayloadId(myFhirContext).toUnqualifiedVersionless();
		try {
			IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
			RequestDetails systemRequestDetails = new SystemRequestDetails().setRequestPartitionId(payload.getPartitionId());
			payloadResource = subscriptionDao.read(payloadId, systemRequestDetails);
			if (payloadResource == null) {
				// Only for unit test
				payloadResource = payload.getPayload(myFhirContext);
			}
		} catch (ResourceGoneException e) {
			mySubscriptionRegistry.unregisterSubscriptionIfRegistered(payloadId.getIdPart());
			return;
		}

		String statusString = mySubscriptionCanonicalizer.getSubscriptionStatus(payloadResource);
		if ("active".equals(statusString)) {
			mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(payloadResource);
		} else {
			mySubscriptionRegistry.unregisterSubscriptionIfRegistered(payloadId.getIdPart());
		}

	}

}
