package ca.uhn.fhir.rest.client.impl;

import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;

public abstract class BaseHttpResponse implements IHttpResponse {
	private final StopWatch myRequestStopWatch;

	public BaseHttpResponse(StopWatch theRequestStopWatch) {
		myRequestStopWatch = theRequestStopWatch;
	}

	@Override
	public StopWatch getRequestStopWatch() {
		return myRequestStopWatch;
	}
}
