package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 412 Precondition Failed</b> response. This exception
 * should be thrown for an {@link Update} operation if that operation requires a version to 
 * be specified in an HTTP header, and none was.
 */
@SuppressWarnings("deprecation")
@CoverageIgnore
public class PreconditionFailedException extends ResourceVersionNotSpecifiedException {
	@SuppressWarnings("hiding")
	public static final int STATUS_CODE = Constants.STATUS_HTTP_412_PRECONDITION_FAILED;
	private static final long serialVersionUID = 1L;

	public PreconditionFailedException(String error) {
		super(STATUS_CODE, error);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public PreconditionFailedException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

}
