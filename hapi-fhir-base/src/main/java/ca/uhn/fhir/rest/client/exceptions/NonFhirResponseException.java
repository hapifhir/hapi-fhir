package ca.uhn.fhir.rest.client.exceptions;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public class NonFhirResponseException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;
	private final String myContentType;
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
		super(theStatusCode, theMessage);
		myContentType = theContentType;
		myResponseText = theResponseText;
	}

	public String getContentType() {
		return myContentType;
	}

	public String getResponseText() {
		return myResponseText;
	}

}
