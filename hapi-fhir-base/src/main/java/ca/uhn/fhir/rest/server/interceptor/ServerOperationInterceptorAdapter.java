package ca.uhn.fhir.rest.server.interceptor;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.method.RequestDetails;

/**
 * NOP implementation of {@link IServerOperationInterceptor}
 */
public class ServerOperationInterceptorAdapter extends InterceptorAdapter implements IServerOperationInterceptor {

	@Override
	public void resourceDeleted(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourceCreated(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

	@Override
	public void resourceUpdated(RequestDetails theRequest, IBaseResource theResource) {
		// nothing
	}

}
