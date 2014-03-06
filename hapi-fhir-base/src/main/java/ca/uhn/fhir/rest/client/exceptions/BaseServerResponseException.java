package ca.uhn.fhir.rest.client.exceptions;

public abstract class BaseServerResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 */
	public BaseServerResponseException(String theMessage) {
		super(theMessage);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public BaseServerResponseException(String theMessage, Throwable theCause) {
		super(theMessage, theCause);
	}

	/**
	 * Constructor
	 * 
	 * @param theCause
	 *            The underlying cause exception
	 */
	public BaseServerResponseException(Throwable theCause) {
		super(theCause.toString(), theCause);
	}


}
