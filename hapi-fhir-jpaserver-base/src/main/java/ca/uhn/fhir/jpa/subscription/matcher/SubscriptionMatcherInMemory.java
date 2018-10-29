package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.jpa.subscription.ResourceModifiedMessage;

public class SubscriptionMatcherInMemory implements ISubscriptionMatcher {

	@Override
	public boolean match(String criteria, ResourceModifiedMessage msg) {
		// FIXME KHS implement
		return true;
	}
}
