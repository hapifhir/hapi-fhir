package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;

/**
 * This interceptor causes the server to reject invocations for HTTP methods
 * other than those supported by the server with an HTTP 405. This is a requirement
 * of some security assessments.
 */
public class BanUnsupportedHttpMethodsInterceptor extends InterceptorAdapter {

	private Set<RequestTypeEnum> myAllowedMethods = new HashSet<RequestTypeEnum>();
	
	public BanUnsupportedHttpMethodsInterceptor() {
		myAllowedMethods.add(RequestTypeEnum.GET);
		myAllowedMethods.add(RequestTypeEnum.OPTIONS);
		myAllowedMethods.add(RequestTypeEnum.DELETE);
		myAllowedMethods.add(RequestTypeEnum.PUT);
		myAllowedMethods.add(RequestTypeEnum.POST);
	}
	
	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		RequestTypeEnum requestType = RequestTypeEnum.valueOf(theRequest.getMethod());
		if (myAllowedMethods.contains(requestType)) {
			return true;
		}
		
		throw new MethodNotAllowedException("Method not supported: " + theRequest.getMethod());
	}

}
