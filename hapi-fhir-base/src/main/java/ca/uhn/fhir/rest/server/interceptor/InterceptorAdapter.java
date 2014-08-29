package ca.uhn.fhir.rest.server.interceptor;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

/**
 * Base class for {@link IServerInterceptor} implementations. Provides a No-op implementation
 * of all methods, always returning <code>true</code> 
 */
public class InterceptorAdapter implements IServerInterceptor {

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		return true;
	}

	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, TagList theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, Bundle theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		return true;
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		return true;
	}

}
