package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 404 Resource Not Found</b> response, which means that the request is pointing to a resource that does not exist.
 */
@CoverageIgnore
public class ResourceNotFoundException extends BaseServerResponseException {

	private static final long serialVersionUID = 1L;

	public static final int STATUS_CODE = Constants.STATUS_HTTP_404_NOT_FOUND;

	public ResourceNotFoundException(Class<? extends IResource> theClass, IdDt theId) {
		super(STATUS_CODE, createErrorMessage(theClass, theId));
	}

	public ResourceNotFoundException(Class<? extends IResource> theClass, IdDt theId, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, createErrorMessage(theClass, theId), theOperationOutcome);
	}

	public ResourceNotFoundException(Class<? extends IResource> theClass, IIdType theId) {
		super(STATUS_CODE, createErrorMessage(theClass, theId));
	}

	public ResourceNotFoundException(Class<? extends IResource> theClass, IIdType theId, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, createErrorMessage(theClass, theId), theOperationOutcome);
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceNotFoundException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	/**
	 * @deprecated This doesn't make sense, since an identifier is not a resource ID and shouldn't generate a 404 if it isn't found - Should be removed
	 */
	@Deprecated
	public ResourceNotFoundException(Class<? extends IResource> theClass, BaseIdentifierDt theId) {
		super(STATUS_CODE, "Resource of type " + theClass.getSimpleName() + " with ID " + theId + " is not known");
	}

	public ResourceNotFoundException(IdDt theId) {
		super(STATUS_CODE, createErrorMessage(theId));
	}

	public ResourceNotFoundException(IIdType theId) {
		super(STATUS_CODE, createErrorMessage(theId));
	}

	public ResourceNotFoundException(IdDt theId, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, createErrorMessage(theId), theOperationOutcome);
	}

	public ResourceNotFoundException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	private static String createErrorMessage(Class<? extends IResource> theClass, IIdType theId) {
		return "Resource of type " + theClass.getSimpleName() + " with ID " + theId + " is not known";
	}

	private static String createErrorMessage(IIdType theId) {
		return "Resource " + (theId != null ? theId.getValue() : "") + " is not known";
	}

}
