package ca.uhn.fhir.rest.client.interceptor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.rest.client.IClientInterceptor;

/**
 * Client interceptor which simply captures request and response objects and stored them so that they can be inspected after a client
 * call has returned
 */
public class CapturingInterceptor implements IClientInterceptor {

	private HttpRequestBase myLastRequest;
	private HttpResponse myLastResponse;

	/**
	 * Clear the last request and response values
	 */
	public void clear() {
		myLastRequest = null;
		myLastResponse = null;
	}

	public HttpRequestBase getLastRequest() {
		return myLastRequest;
	}

	public HttpResponse getLastResponse() {
		return myLastResponse;
	}

	@Override
	public void interceptRequest(HttpRequestBase theRequest) {
		myLastRequest = theRequest;
	}

	@Override
	public void interceptResponse(HttpResponse theRequest) {
		myLastResponse = theRequest;
	}

}