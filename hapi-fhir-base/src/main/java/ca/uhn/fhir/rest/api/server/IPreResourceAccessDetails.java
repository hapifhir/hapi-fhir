package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This object is an abstraction for a server response that is going to
 * return one or more resources to the user. This can be used by interceptors
 * to make decisions about whether a resource should be visible or not
 * to the user making the request.
 */
public interface IPreResourceAccessDetails {

	int size();

	IBaseResource getResource(int theIndex);

	void setDontReturnResourceAtIndex(int theIndex);

}
