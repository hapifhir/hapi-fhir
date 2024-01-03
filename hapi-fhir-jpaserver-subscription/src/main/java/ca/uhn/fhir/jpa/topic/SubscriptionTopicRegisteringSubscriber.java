/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 * <p>
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
public class SubscriptionTopicRegisteringSubscriber implements MessageHandler {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private SubscriptionTopicRegistry mySubscriptionTopicRegistry;

	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public SubscriptionTopicRegisteringSubscriber() {
		super();
	}

	@Override
	public void handleMessage(@Nonnull Message<?> theMessage) throws MessagingException {
		if (!(theMessage instanceof ResourceModifiedJsonMessage)) {
			ourLog.warn("Received message of unexpected type on matching channel: {}", theMessage);
			return;
		}

		ResourceModifiedMessage payload = ((ResourceModifiedJsonMessage) theMessage).getPayload();

		if (!payload.hasPayloadType(myFhirContext, "SubscriptionTopic")) {
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
			IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("SubscriptionTopic");
			RequestDetails systemRequestDetails = getPartitionAwareRequestDetails(payload);
			payloadResource = subscriptionDao.read(payloadId, systemRequestDetails);
			if (payloadResource == null) {
				// Only for unit test
				payloadResource = payload.getPayload(myFhirContext);
			}
		} catch (ResourceGoneException e) {
			mySubscriptionTopicRegistry.unregister(payloadId.getIdPart());
			return;
		}

		SubscriptionTopic subscriptionTopic =
				SubscriptionTopicCanonicalizer.canonicalizeTopic(myFhirContext, payloadResource);
		if (subscriptionTopic.getStatus() == Enumerations.PublicationStatus.ACTIVE) {
			mySubscriptionTopicRegistry.register(subscriptionTopic);
		} else {
			mySubscriptionTopicRegistry.unregister(payloadId.getIdPart());
		}
	}

	/**
	 * There were some situations where the RequestDetails attempted to use the default partition
	 * and the partition name was a list containing null values (i.e. using the package installer to STORE_AND_INSTALL
	 * Subscriptions while partitioning was enabled). If any partition matches these criteria,
	 * {@link RequestPartitionId#defaultPartition()} is used to obtain the default partition.
	 */
	private RequestDetails getPartitionAwareRequestDetails(ResourceModifiedMessage payload) {
		RequestPartitionId payloadPartitionId = payload.getPartitionId();
		if (payloadPartitionId == null || payloadPartitionId.isDefaultPartition()) {
			// This may look redundant but the package installer STORE_AND_INSTALL Subscriptions when partitioning is
			// enabled
			// creates a corrupt default partition.  This resets it to a clean one.
			payloadPartitionId = RequestPartitionId.defaultPartition();
		}
		return new SystemRequestDetails().setRequestPartitionId(payloadPartitionId);
	}
}
