package ca.uhn.fhir.context.support.support;

public enum IssueSeverity {
	/**
	 * The issue caused the action to fail, and no further checking could be performed.
	 */
	FATAL,
	/**
	 * The issue is sufficiently important to cause the action to fail.
	 */
	ERROR,
	/**
	 * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired.
	 */
	WARNING,
	/**
	 * The issue has no relation to the degree of success of the action.
	 */
	INFORMATION
}
