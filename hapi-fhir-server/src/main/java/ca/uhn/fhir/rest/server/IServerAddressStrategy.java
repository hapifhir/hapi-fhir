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
 * Provides the server base for a given incoming request. This can be used to account for
 * multi-homed servers or other unusual network configurations.
 */
public interface IServerAddressStrategy {

	/**
	 * Determine the server base for a given request
	 */
	String determineServerBase(ServletContext theServletContext, HttpServletRequest theRequest);

	/**
	 * Determines the servlet's context path.
	 *
	 * This is here to try and deal with the wide variation in servers and what they return.
	 *
	 * getServletContext().getContextPath() is supposed to return the path to the specific servlet we are deployed as but it's not available everywhere. On some servers getServletContext() can return
	 * null (old Jetty seems to suffer from this, see hapi-fhir-base-test-mindeps-server) and on other old servers (Servlet 2.4) getServletContext().getContextPath() doesn't even exist.
	 *
	 * theRequest.getContextPath() returns the context for the specific incoming request. It should be available everywhere, but it's likely to be less predicable if there are multiple servlet mappings
	 * pointing to the same servlet, so we don't favour it. This is possibly not the best strategy (maybe we should just always use theRequest.getContextPath()?) but so far people seem happy with this
	 * behaviour across a wide variety of platforms.
	 *
	 * If you are having troubles on a given platform/configuration and want to suggest a change or even report incompatibility here, we'd love to hear about it.
	 */
	default String determineServletContextPath(HttpServletRequest theRequest, RestfulServer server) {
		String retVal;
		if (server.getServletContext() != null) {
			if (server.getServletContext().getMajorVersion() >= 3
					|| (server.getServletContext().getMajorVersion() > 2
							&& server.getServletContext().getMinorVersion() >= 5)) {
				retVal = server.getServletContext().getContextPath();
			} else {
				retVal = theRequest.getContextPath();
			}
		} else {
			retVal = theRequest.getContextPath();
		}
		retVal = StringUtils.defaultString(retVal);
		return retVal;
	}
}
