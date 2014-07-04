package ca.uhn.fhir.rest.server;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Determines the server's base using the incoming request
 */
public class IncomingRequestAddressStrategy implements IServerAddressStrategy {

	@Override
	public String determineServerBase(HttpServletRequest theRequest) {
		String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());
		String servletPath = StringUtils.defaultString(theRequest.getServletPath());
		StringBuffer requestUrl = theRequest.getRequestURL();
		String servletContextPath = "";
		if (theRequest.getServletContext() != null) {
			servletContextPath = StringUtils.defaultString(theRequest.getServletContext().getContextPath());
			// } else {
			// servletContextPath = servletPath;
		}

		String requestPath = requestFullPath.substring(servletContextPath.length() + servletPath.length());
		if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
			requestPath = requestPath.substring(1);
		}

		int contextIndex;
		if (servletPath.length() == 0) {
			if (requestPath.length() == 0) {
				contextIndex = requestUrl.length();
			} else {
				contextIndex = requestUrl.indexOf(requestPath);
			}
		} else {
			contextIndex = requestUrl.indexOf(servletPath);
		}

		String fhirServerBase;
		int length = contextIndex + servletPath.length();
		fhirServerBase = requestUrl.substring(0, length);
		return fhirServerBase;
	}

}
