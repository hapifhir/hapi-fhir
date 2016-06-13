package ca.uhn.fhir.rest.method;

import ca.uhn.fhir.rest.annotation.Since;
import ca.uhn.fhir.rest.server.Constants;

class SinceParameter extends SinceOrAtParameter {

	public SinceParameter() {
		super(Constants.PARAM_SINCE, Since.class);
	}

}
