package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.model.ResourceVersionConflictResolutionStrategy;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * This interceptor looks for a header on incoming requests called <code>X-Retry-On-Version-Conflict</code> and
 * if present, it will instruct the server to automatically retry JPA server operations that would have
 * otherwise failed with a {@link ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException} (HTTP 409).
 * <p>
 *    The format of the header is:<br/>
 *    <code>X-Retry-On-Version-Conflict: retry; max-retries=100</code>
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
			for (String headerValue : theRequestDetails.getHeaders(HEADER_NAME)) {
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

		return retVal;
	}


}
