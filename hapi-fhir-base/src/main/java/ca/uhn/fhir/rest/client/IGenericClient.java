package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.IdDt;

public interface IGenericClient {

	/**
	 * Implementation of the "read" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @return The resource
	 */
	<T extends IResource> T read(Class<T> theType, IdDt theId);

	/**
	 * Implementation of the "vread" method.
	 * 
	 * @param theType The type of resource to load
	 * @param theId The ID to load
	 * @param theVersionId The version ID
	 * @return The resource
	 */
	<T extends IResource> T vread(Class<T> theType, IdDt theId, IdDt theVersionId);

}
