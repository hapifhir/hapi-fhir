package ca.uhn.fhir.rest.method;

import ca.uhn.fhir.rest.annotation.At;
import ca.uhn.fhir.rest.server.Constants;

class AtParameter extends SinceOrAtParameter {

	public AtParameter() {
		super(Constants.PARAM_AT, At.class);
	}

}
