package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class InterceptorUtil {

	/**
	 * Fires {@link Pointcut#STORAGE_PRESHOW_RESOURCES} interceptor hook, and potentially remove resources
	 * from the resource list
	 */
	public static void fireStoragePreshowResource(List<IBaseResource> theResources, RequestDetails theRequest, IInterceptorBroadcaster theInterceptorBroadcaster) {
		theResources.removeIf(t -> t == null);

		// Interceptor call: STORAGE_PRESHOW_RESOURCE
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (theResources.size() > 0) {
			SimplePreResourceShowDetails accessDetails = new SimplePreResourceShowDetails(theResources);
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			JpaInterceptorBroadcaster.doCallHooks(theInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);

			theResources.removeIf(t -> t == null);
		}

	}

}
