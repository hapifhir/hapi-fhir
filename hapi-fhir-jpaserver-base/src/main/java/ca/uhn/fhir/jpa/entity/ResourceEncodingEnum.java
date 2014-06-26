package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public enum ResourceEncodingEnum {

	/** Json */
	JSON,
	
	/** Json Compressed */
	JSONC;

	public IParser newParser(FhirContext theContext) {
		return theContext.newJsonParser();
	}
	
}
