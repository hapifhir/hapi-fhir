package ca.uhn.fhir.rest.server.method;

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


import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.IRestfulResponse;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding<MethodOutcome> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);

	private boolean myReturnVoid;

	public BaseOutcomeReturningMethodBinding(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theProvider);

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			if (!allowVoidReturnType()) {
				throw new ConfigurationException(Msg.code(367) + "Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getName() + " is a @" + theMethodAnnotation.getSimpleName() + " method but it does not return " + MethodOutcome.class);
			} else if (theMethod.getReturnType() == void.class) {
				myReturnVoid = true;
			}
		}
	}

	protected abstract void addParametersForServerRequest(RequestDetails theRequest, Object[] theParams);

	/**
	 * Subclasses may override to allow a void method return type, which is allowable for some methods (e.g. delete)
	 */
	protected boolean allowVoidReturnType() {
		return false;
	}

	/**
	 * For servers, this method will match only incoming requests that match the given operation, or which have no
	 * operation in the URL if this method returns null.
	 */
	protected abstract String getMatchingOperation();

	private int getOperationStatus(MethodOutcome response) {
		switch (getRestOperationType()) {
			case CREATE:
				validateResponseNotNullIfItShouldntBe(response);
				if (response == null || response.getCreated() == null || Boolean.TRUE.equals(response.getCreated())) {
					return Constants.STATUS_HTTP_201_CREATED;
				}
				return Constants.STATUS_HTTP_200_OK;

			case UPDATE:
			case PATCH:
				if (response == null || response.getCreated() == null || Boolean.FALSE.equals(response.getCreated())) {
					return Constants.STATUS_HTTP_200_OK;
				}
				return Constants.STATUS_HTTP_201_CREATED;
			case VALIDATE:
			case DELETE:
			default:
				validateResponseNotNullIfItShouldntBe(response);
				if (response == null) {
					return Constants.STATUS_HTTP_204_NO_CONTENT;
				}
				if (response.getOperationOutcome() == null) {
					return Constants.STATUS_HTTP_204_NO_CONTENT;
				}
				return Constants.STATUS_HTTP_200_OK;
		}
	}

	private void validateResponseNotNullIfItShouldntBe(MethodOutcome response) {
		if (response == null && !isReturnVoid()) {
			throw new InternalErrorException(Msg.code(368) + "Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
		}
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		Set<RequestTypeEnum> allowableRequestTypes = provideAllowableRequestTypes();
		RequestTypeEnum requestType = theRequest.getRequestType();
		if (!allowableRequestTypes.contains(requestType)) {
			return MethodMatchEnum.NONE;
		}
		if (!getResourceName().equals(theRequest.getResourceName())) {
			return MethodMatchEnum.NONE;
		}
		if (getMatchingOperation() == null && StringUtils.isNotBlank(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}
		if (getMatchingOperation() != null && !getMatchingOperation().equals(theRequest.getOperation())) {
			return MethodMatchEnum.NONE;
		}

		/*
		 * Note: Technically this will match an update (PUT) method even if
		 * there is no ID in the URL - We allow this here because there is no
		 * better match for that, and this allows the update/PUT method to give
		 * a helpful error if the client has forgotten to include the
		 * ID in the URL.
		 *
		 * It's also needed for conditional update..
		 */

		return MethodMatchEnum.EXACT;
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {

		Object[] params = createParametersForServerRequest(theRequest);
		addParametersForServerRequest(theRequest, params);

		/*
		 * No need to catch and handle exceptions here, we already handle them one level up including invoking interceptors
		 * on them
		 */
		MethodOutcome response;
		Object methodReturn = invokeServerMethod(theRequest, params);

		if (methodReturn instanceof IBaseOperationOutcome) {
			response = new MethodOutcome();
			response.setOperationOutcome((IBaseOperationOutcome) methodReturn);
		} else {
			response = (MethodOutcome) methodReturn;
		}

		if (response != null && response.getId() != null && response.getId().hasResourceType()) {
			if (getContext().getResourceDefinition(response.getId().getResourceType()) == null) {
				throw new InternalErrorException(Msg.code(369) + "Server method returned invalid resource ID: " + response.getId().getValue());
			}
		}

		IBaseOperationOutcome outcome = response != null ? response.getOperationOutcome() : null;

		return returnResponse(theServer, theRequest, response, outcome);
	}

	public boolean isReturnVoid() {
		return myReturnVoid;
	}

	protected abstract Set<RequestTypeEnum> provideAllowableRequestTypes();

	private Object returnResponse(IRestfulServer<?> theServer, RequestDetails theRequest, MethodOutcome theMethodOutcome, IBaseResource theOriginalOutcome) throws IOException {
		int operationStatus = getOperationStatus(theMethodOutcome);
		IBaseResource outcome = theOriginalOutcome;

		RestOperationTypeEnum restOperationType = getRestOperationType(theRequest);
		boolean allowPrefer = RestfulServerUtils.respectPreferHeader(restOperationType);
		if (allowPrefer) {
			String prefer = theRequest.getHeader(Constants.HEADER_PREFER);
			PreferHeader preferReturn = RestfulServerUtils.parsePreferHeader(theServer, prefer);
			PreferReturnEnum returnEnum = preferReturn.getReturn();
			returnEnum = defaultIfNull(returnEnum, PreferReturnEnum.REPRESENTATION);

			switch (returnEnum) {
				case REPRESENTATION:
					if (theMethodOutcome != null) {
						outcome = theMethodOutcome.getResource();
						theMethodOutcome.fireResourceViewCallbacks();
					} else {
						outcome = null;
					}
					break;
				case MINIMAL:
					outcome = null;
					break;
				case OPERATION_OUTCOME:
					outcome = theOriginalOutcome;
					break;
			}

		}

		ResponseDetails responseDetails = new ResponseDetails();
		responseDetails.setResponseResource(outcome);
		responseDetails.setResponseCode(operationStatus);

		if (!BaseResourceReturningMethodBinding.callOutgoingResponseHook(theRequest, responseDetails)) {
			return null;
		}

		IRestfulResponse restfulResponse = theRequest.getResponse();

		if (theMethodOutcome != null) {
			if (theMethodOutcome.getResource() != null) {
				restfulResponse.setOperationResourceLastUpdated(RestfulServerUtils.extractLastUpdatedFromResource(theMethodOutcome.getResource()));
			}

			IIdType responseId = theMethodOutcome.getId();
			if (responseId != null && responseId.getResourceType() == null && responseId.hasIdPart()) {
				responseId = responseId.withResourceType(getResourceName());
			}

			if (responseId != null) {
				String serverBase = theRequest.getFhirServerBase();
				responseId = RestfulServerUtils.fullyQualifyResourceIdOrReturnNull(theServer, theMethodOutcome.getResource(), serverBase, responseId);
				restfulResponse.setOperationResourceId(responseId);
			}
		}

		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);
		Set<SummaryEnum> summaryMode = Collections.emptySet();

		return restfulResponse.streamResponseAsResource(responseDetails.getResponseResource(), prettyPrint, summaryMode, responseDetails.getResponseCode(), null, theRequest.isRespondGzip(), true);
	}

}
