package ca.uhn.fhir.broker.api;

/**
 * Thrown when a consumer fails to start (likely because it failed to connect to the broker.)
 */
public class ChannelConsumerStartFailureException extends RuntimeException {
	public ChannelConsumerStartFailureException(String theErrorMessage, Exception theException) {
		super(theErrorMessage, theException);
	}
}
