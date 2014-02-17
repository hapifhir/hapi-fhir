package ca.uhn.fhir.model.resource;

import ca.uhn.fhir.model.api.ResourceElement;
import ca.uhn.fhir.model.datatype.IdentifierDt;

/**
 * Classes extending this class must be annotated with 
 */
public class BaseResourceWithIdentifier extends BaseResource {

	@ResourceElement(name="identifier", order=ResourceElement.ORDER_UNKNOWN)
	private IdentifierDt myIdentifier;
	
}
