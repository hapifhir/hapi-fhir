package ca.uhn.fhir.rest.method;

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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.annotation.GetTags;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;

public class GetTagsMethodBinding extends BaseMethodBinding<TagList> {

	private Class<? extends IResource> myType;
	private Integer myIdParamIndex;
	private Integer myVersionIdParamIndex;
	private String myResourceName;

	public GetTagsMethodBinding(Method theMethod, FhirContext theConetxt, Object theProvider, GetTags theAnnotation) {
		super(theMethod, theConetxt, theProvider);

		if (theProvider instanceof IResourceProvider) {
			myType = ((IResourceProvider) theProvider).getResourceType();
		} else {
			myType = theAnnotation.type();
		}
		
		if (!IResource.class.equals(myType)) {
			myResourceName = theConetxt.getResourceDefinition(myType).getName();
		}

		myIdParamIndex = MethodUtil.findIdParameterIndex(theMethod);
		myVersionIdParamIndex = MethodUtil.findVersionIdParameterIndex(theMethod);

		if (myIdParamIndex != null && myType.equals(IResource.class)) {
			throw new ConfigurationException("Method '" + theMethod.getName() + "' does not specify a resource type, but has an @" + IdParam.class.getSimpleName() + " parameter. Please specity a resource type in the @" + GetTags.class.getSimpleName() + " annotation");
		}
	}

	@Override
	public TagList invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException {
		if (theResponseStatusCode == Constants.STATUS_HTTP_200_OK) {
			IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseReader, theResponseStatusCode);
			TagList retVal = parser.parseTagList(theResponseReader);
			return retVal;
		} else {
			throw processNon2xxResponseAndReturnExceptionToThrow(theResponseStatusCode, theResponseMimeType, theResponseReader);
		}

	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return null;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		HttpGetClientInvocation retVal;

		IdDt id = null;
		IdDt versionId = null;
		if (myIdParamIndex != null) {
			id = (IdDt) theArgs[myIdParamIndex];
			if (myVersionIdParamIndex != null) {
				versionId = (IdDt) theArgs[myVersionIdParamIndex];
			}
		}

		if (myType != IResource.class) {
			if (id != null) {
				if (versionId != null) {
					retVal = new HttpGetClientInvocation(getResourceName(), id.getIdPart(), Constants.PARAM_HISTORY, versionId.getValue(), Constants.PARAM_TAGS);
				} else if (id.hasVersionIdPart()){
					retVal = new HttpGetClientInvocation(getResourceName(), id.getIdPart(), Constants.PARAM_HISTORY, id.getVersionIdPart(), Constants.PARAM_TAGS);
				} else {
					retVal = new HttpGetClientInvocation(getResourceName(), id.getIdPart(), Constants.PARAM_TAGS);
				}
			} else {
				retVal = new HttpGetClientInvocation(getResourceName(), Constants.PARAM_TAGS);
			}
		} else {
			retVal = new HttpGetClientInvocation(Constants.PARAM_TAGS);
		}

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null);
			}
		}

		return retVal;
	}

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest) throws BaseServerResponseException, IOException {
		Object[] params = createParametersForServerRequest(theRequest, null);

		if (myIdParamIndex != null) {
			params[myIdParamIndex] = theRequest.getId();
		}
		if (myVersionIdParamIndex != null) {
			params[myVersionIdParamIndex] = theRequest.getId();
		}

		TagList resp = (TagList) invokeServerMethod(params);

		for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
			IServerInterceptor next = theServer.getInterceptors().get(i);
			boolean continueProcessing = next.outgoingResponse(theRequest, resp, theRequest.getServletRequest(), theRequest.getServletResponse());
			if (!continueProcessing) {
				return;
			}
		}
		
		EncodingEnum responseEncoding = RestfulServer.determineResponseEncoding(theRequest.getServletRequest());

		HttpServletResponse response = theRequest.getServletResponse();
		response.setContentType(responseEncoding.getResourceContentType());
		response.setStatus(Constants.STATUS_HTTP_200_OK);
		response.setCharacterEncoding(Constants.CHARSET_UTF_8);

		theServer.addHeadersToResponse(response);

		IParser parser = responseEncoding.newParser(getContext());
		parser.setPrettyPrint(RestfulServer.prettyPrintResponse(theRequest));
		PrintWriter writer = response.getWriter();
		try {
			parser.encodeTagListToWriter(resp, writer);
		} finally {
			writer.close();
		}
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (theRequest.getRequestType()!=RequestType.GET) {
			return false;
		}
		if (!Constants.PARAM_TAGS.equals(theRequest.getOperation())) {
			return false;
		}
		if (myResourceName == null) {
			if (getResourceName() != null) {
				return false;
			}
		} else if (!myResourceName.equals(theRequest.getResourceName())) {
			return false;

		}
		if ((myIdParamIndex != null) != (theRequest.getId() != null)) {
			return false;
		}
//		if ((myVersionIdParamIndex != null) != (theRequest.getVersionId() != null)) {
//			return false;
//		}
		return true;
	}

	@Override
	public OtherOperationTypeEnum getOtherOperationType() {
		return OtherOperationTypeEnum.GET_TAGS;
	}

}
