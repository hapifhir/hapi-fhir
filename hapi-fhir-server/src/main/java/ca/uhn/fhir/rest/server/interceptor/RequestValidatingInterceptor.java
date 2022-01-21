package ca.uhn.fhir.rest.server.interceptor;

/*-
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.method.ResourceParameter;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor intercepts each incoming request and if it contains a FHIR resource, validates that resource. The
 * interceptor may be configured to run any validator modules, and will then add headers to the response or fail the
 * request with an {@link UnprocessableEntityException HTTP 422 Unprocessable Entity}.
 */
public class RequestValidatingInterceptor extends BaseValidatingInterceptor<String> {

	/**
	 * X-HAPI-Request-Validation
	 */
	public static final String DEFAULT_RESPONSE_HEADER_NAME = "X-FHIR-Request-Validation";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestValidatingInterceptor.class);
	private boolean myAddValidationResultsToResponseOperationOutcome = true;

	@Override
	ValidationResult doValidate(FhirValidator theValidator, String theRequest) {
		return theValidator.validateWithResult(theRequest);
	}

	@Hook(Pointcut.SERVER_INCOMING_REQUEST_POST_PROCESSED)
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		EncodingEnum encoding = RestfulServerUtils.determineRequestEncodingNoDefault(theRequestDetails);
		if (encoding == null) {
			ourLog.trace("Incoming request does not appear to be FHIR, not going to validate");
			return true;
		}

		Charset charset = ResourceParameter.determineRequestCharset(theRequestDetails);
		String requestText = new String(theRequestDetails.loadRequestContents(), charset);

		if (isBlank(requestText)) {
			ourLog.trace("Incoming request does not have a body");
			return true;
		}

		ValidationResult validationResult = validate(requestText, theRequestDetails);

		if (myAddValidationResultsToResponseOperationOutcome) {
			addValidationResultToRequestDetails(theRequestDetails, validationResult);
		}

		return true;
	}

	/**
	 * If set to {@literal true} (default is true), the validation results
	 * will be added to the OperationOutcome being returned to the client,
	 * unless the response being returned is not an OperationOutcome
	 * to begin with (e.g. if the client has requested
	 * <code>Return: prefer=representation</code>)
	 */
	public boolean isAddValidationResultsToResponseOperationOutcome() {
		return myAddValidationResultsToResponseOperationOutcome;
	}

	/**
	 * If set to {@literal true} (default is true), the validation results
	 * will be added to the OperationOutcome being returned to the client,
	 * unless the response being returned is not an OperationOutcome
	 * to begin with (e.g. if the client has requested
	 * <code>Return: prefer=representation</code>)
	 */
	public void setAddValidationResultsToResponseOperationOutcome(boolean theAddValidationResultsToResponseOperationOutcome) {
		myAddValidationResultsToResponseOperationOutcome = theAddValidationResultsToResponseOperationOutcome;
	}

	@Override
	String provideDefaultResponseHeaderName() {
		return DEFAULT_RESPONSE_HEADER_NAME;
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

}
