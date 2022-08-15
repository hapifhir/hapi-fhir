package ca.uhn.fhir.rest.server.interceptor;

/*
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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

/**
 * This interceptor creates verbose server log entries containing the complete request and response payloads. 
 * <p> 
 * This interceptor is mainly intended for debugging since it will generate very large log entries and
 * could potentially be a security risk since it logs every header and complete payload. Use with caution! 
 * </p>
 */
public class VerboseLoggingInterceptor extends InterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VerboseLoggingInterceptor.class);

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		
		StringBuilder b = new StringBuilder("Incoming request: ");
		b.append(theRequest.getMethod());
		b.append(" ");
		b.append(theRequest.getRequestURL());
		b.append("\n");
		
		for (Enumeration<String> headerEnumeration = theRequest.getHeaderNames(); headerEnumeration.hasMoreElements(); ) {
			String nextName = headerEnumeration.nextElement();
			for (Enumeration<String> valueEnumeration = theRequest.getHeaders(nextName); valueEnumeration.hasMoreElements(); ) {
				b.append(" * ").append(nextName).append(": ").append(valueEnumeration.nextElement()).append("\n");
			}
		}
		
		ourLog.info(b.toString());
		return true;
	}

	
	
}
