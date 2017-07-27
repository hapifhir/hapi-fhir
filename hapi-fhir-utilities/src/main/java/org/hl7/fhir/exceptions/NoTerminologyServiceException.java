package org.hl7.fhir.exceptions;

public class NoTerminologyServiceException extends FHIRException {

	public NoTerminologyServiceException() {
		super();
	}

	public NoTerminologyServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoTerminologyServiceException(String message) {
		super(message);
	}

	public NoTerminologyServiceException(Throwable cause) {
		super(cause);
	}

}
