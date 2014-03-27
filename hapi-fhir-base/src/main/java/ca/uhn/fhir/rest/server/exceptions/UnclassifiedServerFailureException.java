package ca.uhn.fhir.rest.server.exceptions;

/**
 * Exception for use when a response is received or being sent that
 * does not correspond to any other exception type
 */
public class UnclassifiedServerFailureException extends BaseServerResponseException {

	public UnclassifiedServerFailureException(int theStatusCode, String theMessage) {
		super(theStatusCode, theMessage);
	}

	private static final long serialVersionUID = 1L;

}
