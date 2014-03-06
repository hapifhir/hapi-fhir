package ca.uhn.fhir.rest.server.exceptions;

public class InvalidRequestException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String theMessage) {
		super(400, theMessage);
	}

}
