package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;

public interface ISubscriptionMatcher {
	SubscriptionMatchResult match(String criteria, ResourceModifiedMessage msg);
}
