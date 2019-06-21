package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class JpaInterceptorBroadcaster {

	/**
	 * Broadcast hooks to both the interceptor service associated with the request, as well
	 * as the one associated with the JPA module.
	 */
	public static boolean doCallHooks(IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		boolean retVal = true;
		if (theInterceptorBroadcaster != null) {
			retVal = theInterceptorBroadcaster.callHooks(thePointcut, theParams);
		}
		if (theRequestDetails != null && retVal) {
			theRequestDetails.getInterceptorBroadcaster().callHooks(thePointcut, theParams);
		}
		return retVal;
	}
}
