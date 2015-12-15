package ca.uhn.fhir.provider.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import ca.uhn.fhir.dao.IDao;
import ca.uhn.fhir.dao.IDaoContext;
import ca.uhn.fhir.provider.IProvider;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public abstract class BaseProvider implements IProvider {
	
	private static final Logger ourLog = LoggerFactory.getLogger(BaseProvider.class);
	public static final String REMOTE_ADDR = "req.remoteAddr";
	public static final String REMOTE_UA = "req.userAgent";
	private ThreadLocal<IDaoContext> daoContext = new ThreadLocal<IDaoContext>();

	public abstract IDao getBaseDao();
	
	@Override
	public void endRequest(HttpServletRequest theRequest) {
		getBaseDao().endRequest(daoContext.get());
		MDC.remove(REMOTE_ADDR);
		MDC.remove(REMOTE_UA);
	}

	public void endRequest(ServletRequestDetails theRequest) {
		endRequest(theRequest.getServletRequest());
	}

	@Override
	public void startRequest(HttpServletRequest theRequest) {
		if (theRequest == null) {
			return;
		}

		Set<String> headerNames = new TreeSet<String>();
		for (@SuppressWarnings("unchecked")
		Enumeration<String> enums = theRequest.getHeaderNames(); enums.hasMoreElements();) {
			headerNames.add(enums.nextElement());
		}
		ourLog.debug("Request headers: {}", headerNames);

		@SuppressWarnings("unchecked")
		Enumeration<String> forwardedFors = theRequest.getHeaders("x-forwarded-for");
		StringBuilder b = new StringBuilder();
		for (Enumeration<String> enums = forwardedFors; enums != null && enums.hasMoreElements();) {
			if (b.length() > 0) {
				b.append(" / ");
			}
			b.append(enums.nextElement());
		}

		String forwardedFor = b.toString();
		String ip = theRequest.getRemoteAddr();
		if (StringUtils.isBlank(forwardedFor)) {
			MDC.put(REMOTE_ADDR, ip);
			ourLog.debug("Request is from address: {}", ip);
		} else {
			MDC.put(REMOTE_ADDR, forwardedFor);
			ourLog.debug("Request is from forwarded address: {}", forwardedFor);
		}

		String userAgent = StringUtils.defaultString(theRequest.getHeader("user-agent"));
		MDC.put(REMOTE_UA, userAgent);
		daoContext.set(getBaseDao().startRequest());
	}

	@Override
	public void startRequest(ServletRequestDetails theRequest) {
		startRequest(theRequest.getServletRequest());
	}

}
