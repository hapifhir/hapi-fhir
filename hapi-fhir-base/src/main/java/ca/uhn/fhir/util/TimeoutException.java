package ca.uhn.fhir.util;

public class TimeoutException extends RuntimeException {
	public TimeoutException(String theMessage) {
		super(theMessage);
	}
}
