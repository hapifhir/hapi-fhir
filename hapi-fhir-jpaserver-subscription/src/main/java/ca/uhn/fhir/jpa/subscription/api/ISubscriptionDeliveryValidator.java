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

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;

public interface ISubscriptionDeliveryValidator {
	void validate(ActiveSubscription theActiveSubscription, ResourceDeliveryMessage thePayload);
}
