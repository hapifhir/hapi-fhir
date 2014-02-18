package ca.uhn.fhir.model.resource;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.datatype.IdentifierDt;

/**
 * Classes extending this class must be annotated with 
 */
public abstract class BaseResourceWithIdentifier extends BaseResource {

	@Child(name="identifier", order=Child.ORDER_UNKNOWN)
	private IdentifierDt myIdentifier;
	
}
