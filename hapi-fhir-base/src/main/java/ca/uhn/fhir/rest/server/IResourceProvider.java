package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.IResource;

public interface IResourceProvider {

	/**
	 * Returns the type of resource returned by this provider
	 * 
	 * @return Returns the type of resource returned by this provider
	 */
	Class<? extends IResource> getResourceType();

}
