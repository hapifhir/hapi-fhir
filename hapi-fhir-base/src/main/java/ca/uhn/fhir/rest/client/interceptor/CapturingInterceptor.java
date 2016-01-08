package ca.uhn.fhir.rest.client.interceptor;

import ca.uhn.fhir.rest.client.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequestBase;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

/**
 * Client interceptor which simply captures request and response objects and stores them so that they can be inspected after a client
 * call has returned
 */
public class CapturingInterceptor implements IClientInterceptor {

	private IHttpRequestBase myLastRequest;
	private IHttpResponse myLastResponse;

	/**
	 * Clear the last request and response values
	 */
	public void clear() {
		myLastRequest = null;
		myLastResponse = null;
	}

	public IHttpRequestBase getLastRequest() {
		return myLastRequest;
	}

	public IHttpResponse getLastResponse() {
		return myLastResponse;
	}

	@Override
	public void interceptRequest(IHttpRequestBase theRequest) {
		myLastRequest = theRequest;
	}

	@Override
	public void interceptResponse(IHttpResponse theRequest) {
		myLastResponse = theRequest;
	}

}
