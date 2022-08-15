package ca.uhn.fhir.jpa.interceptor;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

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

	public static final String HEADER_NAME = "X-Retry-On-Version-Conflict";
	public static final String MAX_RETRIES = "max-retries";
	public static final String RETRY = "retry";

	@Hook(value = Pointcut.STORAGE_VERSION_CONFLICT, order = 100)
	public ResourceVersionConflictResolutionStrategy check(RequestDetails theRequestDetails) {
		ResourceVersionConflictResolutionStrategy retVal = new ResourceVersionConflictResolutionStrategy();

		if (theRequestDetails != null) {
			List<String> headers = theRequestDetails.getHeaders(HEADER_NAME);
			if (headers != null) {
				for (String headerValue : headers) {
					if (isNotBlank(headerValue)) {

						StringTokenizer tok = new StringTokenizer(headerValue, ";");
						while (tok.hasMoreTokens()) {
							String next = trim(tok.nextToken());
							if (next.equals(RETRY)) {
								retVal.setRetry(true);
							} else if (next.startsWith(MAX_RETRIES + "=")) {

								String val = trim(next.substring((MAX_RETRIES + "=").length()));
								int maxRetries = Integer.parseInt(val);
								maxRetries = Math.min(100, maxRetries);
								retVal.setMaxRetries(maxRetries);

							}

						}

					}
				}
			}
		}

		return retVal;
	}


	/**
	 * Convenience method to add a retry header to a system request
	 */
	public static void addRetryHeader(SystemRequestDetails theRequestDetails, int theMaxRetries) {
		Validate.inclusiveBetween(1, Integer.MAX_VALUE, theMaxRetries, "Max retries must be > 0");
		String value = RETRY + "; " + MAX_RETRIES + "=" + theMaxRetries;
		theRequestDetails.addHeader(HEADER_NAME, value);
	}
}
