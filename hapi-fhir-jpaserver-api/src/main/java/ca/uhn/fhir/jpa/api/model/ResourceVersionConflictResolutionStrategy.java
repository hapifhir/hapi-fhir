package ca.uhn.fhir.jpa.api.model;

/**
 *
 *
 * @since 5.1.0
 */
public class ResourceVersionConflictResolutionStrategy {

	public int getRetryUpToMillis() {
		return myRetryUpToMillis;
	}

	public void setRetryUpToMillis(int theRetryUpToMillis) {
		myRetryUpToMillis = theRetryUpToMillis;
	}

	private int myRetryUpToMillis;

}
