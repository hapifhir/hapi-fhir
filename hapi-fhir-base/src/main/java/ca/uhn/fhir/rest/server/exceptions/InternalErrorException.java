package ca.uhn.fhir.rest.server.exceptions;

public class InternalErrorException extends AbstractResponseException {

	private static final long serialVersionUID = 1L;

	public InternalErrorException(String theMessage) {
		super(500, theMessage);
	}

	public InternalErrorException(String theMessage, Throwable theCause) {
		super(500, theMessage, theCause);
	}

	public InternalErrorException(Throwable theCause) {
		super(500, theCause);
	}

}
