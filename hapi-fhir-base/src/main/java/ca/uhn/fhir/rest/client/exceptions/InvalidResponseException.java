package ca.uhn.fhir.rest.client.exceptions;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class InvalidResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 */
	public InvalidResponseException(int theStatusCode, String theMessage) {
		super(theStatusCode, theMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public InvalidResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theStatusCode, theMessage, theCause);
	}

	/**
	 * Constructor
	 * 
	 * @param theCause
	 *            The underlying cause exception
	 */
	public InvalidResponseException(int theStatusCode, Throwable theCause) {
		super(theStatusCode, theCause.toString(), theCause);
	}

}
