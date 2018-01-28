package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * @see ca.uhn.fhir.rest.server.interceptor.IServerInterceptor
 */
public class ResponseDetails {

	private IBaseResource myResponseResource;
	private int myResponseCode;

	/**
	 * Constructor
	 */
	public ResponseDetails() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResponseDetails(IBaseResource theResponseResource) {
		setResponseResource(theResponseResource);
	}

	public int getResponseCode() {
		return myResponseCode;
	}

	public void setResponseCode(int theResponseCode) {
		myResponseCode = theResponseCode;
	}

	/**
	 * Get the resource which will be returned to the client
	 */
	public IBaseResource getResponseResource() {
		return myResponseResource;
	}

	/**
	 * Set the resource which will be returned to the client
	 */
	public void setResponseResource(IBaseResource theResponseResource) {
		myResponseResource = theResponseResource;
	}

}
