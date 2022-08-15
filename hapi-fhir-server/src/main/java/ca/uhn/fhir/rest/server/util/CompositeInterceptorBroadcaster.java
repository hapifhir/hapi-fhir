package ca.uhn.fhir.rest.server.util;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import javax.annotation.Nullable;

public class CompositeInterceptorBroadcaster {

	/**
	 * Non instantiable
	 */
	private CompositeInterceptorBroadcaster() {
		// nothing
	}

	/**
	 * Broadcast hooks to both the interceptor service associated with the request, as well
	 * as the one associated with the JPA module.
	 */
	public static boolean doCallHooks(IInterceptorBroadcaster theInterceptorBroadcaster, @Nullable RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		return newCompositeBroadcaster(theInterceptorBroadcaster, theRequestDetails).callHooks(thePointcut, theParams);
	}

	/**
	 * Broadcast hooks to both the interceptor service associated with the request, as well
	 * as the one associated with the JPA module.
	 */
	public static Object doCallHooksAndReturnObject(IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails, Pointcut thePointcut, HookParams theParams) {
		return newCompositeBroadcaster(theInterceptorBroadcaster, theRequestDetails).callHooksAndReturnObject(thePointcut, theParams);
	}

	public static boolean hasHooks(Pointcut thePointcut, IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails) {
		return newCompositeBroadcaster(theInterceptorBroadcaster, theRequestDetails).hasHooks(thePointcut);
	}

	/**
	 * @since 5.5.0
	 */
	public static IInterceptorBroadcaster newCompositeBroadcaster(IInterceptorBroadcaster theInterceptorBroadcaster, RequestDetails theRequestDetails) {
		return new IInterceptorBroadcaster() {
			@Override
			public boolean callHooks(Pointcut thePointcut, HookParams theParams) {
				boolean retVal = true;
				if (theInterceptorBroadcaster != null) {
					retVal = theInterceptorBroadcaster.callHooks(thePointcut, theParams);
				}
				if (theRequestDetails != null && theRequestDetails.getInterceptorBroadcaster() != null && retVal) {
					IInterceptorBroadcaster interceptorBroadcaster = theRequestDetails.getInterceptorBroadcaster();
					interceptorBroadcaster.callHooks(thePointcut, theParams);
				}
				return retVal;
			}

			@Override
			public Object callHooksAndReturnObject(Pointcut thePointcut, HookParams theParams) {
				Object retVal = true;
				if (theInterceptorBroadcaster != null) {
					retVal = theInterceptorBroadcaster.callHooksAndReturnObject(thePointcut, theParams);
				}
				if (theRequestDetails != null && theRequestDetails.getInterceptorBroadcaster() != null && retVal == null) {
					IInterceptorBroadcaster interceptorBroadcaster = theRequestDetails.getInterceptorBroadcaster();
					retVal = interceptorBroadcaster.callHooksAndReturnObject(thePointcut, theParams);
				}
				return retVal;
			}

			@Override
			public boolean hasHooks(Pointcut thePointcut) {
				if (theInterceptorBroadcaster != null && theInterceptorBroadcaster.hasHooks(thePointcut)) {
					return true;
				}
				return theRequestDetails != null &&
					theRequestDetails.getInterceptorBroadcaster() != null &&
					theRequestDetails.getInterceptorBroadcaster().hasHooks(thePointcut);
			}
		};
	}
}
