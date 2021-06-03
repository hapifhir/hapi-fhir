package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ServletRequestTracing {
	private static final Logger ourLog = LoggerFactory.getLogger(ServletRequestTracing.class);
	public static final String ATTRIBUTE_REQUEST_ID = ServletRequestTracing.class.getName() + '.' + Constants.HEADER_REQUEST_ID;

	/**
	 * Assign a tracing id to this request, using
	 * the X-Request-ID if present and compatible.
	 *
	 * If none present, generate a 64 random alpha-numeric string that is not
	 * cryptographically secure.
	 *
	 * @param theServletRequest the request to trace
	 * @return the tracing id
	 */
	public static String getOrGenerateRequestId(ServletRequest theServletRequest) {
		String requestId = maybeGetRequestId(theServletRequest);
		if (isBlank(requestId)) {
			requestId = RandomStringUtils.randomAlphanumeric(Constants.REQUEST_ID_LENGTH);
		}

		ourLog.debug("Assigned tracing id {}", requestId);

		theServletRequest.setAttribute(ATTRIBUTE_REQUEST_ID, requestId);

		return requestId;
	}

	@Nullable
	public static String maybeGetRequestId(ServletRequest theServletRequest) {
		// have we already seen this request?
		String requestId = (String) theServletRequest.getAttribute(ATTRIBUTE_REQUEST_ID);

		if (requestId == null && theServletRequest instanceof HttpServletRequest) {
			// Also applies to non-FHIR (e.g. admin-json) requests).
			HttpServletRequest request = (HttpServletRequest) theServletRequest;
			requestId = request.getHeader(Constants.HEADER_REQUEST_ID);
			if (isNotBlank(requestId)) {
				for (char nextChar : requestId.toCharArray()) {
					if (!Character.isLetterOrDigit(nextChar)) {
						if (nextChar != '.' && nextChar != '-' && nextChar != '_' && nextChar != ' ') {
							requestId = null;
							break;
						}
					}
				}
			}
		}
		return requestId;
	}

}
