package ca.uhn.fhir.validation;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

public interface IResourceLoader {

	/**
	 * Load the latest version of a given resource
	 * 
	 * @param theType
	 *           The type of the resource to load
	 * @param theId
	 *           The ID of the resource to load
	 * @throws ResourceNotFoundException
	 *            If the resource is not known
	 */
	public <T extends IBaseResource> T load(Class<T> theType, IIdType theId) throws ResourceNotFoundException;

}
