package ca.uhn.fhir.rest.server.interceptor;

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
public class BanUnsupprtedHttpMethodsInterceptor extends InterceptorAdapter {

	private Set<RequestTypeEnum> myAllowedMethods = new HashSet<RequestTypeEnum>();
	
	public BanUnsupprtedHttpMethodsInterceptor() {
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
