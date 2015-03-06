package ca.uhn.fhir.rest.gclient;

import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.model.primitive.IdDt;

public interface IBaseOn<T> {

	/**
	 * Perform the operation across all versions of all resources of all types on the server
	 */
	T onServer();
	
	/**
	 * Perform the operation across all versions of all resources of the given type on the server
	 */
	T onType(Class<? extends IBaseResource> theResourceType);
	
	/**
	 * Perform the operation across all versions of a specific resource (by ID and type) on the server.
	 * Note that <code>theId</code> must be populated with both a resource type and a resource ID at
	 * a minimum.
	 * 
	 * @throws IllegalArgumentException If <code>theId</code> does not contain at least a resource type and ID 
	 */
	T onInstance(IdDt theId);

}
