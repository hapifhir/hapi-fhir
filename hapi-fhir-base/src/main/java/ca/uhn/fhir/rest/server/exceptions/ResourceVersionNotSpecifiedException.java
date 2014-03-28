package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.rest.server.Constants;

/**
 * Thrown for an Update operation if that operation requires a version to 
 * be specified in an HTTP header, and none was.
 */
public class ResourceVersionNotSpecifiedException extends BaseServerResponseException {
	private static final long serialVersionUID = 1L;

	public ResourceVersionNotSpecifiedException(String error) {
		super(Constants.STATUS_HTTP_412_PRECONDITION_FAILED, error);
	}
}
