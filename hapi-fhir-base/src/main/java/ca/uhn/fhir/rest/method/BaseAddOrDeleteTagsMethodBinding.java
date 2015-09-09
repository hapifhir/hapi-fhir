package ca.uhn.fhir.rest.method;

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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.TagListParam;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

abstract class BaseAddOrDeleteTagsMethodBinding extends BaseMethodBinding<Void> {

	private Class<? extends IBaseResource> myType;
	private Integer myIdParamIndex;
	private Integer myVersionIdParamIndex;
	private String myResourceName;
	private Integer myTagListParamIndex;

	public BaseAddOrDeleteTagsMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider, Class<? extends IResource> theTypeFromMethodAnnotation) {
		super(theMethod, theConetxt, theProvider);

		if (theProvider instanceof IResourceProvider) {
			myType = ((IResourceProvider) theProvider).getResourceType();
		} else {
			myType = theTypeFromMethodAnnotation;
		}

		if (Modifier.isInterface(myType.getModifiers())) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not specify a resource type, but has an @" + IdParam.class.getSimpleName() + " parameter. Please specity a resource type in the method annotation on this method");
		}
		
		myResourceName = theConetxt.getResourceDefinition(myType).getName();

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);
		myVersionIdParamIndex = MethodUtil.findVersionIdParameterIndex(theMethod);
		myTagListParamIndex = MethodUtil.findTagListParameterIndex(theMethod);

		if (myIdParamIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not have an @" + IdParam.class.getSimpleName() + " parameter.");
		}

		if (myTagListParamIndex == null) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not have a parameter of type " + TagList.class.getSimpleName() + ", or paramater is not annotated with the @" + TagListParam.class.getSimpleName() + " annotation");
		}

	}

	@Override
	public Void invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws BaseServerResponseException {
		switch (theResponseStatusCode) {
		case Constants.STATUS_HTTP_200_OK:
		case Constants.STATUS_HTTP_201_CREATED:
		case Constants.STATUS_HTTP_204_NO_CONTENT:
			return null;
		default:
			throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
		}

	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return null;
	}

	protected abstract boolean isDelete();

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpPostClientInvocation retVal;

		IdDt id = (IdDt) theArgs[myIdParamIndex];
		if (id == null || id.isEmpty()) {
			throw new InvalidRequestException("ID must not be null or empty for this operation");
		}

		IdDt versionId = null;
		if (myVersionIdParamIndex != null) {
			versionId = (IdDt) theArgs[myVersionIdParamIndex];
		}

		TagList tagList = (TagList) theArgs[myTagListParamIndex];

		Class<? extends IBaseResource> type = myType;
		assert type != null;

		if (isDelete()) {
			if (versionId != null) {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS, Constants.PARAM_DELETE);
			} else {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_TAGS, Constants.PARAM_DELETE);
			}
		} else {
			if (versionId != null) {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS);
			} else {
				retVal = new HttpPostClientInvocation(getContext(), tagList, getResourceName(), id.getValue(), Constants.PARAM_TAGS);
			}
		}
		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null, null);
		}

		return retVal;
	}

	@Override
	public void invokeServer(RestfulServer theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
		Object[] params = createParametersForServerRequest(theRequest, null);

		params[myIdParamIndex] = theRequest.getId();

		if (myVersionIdParamIndex != null) {
			params[myVersionIdParamIndex] = theRequest.getId();
		}

		IParser parser = createAppropriateParserForParsingServerRequest(theRequest);
		Reader reader = theRequest.getServletRequest().getReader();
		try {
			TagList tagList = parser.parseTagList(reader);
			params[myTagListParamIndex] = tagList;
		} finally {
			reader.close();
		}
		invokeServerMethod(theServer, theRequest, params);

		for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
			IServerInterceptor next = theServer.getInterceptors().get(i);
			boolean continueProcessing = next.outgoingResponse(theRequest, theRequest.getServletRequest(), theRequest.getServletResponse());
			if (!continueProcessing) {
				return;
			}
		}

		HttpServletResponse response = theRequest.getServletResponse();
		response.setContentType(Constants.CT_TEXT);
		response.setStatus(Constants.STATUS_HTTP_200_OK);
		response.setCharacterEncoding(Constants.CHARSET_NAME_UTF8);

		theServer.addHeadersToResponse(response);

		PrintWriter writer = response.getWriter();
		writer.close();
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (theRequest.getRequestType() != RequestTypeEnum.POST) {
			return false;
		}
		if (!Constants.PARAM_TAGS.equals(theRequest.getOperation())) {
			return false;
		}

		if (!myResourceName.equals(theRequest.getResourceName())) {
			return false;
		}

		if (theRequest.getId() == null) {
			return false;
		}

		if (isDelete()) {
			if (Constants.PARAM_DELETE.equals(theRequest.getSecondaryOperation()) == false) {
				return false;
			}
		} else {
			if (theRequest.getSecondaryOperation() != null) {
				return false;
			}
		}

		return true;
	}

}
