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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.HashSet;
import java.util.Set;

/**
 * This interceptor intercepts each outgoing response and if it contains a FHIR resource, validates that resource. The interceptor may be configured to run any validator modules, and will then add
 * headers to the response or fail the request with an {@link UnprocessableEntityException HTTP 422 Unprocessable Entity}.
 */
public class ResponseValidatingInterceptor extends BaseValidatingInterceptor<IBaseResource> {

	/**
	 * X-HAPI-Request-Validation
	 */
	public static final String DEFAULT_RESPONSE_HEADER_NAME = "X-FHIR-Response-Validation";

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResponseValidatingInterceptor.class);

	private Set<RestOperationTypeEnum> myExcludeOperationTypes;

	/**
	 * Do not validate the following operations. A common use for this is to exclude {@link RestOperationTypeEnum#METADATA} so that this operation will execute as quickly as possible.
	 */
	public void addExcludeOperationType(RestOperationTypeEnum theOperationType) {
		Validate.notNull(theOperationType, "theOperationType must not be null");
		if (myExcludeOperationTypes == null) {
			myExcludeOperationTypes = new HashSet<>();
		}
		myExcludeOperationTypes.add(theOperationType);
	}

	@Override
	ValidationResult doValidate(FhirValidator theValidator, IBaseResource theRequest) {
		return theValidator.validateWithResult(theRequest);
	}

	@Hook(Pointcut.SERVER_OUTGOING_RESPONSE)
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject) {
		RestOperationTypeEnum operationType = theRequestDetails.getRestOperationType();
		if (operationType != null && myExcludeOperationTypes != null && myExcludeOperationTypes.contains(operationType)) {
			ourLog.trace("Operation type {} is excluded from validation", operationType);
			return true;
		}

		validate(theResponseObject, theRequestDetails);

		return true;
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
