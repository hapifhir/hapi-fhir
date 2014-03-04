package ca.uhn.fhir.server.exceptions;

/**
 * Created by dsotnikov on 2/27/2014.
 */
public class MethodNotFoundException extends AbstractResponseException {
	private static final long serialVersionUID = 1L;

	public MethodNotFoundException(String error) {
		super(404, error);
	}
}
