package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.server.Constants;

/**
 * Parameter type for methods annotated with {@link Patch}
 */
public enum PatchTypeEnum {

	JSON_PATCH(Constants.CT_JSON_PATCH), XML_PATCH(Constants.CT_XML_PATCH);

	private final String myContentType;

	PatchTypeEnum(String theContentType) {
		myContentType = theContentType;
	}

	public String getContentType() {
		return myContentType;
	}

}
