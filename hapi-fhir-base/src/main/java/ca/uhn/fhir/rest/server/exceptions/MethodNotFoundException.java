package ca.uhn.fhir.rest.server.exceptions;

/**
 * Created by dsotnikov on 2/27/2014.
 */
public class MethodNotFoundException extends BaseServerResponseException {
	private static final long serialVersionUID = 1L;

	public MethodNotFoundException(String error) {
		super(404, error);
	}
}
