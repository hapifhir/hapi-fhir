package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.MDC;

public class BaseJpaProvider {

	public static final String REMOTE_ADDR = "req.remoteAddr";
	public static final String REMOTE_UA = "req.userAgent";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaProvider.class);

	public void startRequest(HttpServletRequest theRequest) {
		if (theRequest == null) {
			return;
		}

		Set<String> headerNames = new TreeSet<String>();
		for (Enumeration<String> enums = theRequest.getHeaderNames(); enums.hasMoreElements();) {
			headerNames.add(enums.nextElement());
		}
		ourLog.debug("Request headers: {}", headerNames);

		Enumeration<String> forwardedFors = theRequest.getHeaders("x-forwarded-for");
		StringBuilder b = new StringBuilder();
		for (Enumeration<String> enums = forwardedFors; enums.hasMoreElements();) {
			if (b.length() > 0) {
				b.append(" / ");
			}
			b.append(enums.nextElement());
		}
		
		String forwardedFor = b.toString();
		String ip = theRequest.getRemoteAddr();
		if (StringUtils.isBlank(forwardedFor)) {
			org.slf4j.MDC.put(REMOTE_ADDR, ip);
			ourLog.debug("Request is from address: {}", ip);
		} else {
			org.slf4j.MDC.put(REMOTE_ADDR, forwardedFor);
			ourLog.debug("Request is from forwarded address: {}", forwardedFor);
		}

		String userAgent = StringUtils.defaultString(theRequest.getHeader("user-agent"));
		org.slf4j.MDC.put(REMOTE_UA, userAgent);
		
	}

	public void endRequest(HttpServletRequest theRequest) {
		MDC.remove(REMOTE_ADDR);
		MDC.remove(REMOTE_UA);
	}

}
