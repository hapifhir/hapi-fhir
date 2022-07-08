package ca.uhn.fhir.rest.server.interceptor;

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
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SimplePreResourceShowDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.CheckReturnValue;
import java.util.List;
import java.util.Objects;

public class ServerInterceptorUtil {

	private ServerInterceptorUtil() {
		super();
	}

	/**
	 * Fires {@link Pointcut#STORAGE_PRESHOW_RESOURCES} interceptor hook, and potentially remove resources
	 * from the resource list
	 */
	@CheckReturnValue
	public static List<IBaseResource> fireStoragePreshowResource(List<IBaseResource> theResources, RequestDetails theRequest, IInterceptorBroadcaster theInterceptorBroadcaster) {
		List<IBaseResource> retVal = theResources;
		retVal.removeIf(Objects::isNull);

		// Interceptor call: STORAGE_PRESHOW_RESOURCE
		// This can be used to remove results from the search result details before
		// the user has a chance to know that they were in the results
		if (retVal.size() > 0) {
			SimplePreResourceShowDetails accessDetails = new SimplePreResourceShowDetails(retVal);
			HookParams params = new HookParams()
				.add(IPreResourceShowDetails.class, accessDetails)
				.add(RequestDetails.class, theRequest)
				.addIfMatchesType(ServletRequestDetails.class, theRequest);
			CompositeInterceptorBroadcaster.doCallHooks(theInterceptorBroadcaster, theRequest, Pointcut.STORAGE_PRESHOW_RESOURCES, params);

			retVal = accessDetails.toList();
			retVal.removeIf(Objects::isNull);
		}

		return retVal;
	}

}
