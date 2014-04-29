package ca.uhn.fhir.rest.client.exceptions;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;


public class FhirClientConnectionException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public FhirClientConnectionException(Throwable theE) {
		super(0, theE);
	}

}
