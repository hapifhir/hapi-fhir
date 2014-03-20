package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.primitive.IdDt;

public class ResourceNotFoundException extends BaseServerResponseException {

	public ResourceNotFoundException(IdDt theId) {
		super(404, "Resource " + (theId != null ? theId.getValue() : "") + " is not known");
	}

	public ResourceNotFoundException(Class<? extends IResource> theClass, IdentifierDt thePatientId) {
		super(404, "Resource of type " + theClass.getSimpleName() + " with ID " + thePatientId + " is not known");
	}

	private static final long serialVersionUID = 1L;

}
