package ca.uhn.fhir.rest.client.exceptions;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents a failure by the HAPI FHIR Client to successfully communicate
 * with a FHIR server, because of IO failures, incomprehensible response, etc.
 */
@CoverageIgnore
public class FhirClientConnectionException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public FhirClientConnectionException(Throwable theCause) {
		super(0, theCause);
	}

	public FhirClientConnectionException(String theMessage, Throwable theCause) {
		super(0, theMessage, theCause);
	}

	public FhirClientConnectionException(String theMessage) {
		super(0, theMessage);
	}

}
