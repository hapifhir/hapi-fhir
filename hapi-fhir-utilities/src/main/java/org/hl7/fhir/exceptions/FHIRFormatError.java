package org.hl7.fhir.exceptions;

import ca.uhn.fhir.parser.DataFormatException;

public class FHIRFormatError extends DataFormatException {

	private static final long serialVersionUID = 1L;

	public FHIRFormatError() {
		super();
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
