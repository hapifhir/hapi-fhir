package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;

public interface ISubscriptionDeliveryChannelNamer {
	String nameFromSubscription(CanonicalSubscription theCanonicalSubscription);
}
