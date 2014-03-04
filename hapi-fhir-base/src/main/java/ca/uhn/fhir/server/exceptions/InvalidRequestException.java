package ca.uhn.fhir.server.exceptions;

public class InvalidRequestException extends AbstractResponseException {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String theMessage) {
		super(400, theMessage);
	}

}
