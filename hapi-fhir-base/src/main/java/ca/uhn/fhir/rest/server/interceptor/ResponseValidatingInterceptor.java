package ca.uhn.fhir.rest.server.interceptor;

import java.nio.charset.Charset;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.param.ResourceParameter;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;

/**
 * This interceptor intercepts each outgoing response and if it contains a FHIR resource, validates that resource. The
 * interceptor may be configured to run any validator modules, and will then add headers to the response or fail the
 * request with an {@link UnprocessableEntityException HTTP 422 Unprocessable Entity}.
 */
public class ResponseValidatingInterceptor extends BaseValidatingInterceptor<IBaseResource> {

	/**
	 * X-HAPI-Request-Validation
	 */
	public static final String DEFAULT_RESPONSE_HEADER_NAME = "X-HAPI-Response-Validation";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptor.class);

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		validate(theResponseObject, theRequestDetails);
		return true;
	}

	/**
	 * Sets the name of the response header to add validation failures to
	 * 
	 * @see #DEFAULT_RESPONSE_HEADER_NAME
	 * @see #setAddResponseHeaderOnSeverity(ResultSeverityEnum)
	 */
	@Override
	public void setResponseHeaderName(String theResponseHeaderName) {
		super.setResponseHeaderName(theResponseHeaderName);
	}

	@Override
	String provideDefaultResponseHeaderName() {
		return DEFAULT_RESPONSE_HEADER_NAME;
	}

	@Override
	ValidationResult doValidate(FhirValidator theValidator, IBaseResource theRequest) {
		return theValidator.validateWithResult(theRequest);
	}

}
