package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.model.primitive.IdDt;

public class ResourceNotFoundException extends BaseServerResponseException {

	public ResourceNotFoundException(IdDt theId) {
		super(404, "Resource " + (theId != null ? theId.getValue() : "") + " is not known");
	}

	private static final long serialVersionUID = 1L;

}
