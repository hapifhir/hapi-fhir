package ca.uhn.fhir.rest.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

/**
 * Server interceptor which logs each request using a defined format
 * <p>
 * The following substitution variables are supported:
 * </p>
 * <table>
 * <tr>
 * <td>${operationType}</td>
 * <td>A code indicating the operation type for this request, e.g. "read", "history-instance", etc.)</td>
 * </tr>
 * <tr>
 * <td>${id}</td>
 * <td>The resource ID associated with this request (or "" if none)</td>
 * </tr>
 * </table>
 *
 */
public class LoggingInterceptor extends InterceptorAdapter {

	private String myMessageFormat = "${operationType} - ${id}";
	private Logger myLogger = ourLog;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoggingInterceptor.class);

	@Override
	public boolean incomingRequest(final RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		StrLookup<?> lookup = new StrLookup<String>() {
			@Override
			public String lookup(String theKey) {
				if ("operationType".equals(theKey)) {
					if (theRequestDetails.getResourceOperationType() != null) {
						return theRequestDetails.getResourceOperationType().getCode();
					}
					if (theRequestDetails.getSystemOperationType() != null) {
						return theRequestDetails.getSystemOperationType().getCode();
					}
					return "";
				}
				if ("id".equals(theKey)) {
					if (theRequestDetails.getId() != null) {
						return theRequestDetails.getId().getValue();
					}
					return "";
				}
				return "!VAL!";
			}
		};
		StrSubstitutor subs = new StrSubstitutor(lookup, "${", "}", '\\');

		String line = subs.replace(myMessageFormat);
		myLogger.info(line);

		return true;
	}

}
