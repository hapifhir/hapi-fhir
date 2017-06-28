package ca.uhn.fhir.rest.client.interceptor;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

/**
 * Client interceptor which simply captures request and response objects and stores them so that they can be inspected after a client
 * call has returned
 */
public class CapturingInterceptor implements IClientInterceptor {

	private IHttpRequest myLastRequest;
	private IHttpResponse myLastResponse;

	/**
	 * Clear the last request and response values
	 */
	public void clear() {
		myLastRequest = null;
		myLastResponse = null;
	}

	public IHttpRequest getLastRequest() {
		return myLastRequest;
	}

	public IHttpResponse getLastResponse() {
		return myLastResponse;
	}

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		myLastRequest = theRequest;
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) {
		//Buffer the reponse to avoid errors when content has already been read and the entity is not repeatable
		try {
			if(theResponse.getResponse() instanceof HttpResponse) {
				HttpEntity entity = ((HttpResponse) theResponse.getResponse()).getEntity();
				if( entity != null && !entity.isRepeatable()){
					theResponse.bufferEntity();
				}
			} else {
				theResponse.bufferEntity();
			}
		} catch (IOException e) {
			throw new InternalErrorException("Unable to buffer the entity for capturing", e);
		}


		myLastResponse = theResponse;
	}

}
