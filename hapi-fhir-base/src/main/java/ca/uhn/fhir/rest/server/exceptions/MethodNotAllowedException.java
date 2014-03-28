package ca.uhn.fhir.rest.server.exceptions;

/**
 * Created by dsotnikov on 2/27/2014.
 */
public class MethodNotAllowedException extends BaseServerResponseException {
	private static final long serialVersionUID = 1L;

	public MethodNotAllowedException(String error) {
		super(405, error);
	}
}
