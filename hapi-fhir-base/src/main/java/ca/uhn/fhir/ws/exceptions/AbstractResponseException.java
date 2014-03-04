package ca.uhn.fhir.ws.exceptions;

public abstract class AbstractResponseException extends Exception {

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
	public AbstractResponseException(int theStatusCode, String theMessage) {
		super(theMessage);
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
	public AbstractResponseException(int theStatusCode, Throwable theCause) {
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
