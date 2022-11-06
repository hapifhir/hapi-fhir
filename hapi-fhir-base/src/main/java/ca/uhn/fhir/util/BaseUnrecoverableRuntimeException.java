package ca.uhn.fhir.util;

/**
 * Exception superclass for an exception representing an unrecoverable failure
 */
public abstract class BaseUnrecoverableRuntimeException extends RuntimeException {
	public BaseUnrecoverableRuntimeException(String theMessage) {
		super(theMessage);
	}

	public BaseUnrecoverableRuntimeException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}
}
