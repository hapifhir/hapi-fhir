package ca.uhn.fhir.rest.client.exceptions;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * This exception will be thrown by FHIR clients if the client attempts to
 * communicate with a server which is a valid FHIR server but is incompatible
 * with this client for some reason.
 */
@CoverageIgnore
public class FhirClientInappropriateForServerException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public FhirClientInappropriateForServerException(Throwable theCause) {
		super(0, theCause);
	}

	public FhirClientInappropriateForServerException(String theMessage, Throwable theCause) {
		super(0, theMessage, theCause);
	}

	public FhirClientInappropriateForServerException(String theMessage) {
		super(0, theMessage);
	}

}
