package ca.uhn.fhir.rest.server.interceptor;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.method.RequestDetails;

public interface IServerOperationInterceptor {

	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being deleted
	 */
	void resourceDeleted(RequestDetails theRequest, IBaseResource theResource);
	
	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being created
	 */
	void resourceCreated(RequestDetails theRequest, IBaseResource theResource);

	/**
	 * User code may call this method to indicate to an interceptor that
	 * a resource is being updated
	 */
	void resourceUpdated(RequestDetails theRequest, IBaseResource theResource);

}
