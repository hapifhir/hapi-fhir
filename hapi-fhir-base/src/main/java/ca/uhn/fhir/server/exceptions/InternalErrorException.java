package ca.uhn.fhir.server.exceptions;

public class InternalErrorException extends AbstractResponseException {

	private static final long serialVersionUID = 1L;

	public InternalErrorException(String theMessage) {
		super(500, theMessage);
	}

	public InternalErrorException(Throwable theCause) {
		super(500, theCause);
	}

}
