package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;

public enum ServerValidationModeEnum {

	/**
	 * Do not validate the server's conformance statement before attempting to 
	 * call it.
	 */
	NEVER,
	
	/**
	 * Validate the server's conformance statement once (per base URL) and cache the
	 * results for the lifetime of the {@link FhirContext}
	 */
	ONCE
	
}
