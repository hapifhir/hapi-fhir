package ca.uhn.fhir.jpa.subscription.submit.interceptor.validation;

import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;

public interface IChannelTypeValidator {

	void validateChannelType(CanonicalSubscription theSubscription);

	CanonicalSubscriptionChannelType getSubscriptionChannelType();

}
