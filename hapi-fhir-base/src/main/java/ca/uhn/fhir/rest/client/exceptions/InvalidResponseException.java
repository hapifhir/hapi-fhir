package ca.uhn.fhir.rest.client.exceptions;

public class InvalidResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 */
	public InvalidResponseException(String theMessage) {
		super(theMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public InvalidResponseException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	/**
	 * Constructor
	 * 
	 * @param theCause
	 *            The underlying cause exception
	 */
	public InvalidResponseException(Throwable theCause) {
		super(theCause.toString(), theCause);
	}

}
