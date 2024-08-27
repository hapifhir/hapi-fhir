/*
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * Determines the server's base using the incoming request
 */
public class IncomingRequestAddressStrategy implements IServerAddressStrategy {

	private String myServletPath;

	@Override
	public String determineServerBase(ServletContext theServletContext, HttpServletRequest theRequest) {
		if (theRequest == null) {
			return null;
		}
		String requestFullPath = StringUtils.defaultString(theRequest.getRequestURI());

		String servletPath;
		if (myServletPath != null) {
			servletPath = myServletPath;
		} else {
			servletPath = StringUtils.defaultString(theRequest.getServletPath());
		}

		StringBuffer requestUrl = theRequest.getRequestURL();
		String servletContextPath = StringUtils.defaultString(theRequest.getContextPath());

		String requestPath = requestFullPath.substring(servletContextPath.length() + servletPath.length());
		if (requestPath.length() > 0 && requestPath.charAt(0) == '/') {
			requestPath = requestPath.substring(1);
		}

		int startOfPath = requestUrl.indexOf("//");
		int requestUrlLength = requestUrl.length();

		if (startOfPath != -1 && (startOfPath + 2) < requestUrlLength) {
			startOfPath = requestUrl.indexOf("/", startOfPath + 2);
		}
		if (startOfPath == -1) {
			startOfPath = 0;
		}

		int contextIndex;
		if (servletPath.length() == 0 || servletPath.equals("/")) {
			if (requestPath.length() == 0) {
				contextIndex = requestUrlLength;
			} else {
				contextIndex = requestUrl.indexOf(requestPath, startOfPath);
			}
		} else {
			// servletContextPath can start with servletPath
			contextIndex = requestUrl.indexOf(servletPath + "/", startOfPath);
			if (contextIndex == -1) {
				contextIndex = requestUrl.indexOf(servletPath, startOfPath);
			}
		}

		String fhirServerBase;
		int length = contextIndex + servletPath.length();
		if (length > requestUrlLength) {
			length = requestUrlLength;
		}
		fhirServerBase = requestUrl.substring(0, length);
		return fhirServerBase;
	}

	/**
	 * If set to a non-null value (default is <code>null</code>), this address strategy assumes that the FHIR endpoint is deployed to the given servlet path within the context. This is useful in some
	 * deployments where it isn't obvious to the servlet which part of the path is actually the root path to reach the servlet.
	 * <p>
	 * Example values could be:
	 * <ul>
	 * <li>null</li>
	 * <li>/</li>
	 * <li>/base</li>
	 * </ul>
	 * </p>
	 * <p>
	 * <b>Wildcards are not supported!</b>
	 * </p>
	 */
	public void setServletPath(String theServletPath) {
		myServletPath = theServletPath;
	}
}
