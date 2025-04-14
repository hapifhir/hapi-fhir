/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2025 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription.api;

import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionInactiveException;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.hl7.fhir.instance.model.api.IIdType;

public interface ISubscriptionDeliveryValidator {
	/**
	 * Validate the subscription exists and is ACTIVE (or REQUESTED). If not, throw a {@link SubscriptionInactiveException}
	 *
	 * @param theSubscriptionId the id of the subscription delivering the payload
	 * @param thePayload the payload being delivered
	 * @throws SubscriptionInactiveException if the subscription is not active
	 */
	void validate(IIdType theSubscriptionId, ResourceDeliveryMessage thePayload);
}
