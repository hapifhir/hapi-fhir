package ca.uhn.fhir.rest.server.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.validation.FhirValidator;

public class ResponseValidatingInterceptor extends InterceptorAdapter {

	private FhirValidator myValidator;
	
	/**
	 * Returns the validator used ny this interceptor
	 */
	public FhirValidator getValidator() {
		return myValidator;
	}

	/**
	 * Sets the validator instance to use. Must not be null.
	 */
	public void setValidator(FhirValidator theValidator) {
		Validate.notNull(theValidator, "Validator must not be null");
		myValidator = theValidator;
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

	
	
}
