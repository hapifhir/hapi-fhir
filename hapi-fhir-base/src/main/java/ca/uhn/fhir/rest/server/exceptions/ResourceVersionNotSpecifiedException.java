package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * @deprecated Use {@link PreconditionFailedException} instead - This exception is
 * strangely named and will be removed at some point.
 */
@Deprecated
@CoverageIgnore
public class ResourceVersionNotSpecifiedException extends BaseServerResponseException {
	public static final int STATUS_CODE = Constants.STATUS_HTTP_412_PRECONDITION_FAILED;
	private static final long serialVersionUID = 1L;

	public ResourceVersionNotSpecifiedException(String error) {
		super(STATUS_CODE, error);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceVersionNotSpecifiedException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	public ResourceVersionNotSpecifiedException(int theStatusCode, String error) {
		super(theStatusCode, error);
	}
	
	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceVersionNotSpecifiedException(int theStatusCode, String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(theStatusCode, theMessage, theOperationOutcome);
	}

}
