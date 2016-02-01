package org.hl7.fhir.dstu3.exceptions;

public class FHIRException extends Exception {

	// Note that the 4-argument constructor has been removed as it is not JDK6 compatible
	
	public FHIRException() {
		super();
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
