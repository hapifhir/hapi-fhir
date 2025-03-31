package ca.uhn.fhir.broker.api;

// FIXME KHS javadoc on all the new exceptions
public class BrokerListenerClosedException extends RuntimeException {
	public BrokerListenerClosedException(String theErrorMessage) {
		super(theErrorMessage);
	}
}
