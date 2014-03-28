package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.rest.server.Constants;

/**
 * Created by dsotnikov on 2/27/2014.
 */
public class ResourceVersionConflictException extends BaseServerResponseException {
	private static final long serialVersionUID = 1L;

	public ResourceVersionConflictException(String error) {
		super(Constants.STATUS_HTTP_409_CONFLICT, error);
	}
}
