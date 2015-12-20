package org.hl7.fhir.exceptions;

public class FHIRException extends Exception {

	public FHIRException() {
		super();
	}

	// Note: the 4 argument constructor has been removed as it is not 1.6 compatible
//	public FHIRException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
//		super(message, cause, enableSuppression, writableStackTrace);
//	}

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
