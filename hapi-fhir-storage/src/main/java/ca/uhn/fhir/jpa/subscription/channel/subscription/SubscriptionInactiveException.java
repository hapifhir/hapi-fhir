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

public class SubscriptionInactiveException extends RuntimeException {
	public SubscriptionInactiveException(String theMessage) {
		super(theMessage);
	}
}
