package org.hl7.fhir.exceptions;

public class FHIRException extends Exception {

	public FHIRException() {
		super();
	}

	public FHIRException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FHIRException(String message, Throwable cause) {
		super(message, cause);
	}

	public FHIRException(String message) {
		super(message);
	}

	public FHIRException(Throwable cause) {
		super(cause);
	}

}
