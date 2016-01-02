package ca.uhn.fhir.rest.server.exceptions;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.base.composite.BaseIdentifierDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.CoverageIgnore;

/**
 * Represents an <b>HTTP 410 Resource Gone</b> response, which geenerally
 * indicates that the resource has been deleted
 */
@CoverageIgnore
public class ResourceGoneException extends BaseServerResponseException {

	public static final int STATUS_CODE = Constants.STATUS_HTTP_410_GONE;

	public ResourceGoneException(IdDt theId) {
		super(STATUS_CODE, "Resource " + (theId != null ? theId.getValue() : "") + " is gone/deleted");
	}

	public ResourceGoneException(Class<? extends IResource> theClass, BaseIdentifierDt thePatientId) {
		super(STATUS_CODE, "Resource of type " + theClass.getSimpleName() + " with ID " + thePatientId + " is gone/deleted");
	}

	public ResourceGoneException(Class<? extends IResource> theClass, IdDt thePatientId) {
		super(STATUS_CODE, "Resource of type " + theClass.getSimpleName() + " with ID " + thePatientId + " is gone/deleted");
	}

	/**
	 * Constructor
	 * 
	 * @param theMessage
	 *            The message
	 *  @param theOperationOutcome The OperationOutcome resource to return to the client
	 */
	public ResourceGoneException(String theMessage, IBaseOperationOutcome theOperationOutcome) {
		super(STATUS_CODE, theMessage, theOperationOutcome);
	}

	public ResourceGoneException(String theMessage) {
		super(STATUS_CODE, theMessage);
	}

	private static final long serialVersionUID = 1L;

}
