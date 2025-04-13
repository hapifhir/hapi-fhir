/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2025 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionCanonicalizer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
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
	public void validate(ActiveSubscription theActiveSubscription, ResourceDeliveryMessage theResourceDeliveryMessage) {
		String subscriptionId = theActiveSubscription.getSubscription().getIdElementString();
		ourLog.debug("Retrieving subscription {}", subscriptionId);
		IBaseResource resource;
		try {
			SystemRequestDetails systemRequestDetails = new SystemRequestDetails();
			systemRequestDetails.setRequestPartitionId(theResourceDeliveryMessage.getRequestPartitionId());
			resource = myDaoRegistry.getSubscriptionDao().read(new IdDt(subscriptionId), systemRequestDetails);
		} catch (ResourceNotFoundException e) {
			throw new SubscriptionInactiveException("Attempting to deliver " + theResourceDeliveryMessage.getPayloadId()
					+ " to deleted subscription " + subscriptionId + ".  Aborting delivery.");
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
				throw new SubscriptionInactiveException("Attempting to deliver "
						+ theResourceDeliveryMessage.getPayloadId() + " to disabled subscription "
						+ subscription.getIdElementString() + ".  Aborting delivery.");
		}
	}
}
