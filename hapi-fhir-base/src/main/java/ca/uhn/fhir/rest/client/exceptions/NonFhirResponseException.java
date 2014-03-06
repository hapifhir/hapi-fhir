package ca.uhn.fhir.rest.client.exceptions;

public class NonFhirResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;
	private final String myContentType;
	private final int myStatusCode;
	private final String myResponseText;

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 * @param theResponseText 
	 * @param theStatusCode 
	 * @param theContentType 
	 */
	public NonFhirResponseException(String theMessage, String theContentType, int theStatusCode, String theResponseText) {
		super(theMessage);
		myContentType=theContentType;
		myStatusCode=theStatusCode;
		myResponseText=theResponseText;
	}

	public String getContentType() {
		return myContentType;
	}

	public int getStatusCode() {
		return myStatusCode;
	}

	public String getResponseText() {
		return myResponseText;
	}



}
