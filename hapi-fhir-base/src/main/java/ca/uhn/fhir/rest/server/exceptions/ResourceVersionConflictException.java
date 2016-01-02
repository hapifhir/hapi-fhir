package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 409 Conflict</b> response. This exception should be 
 * thrown in methods which accept a version (e.g. {@link Update}, {@link Delete}) 
 * when the operation fails because of a version conflict as specified in the FHIR specification. 
 */
@CoverageIgnore
public class ResourceVersionConflictException extends BaseServerResponseException {
	public static final int STATUS_CODE = Constants.STATUS_HTTP_409_CONFLICT;
	private static final long serialVersionUID = 1L;

	public ResourceVersionConflictException(String error) {
		super(STATUS_CODE, error);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceVersionConflictException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

}
