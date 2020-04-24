package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR Model
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

	/**
	 * Broadcast hooks to both the interceptor service associated with the request, as well
	 * as the one associated with the JPA module.
	 */
	public static Object doCallHooksAndReturnObject(IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		Object retVal = true;
		if (theInterceptorBroadcaster != null) {
			retVal = theInterceptorBroadcaster.callHooksAndReturnObject(thePointcut, theParams);
		}
		if (theRequestDetails != null && retVal == null) {
			retVal = theRequestDetails.getInterceptorBroadcaster().callHooksAndReturnObject(thePointcut, theParams);
		}
		return retVal;
	}

	public static boolean hasHooks(Pointcut thePointcut, IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails) {
		if (theInterceptorBroadcaster != null && theInterceptorBroadcaster.hasHooks(thePointcut)) {
			return true;
		}
		return theRequestDetails != null && theRequestDetails.getInterceptorBroadcaster().hasHooks(thePointcut);
	}
}
