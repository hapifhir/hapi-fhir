package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class JpaServerInterceptorAdapter extends InterceptorAdapter implements IJpaServerInterceptor {

	@Override
	public void resourceCreated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
		// nothing
	}

	@Override
	public void resourceUpdated(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
		// nothing
	}

	@Override
	public void resourceDeleted(ActionRequestDetails theDetails, ResourceTable theResourceTable) {
		// nothing
	}

}
