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

/**
 * Thrown when retrying a subscription message delivery and the subscription has been disabled.
 */
public class SubscriptionInactiveException extends RuntimeException {
	public SubscriptionInactiveException(String theMessage) {
		super(theMessage);
	}
}
