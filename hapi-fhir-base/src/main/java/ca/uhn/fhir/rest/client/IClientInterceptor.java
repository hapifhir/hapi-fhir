package ca.uhn.fhir.rest.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public interface IClientInterceptor {

	void interceptRequest(HttpRequestBase theRequest);
	
	void interceptResponse(HttpResponse theRequest);

}
