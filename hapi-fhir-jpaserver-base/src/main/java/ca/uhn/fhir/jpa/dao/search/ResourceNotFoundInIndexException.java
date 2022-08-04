package ca.uhn.fhir.jpa.dao.search;

import java.io.Serial;

public class ResourceNotFoundInIndexException extends IllegalStateException {
	@Serial
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundInIndexException(Throwable theCause) {
		super(theCause.getMessage(), theCause);
	}

	public ResourceNotFoundInIndexException(String theMessage) {
		super(theMessage);
	}

	public ResourceNotFoundInIndexException(String theString, Throwable theCause) {
		super(theString, theCause);
	}

}
