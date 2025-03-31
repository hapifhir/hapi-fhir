package ca.uhn.fhir.broker.api;

public class BrokerConsumerClosedException extends RuntimeException {
	public BrokerConsumerClosedException(String theErrorMessage) {
		super(theErrorMessage);
	}
}
