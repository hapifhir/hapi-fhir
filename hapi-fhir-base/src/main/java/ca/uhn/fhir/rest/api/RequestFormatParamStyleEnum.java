package ca.uhn.fhir.rest.api;

public enum RequestFormatParamStyleEnum {
	/**
	 * Do not include a _format parameter on requests
	 */
	NONE,

	/**
	 * "xml" or "json"
	 */
	SHORT

}
