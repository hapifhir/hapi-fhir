package ca.uhn.fhir.rest.server.exceptions;

public abstract class BaseServerResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int myStatusCode;

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage) {
		super(theMessage);
		myStatusCode = theStatusCode;
	}

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theMessage
	 *            The message
	 * @param theCause The cause
	 */
	public BaseServerResponseException(int theStatusCode, String theMessage, Throwable theCause) {
		super(theMessage, theCause);
		myStatusCode = theStatusCode;
	}

	/**
	 * Constructor
	 * 
	 * @param theStatusCode
	 *            The HTTP status code corresponding to this problem
	 * @param theCause
	 *            The underlying cause exception
	 */
	public BaseServerResponseException(int theStatusCode, Throwable theCause) {
		super(theCause.toString(), theCause);
		myStatusCode = theStatusCode;
	}

	/**
	 * Returns the HTTP status code corresponding to this problem
	 */
	public int getStatusCode() {
		return myStatusCode;
	}

}
