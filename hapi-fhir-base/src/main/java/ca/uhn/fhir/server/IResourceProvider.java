package ca.uhn.fhir.server;

import ca.uhn.fhir.model.api.IResource;

public interface IResourceProvider<T extends IResource> {

	/**
	 * Returns the type of resource returned by this provider
	 * 
	 * @return Returns the type of resource returned by this provider
	 */
	Class<T> getResourceType();

	/**
	 * Retrieve the resource by its identifier
	 * 
	 * @param theId
	 *            The resource identity
	 * @return The resource
	 */
	T getResourceById(long theId);

}
