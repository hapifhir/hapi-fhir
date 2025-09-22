package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.rest.client.api.IHttpRequest;

public interface IRawHttp {
	Object execute();
	IRawHttp fromRequest(IHttpRequest theRequest);
}
