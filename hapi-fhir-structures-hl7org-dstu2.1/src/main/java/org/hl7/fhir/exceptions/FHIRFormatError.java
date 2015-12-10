package org.hl7.fhir.exceptions;

public class FHIRFormatError extends FHIRException {

	public FHIRFormatError() {
		super();
	}

	public FHIRFormatError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FHIRFormatError(String message, Throwable cause) {
		super(message, cause);
	}

	public FHIRFormatError(String message) {
		super(message);
	}

	public FHIRFormatError(Throwable cause) {
		super(cause);
	}

}
