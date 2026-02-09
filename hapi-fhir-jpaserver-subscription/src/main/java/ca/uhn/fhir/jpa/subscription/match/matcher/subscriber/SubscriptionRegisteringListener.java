/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.match.matcher.subscriber;

import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Responsible for transitioning subscription resources from REQUESTED to ACTIVE
 * Once activated, the subscription is added to the SubscriptionRegistry.
 * <p>
 * Also validates criteria.  If invalid, rejects the subscription without persisting the subscription.
 */
public class SubscriptionRegisteringListener implements IMessageListener<ResourceModifiedMessage> {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionRegisteringListener.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private SubscriptionRegistry mySubscriptionRegistry;

	@Autowired
	private SubscriptionCanonicalizer mySubscriptionCanonicalizer;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired(required = false)
	private PartitionSettings myPartitionSettings;

	/**
	 * Constructor
	 */
	public SubscriptionRegisteringListener() {
		super();
	}

	public Class<ResourceModifiedMessage> getPayloadType() {
		return ResourceModifiedMessage.class;
	}

	@Override
	public void handleMessage(@Nonnull IMessage<ResourceModifiedMessage> theMessage) {
		ResourceModifiedMessage payload = theMessage.getPayload();

		if (!payload.hasResourceType(this.myFhirContext, "Subscription")) {
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

		IIdType payloadId = payload.getPayloadId(myFhirContext).toUnqualifiedVersionless();
		IFhirResourceDao<?> subscriptionDao = myDaoRegistry.getResourceDao("Subscription");
		RequestDetails systemRequestDetails = getPartitionAwareRequestDetails(payload);
		IBaseResource payloadResource = subscriptionDao.read(payloadId, systemRequestDetails, true);
		if (payloadResource == null) {
			// Only for unit test
			payloadResource = payload.getResource(myFhirContext);
		}
		if (payloadResource.isDeleted()) {
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

	private Integer getDefaultPartitionId() {
		if (myPartitionSettings != null) {
			return myPartitionSettings.getDefaultPartitionId();
		}
		/*
		 * We log a warning because in most cases you will want a partitionsettings
		 * object.
		 * However, PartitionSettings beans are not provided in the same
		 * config as the one that provides this bean; as such, it is the responsibility
		 * of whomever includes the config for this bean to also provide a PartitionSettings
		 * bean (or import a config that does)
		 */
		ourLog.warn("No PartitionSettings available.");
		return null;
	}

	/**
	 * There were some situations where the RequestDetails attempted to use the default partition
	 * and the partition name was a list containing null values (i.e. using the package installer to STORE_AND_INSTALL
	 * Subscriptions while partitioning was enabled). If any partition matches these criteria,
	 * {@link RequestPartitionId#defaultPartition()} is used to obtain the default partition.
	 */
	private RequestDetails getPartitionAwareRequestDetails(ResourceModifiedMessage payload) {
		Integer defaultPartitionId = getDefaultPartitionId();
		RequestPartitionId payloadPartitionId = payload.getPartitionId();
		if (payloadPartitionId == null || payloadPartitionId.isPartition(defaultPartitionId)) {
			// This may look redundant but the package installer STORE_AND_INSTALL Subscriptions when partitioning is
			// enabled
			// creates a corrupt default partition.  This resets it to a clean one.
			payloadPartitionId = RequestPartitionId.defaultPartition();
		}
		SystemRequestDetails requestDetails = new SystemRequestDetails().setRequestPartitionId(payloadPartitionId);
		// skip consent checking when registering Subscription resources since it is a system action
		ConsentInterceptor.skipAllConsentForRequest(requestDetails);
		return requestDetails;
	}
}
