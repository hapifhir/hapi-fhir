package ca.uhn.fhir.to.model;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;

import java.io.IOException;

public class BufferResponseInterceptor implements IClientInterceptor {

	@Override
	public void interceptRequest(IHttpRequest theRequest) {
		// nothing
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException {
		theResponse.bufferEntity();
	}
}
