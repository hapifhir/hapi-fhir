package ca.uhn.fhir.jpa.api.model;

import org.apache.commons.lang3.Validate;

/**
 * @since 5.1.0
 */
public class ResourceVersionConflictResolutionStrategy {

	private int myMaxRetries;
	private boolean myRetry;

	public int getMaxRetries() {
		return myMaxRetries;
	}

	public void setMaxRetries(int theMaxRetries) {
		Validate.isTrue(theMaxRetries >= 0, "theRetryUpToMillis must not be negative");
		myMaxRetries = theMaxRetries;
	}

	public boolean isRetry() {
		return myRetry;
	}

	public void setRetry(boolean theRetry) {
		myRetry = theRetry;
	}
}
