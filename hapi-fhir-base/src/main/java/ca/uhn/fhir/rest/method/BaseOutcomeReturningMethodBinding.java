package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

abstract class BaseOutcomeReturningMethodBinding extends BaseMethodBinding<MethodOutcome> {
	static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseOutcomeReturningMethodBinding.class);

	private static EnumSet<RestOperationTypeEnum> ourOperationsWhichAllowPreferHeader = EnumSet.of(RestOperationTypeEnum.CREATE, RestOperationTypeEnum.UPDATE);

	private boolean myReturnVoid;

	public BaseOutcomeReturningMethodBinding(Method theMethod, FhirContext theContext, Class<?> theMethodAnnotation, Object theProvider) {
		super(theMethod, theContext, theProvider);

		if (!theMethod.getReturnType().equals(MethodOutcome.class)) {
			if (!allowVoidReturnType()) {
				throw new ConfigurationException("Method " + theMethod.getName() + " in type " + theMethod.getDeclaringClass().getCanonicalName() + " is a @" + theMethodAnnotation.getSimpleName() + " method but it does not return " + MethodOutcome.class);
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

	protected abstract BaseHttpClientInvocation createClientInvocation(Object[] theArgs, IResource resource);

	/**
	 * For servers, this method will match only incoming requests that match the given operation, or which have no
	 * operation in the URL if this method returns null.
	 */
	protected abstract String getMatchingOperation();

	private int getOperationStatus(MethodOutcome response) {
		switch (getRestOperationType()) {
		case CREATE:
			if (response == null) {
				throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null, which is not allowed for create operation");
			}
			if (response.getCreated() == null || Boolean.TRUE.equals(response.getCreated())) {
				return Constants.STATUS_HTTP_201_CREATED;
			}
			return Constants.STATUS_HTTP_200_OK;

		case UPDATE:
			if (response == null || response.getCreated() == null || Boolean.FALSE.equals(response.getCreated())) {
				return Constants.STATUS_HTTP_200_OK;
			}
			return Constants.STATUS_HTTP_201_CREATED;

		case VALIDATE:
		case DELETE:
		default:
			if (response == null) {
				if (isReturnVoid() == false) {
					throw new InternalErrorException("Method " + getMethod().getName() + " in type " + getMethod().getDeclaringClass().getCanonicalName() + " returned null");
				}
				return Constants.STATUS_HTTP_204_NO_CONTENT;
			}
			if (response.getOperationOutcome() == null) {
				return Constants.STATUS_HTTP_204_NO_CONTENT;
			}
			return Constants.STATUS_HTTP_200_OK;
		}
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		Set<RequestTypeEnum> allowableRequestTypes = provideAllowableRequestTypes();
		RequestTypeEnum requestType = theRequest.getRequestType();
		if (!allowableRequestTypes.contains(requestType)) {
			return false;
		}
		if (!getResourceName().equals(theRequest.getResourceName())) {
			return false;
		}
		if (getMatchingOperation() == null && StringUtils.isNotBlank(theRequest.getOperation())) {
			return false;
		}
		if (getMatchingOperation() != null && !getMatchingOperation().equals(theRequest.getOperation())) {
			return false;
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
		
		return true;
	}

	@Override
	public MethodOutcome invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		if (theResponseStatusCode >= 200 && theResponseStatusCode < 300) {
			if (myReturnVoid) {
				return null;
			}
			MethodOutcome retVal = MethodUtil.process2xxResponse(getContext(), theResponseStatusCode, theResponseMimeType, theResponseReader, theHeaders);
			return retVal;
		}
		throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
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
		Object methodReturn = invokeServerMethod(theServer, theRequest, params);
		
		if (methodReturn instanceof IBaseOperationOutcome) {
			response = new MethodOutcome();
			response.setOperationOutcome((IBaseOperationOutcome) methodReturn);
		} else {
			response = (MethodOutcome) methodReturn;
		}
		
		if (response != null && response.getId() != null && response.getId().hasResourceType()) {
			if (getContext().getResourceDefinition(response.getId().getResourceType()) == null) {
				throw new InternalErrorException("Server method returned invalid resource ID: " + response.getId().getValue());
			}
		}

		IBaseOperationOutcome outcome = response != null ? response.getOperationOutcome() : null;
		IBaseResource resource = response != null ? response.getResource() : null;

		return returnResponse(theServer, theRequest, response, outcome, resource);
	}
	public boolean isReturnVoid() {
		return myReturnVoid;
	}

	protected abstract Set<RequestTypeEnum> provideAllowableRequestTypes();

	private Object returnResponse(IRestfulServer<?> theServer, RequestDetails theRequest, MethodOutcome response, IBaseResource originalOutcome, IBaseResource resource) throws IOException {
		boolean allowPrefer = false;
		int operationStatus = getOperationStatus(response);
		IBaseResource outcome = originalOutcome;

		if (ourOperationsWhichAllowPreferHeader.contains(getRestOperationType())) {
			allowPrefer = true;
		}

		if (resource != null && allowPrefer) {
			String prefer = theRequest.getHeader(Constants.HEADER_PREFER);
			PreferReturnEnum preferReturn = RestfulServerUtils.parsePreferHeader(prefer);
			if (preferReturn != null) {
				if (preferReturn == PreferReturnEnum.REPRESENTATION) {
					outcome = resource;
				}
			}
		}

		for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
			IServerInterceptor next = theServer.getInterceptors().get(i);
			boolean continueProcessing = next.outgoingResponse(theRequest, outcome);
			if (!continueProcessing) {
				return null;
			}
		}
		
		IRestfulResponse restfulResponse = theRequest.getResponse();
		
		if (response != null) {
			if (response.getResource() != null) {
				restfulResponse.setOperationResourceLastUpdated(RestfulServerUtils.extractLastUpdatedFromResource(response.getResource()));
			}
			
			IIdType responseId = response.getId();
			if (responseId != null && responseId.getResourceType() == null && responseId.hasIdPart()) {
				responseId = responseId.withResourceType(getResourceName());
			}
			
			if (responseId != null) {
				String serverBase = theRequest.getFhirServerBase();
				responseId = RestfulServerUtils.fullyQualifyResourceIdOrReturnNull(theServer, resource, serverBase, responseId);
				restfulResponse.setOperationResourceId(responseId);
			}
		}

		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);
		Set<SummaryEnum> summaryMode = Collections.emptySet();

		return restfulResponse.streamResponseAsResource(outcome, prettyPrint, summaryMode, operationStatus, null, theRequest.isRespondGzip(), true);
//		return theRequest.getResponse().returnResponse(ParseAction.create(outcome), operationStatus, allowPrefer, response, getResourceName());
	}

	protected void streamOperationOutcome(BaseServerResponseException theE, RestfulServer theServer, EncodingEnum theEncodingNotNull, HttpServletResponse theResponse, RequestDetails theRequest) throws IOException {
		theResponse.setStatus(theE.getStatusCode());

		theServer.addHeadersToResponse(theResponse);

		if (theE.getOperationOutcome() != null) {
			theResponse.setContentType(theEncodingNotNull.getResourceContentType());
			IParser parser = theEncodingNotNull.newParser(theServer.getFhirContext());
			parser.setPrettyPrint(RestfulServerUtils.prettyPrintResponse(theServer, theRequest));
			Writer writer = theResponse.getWriter();
			try {
				parser.encodeResourceToWriter(theE.getOperationOutcome(), writer);
			} finally {
				writer.close();
			}
		} else {
			theResponse.setContentType(Constants.CT_TEXT);
			Writer writer = theResponse.getWriter();
			try {
				writer.append(theE.getMessage());
			} finally {
				writer.close();
			}
		}
	}

	protected static void parseContentLocation(FhirContext theContext, MethodOutcome theOutcomeToPopulate, String theLocationHeader) {
		if (StringUtils.isBlank(theLocationHeader)) {
			return;
		}

		IIdType id = theContext.getVersion().newIdType();
		id.setValue(theLocationHeader);
		theOutcomeToPopulate.setId(id);
	}

}
