/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionDeliveryValidator implements ISubscriptionDeliveryValidator {

	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionDeliveryValidator.class);
	private final DaoRegistry myDaoRegistry;
	private final SubscriptionCanonicalizer mySubscriptionCanonicalizer;

	/**
	 * Constructor
	 */
	public SubscriptionDeliveryValidator(
			DaoRegistry theDaoRegistry, SubscriptionCanonicalizer theSubscriptionCanonicalizer) {
		myDaoRegistry = theDaoRegistry;
		mySubscriptionCanonicalizer = theSubscriptionCanonicalizer;
	}

	@Override
	public void validate(IIdType theSubscriptionId, ResourceDeliveryMessage theResourceDeliveryMessage) {
		ourLog.debug("Retrieving subscription {}", theSubscriptionId);
		IBaseResource resource;
		try {
			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
			systemRequestDetails.setRequestPartitionId(theResourceDeliveryMessage.getRequestPartitionId());
			resource = myDaoRegistry.getSubscriptionDao().read(theSubscriptionId, systemRequestDetails);
		} catch (ResourceNotFoundException e) {
			throw new SubscriptionInactiveException(Msg.code(2667) + "Attempting to deliver " + theResourceDeliveryMessage.getPayloadId()
					+ " to deleted subscription " + theSubscriptionId + ".  Aborting delivery.");
		}
		ourLog.debug("Retrieved resource {}", resource.getIdElement());
		CanonicalSubscription subscription = mySubscriptionCanonicalizer.canonicalize(resource);

		switch (subscription.getStatus()) {
			case ACTIVE:
			case REQUESTED:
				return;
			case ERROR:
			case OFF:
			case NULL:
			default:
				throw new SubscriptionInactiveException(Msg.code(2668) + "Attempting to deliver "
						+ theResourceDeliveryMessage.getPayloadId() + " to disabled subscription "
						+ subscription.getIdElementString() + ".  Aborting delivery.");
		}
	}
}
