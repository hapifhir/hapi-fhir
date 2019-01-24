package ca.uhn.fhir.jpa.subscription.module.matcher;

public enum SubscriptionMatchingStrategy {
	/**
	 * Resources can be matched against this subcription in-memory without needing to make a call out to a FHIR Repository
	 */
	IN_MEMORY,

	/**
	 * Resources cannot be matched against this subscription in-memory.  We need to make a call to a FHIR Repository to determine a match
	 */
	DATABASE
}

