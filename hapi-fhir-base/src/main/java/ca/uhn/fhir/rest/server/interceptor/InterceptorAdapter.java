package ca.uhn.fhir.rest.server.interceptor;

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
	public boolean incomingRequest(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		return true;
	}

	@Override
	public boolean incomingRequest(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
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
