package ca.uhn.fhir.jpa.term;

public class SaveAllDeferredTimeoutException extends RuntimeException {
	public SaveAllDeferredTimeoutException(String theMessage) {
		super(theMessage);
	}
}
