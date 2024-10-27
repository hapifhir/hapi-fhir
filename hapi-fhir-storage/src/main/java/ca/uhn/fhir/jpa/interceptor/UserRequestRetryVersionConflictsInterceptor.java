/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.apache.commons.lang3.Validate;

/**
 * This interceptor looks for a header on incoming requests called <code>X-Retry-On-Version-Conflict</code> and
 * if present, it will instruct the server to automatically retry JPA server operations that would have
 * otherwise failed with a {@link ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException} (HTTP 409).
 * <p>
 * The format of the header is:<br/>
 * <code>X-Retry-On-Version-Conflict: retry; max-retries=100</code>
 * </p>
 */
@Interceptor
public class UserRequestRetryVersionConflictsInterceptor {

	/** Deprecated and moved to {@link ca.uhn.fhir.rest.api.Constants#HEADER_RETRY_ON_VERSION_CONFLICT} */
	@Deprecated
	public static final String HEADER_NAME = "X-Retry-On-Version-Conflict";

	/** Deprecated and moved to {@link ca.uhn.fhir.rest.api.Constants#HEADER_MAX_RETRIES} */
	@Deprecated
	public static final String MAX_RETRIES = "max-retries";

	/** Deprecated and moved to {@link ca.uhn.fhir.rest.api.Constants#HEADER_RETRY} */
	@Deprecated
	public static final String RETRY = "retry";

	@Hook(value = Pointcut.STORAGE_VERSION_CONFLICT, order = 100)
	public ResourceVersionConflictResolutionStrategy check(RequestDetails theRequestDetails) {
		ResourceVersionConflictResolutionStrategy retVal = new ResourceVersionConflictResolutionStrategy();
		boolean shouldSetRetries = theRequestDetails != null && theRequestDetails.isRetry();
		if (shouldSetRetries) {
			retVal.setRetry(true);
			int maxRetries = Math.min(100, theRequestDetails.getMaxRetries());
			retVal.setMaxRetries(maxRetries);
		}

		return retVal;
	}

	/**
	 * Convenience method to add a retry header to a system request
	 */
	public static void addRetryHeader(SystemRequestDetails theRequestDetails, int theMaxRetries) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theMaxRetries, "Max retries must be > 0");
		theRequestDetails.setRetry(true);
		theRequestDetails.setMaxRetries(theMaxRetries);
	}
}
