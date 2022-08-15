package ca.uhn.fhir.rest.server.method;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.GraphQLQueryBody;
import ca.uhn.fhir.rest.annotation.GraphQLQueryUrl;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.param.ParameterUtil;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class GraphQLMethodBinding extends OperationMethodBinding {

	private final Integer myIdParamIndex;
	private final Integer myQueryUrlParamIndex;
	private final Integer myQueryBodyParamIndex;
	private final RequestTypeEnum myMethodRequestType;

	public GraphQLMethodBinding(Method theMethod, RequestTypeEnum theMethodRequestType, FhirContext theContext, Object theProvider) {
		super(null, null, theMethod, theContext, theProvider, true, Constants.OPERATION_NAME_GRAPHQL, null, null, null, null, true);

		myIdParamIndex = ParameterUtil.findIdParameterIndex(theMethod, theContext);
		myQueryUrlParamIndex = ParameterUtil.findParamAnnotationIndex(theMethod, GraphQLQueryUrl.class);
		myQueryBodyParamIndex = ParameterUtil.findParamAnnotationIndex(theMethod, GraphQLQueryBody.class);
		myMethodRequestType = theMethodRequestType;
	}

	@Override
	public String getResourceName() {
		return null;
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.GRAPHQL_REQUEST;
	}

	@Override
	public RestOperationTypeEnum getRestOperationType(RequestDetails theRequestDetails) {
		return getRestOperationType();
	}

	@Override
	protected Set<Class<?>> provideExpectedReturnTypes() {
		return Collections.singleton(String.class);
	}

	@Override
	public boolean isCanOperateAtServerLevel() {
		return true;
	}

	@Override
	public boolean isCanOperateAtTypeLevel() {
		return false;
	}

	@Override
	public boolean isCanOperateAtInstanceLevel() {
		return myIdParamIndex != null;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		if (Constants.OPERATION_NAME_GRAPHQL.equals(theRequest.getOperation()) && myMethodRequestType.equals(theRequest.getRequestType())) {
			return MethodMatchEnum.EXACT;
		}

		return MethodMatchEnum.NONE;
	}

	private String getQueryValue(Object[] methodParams) {
		switch (myMethodRequestType) {
			case POST:
				Validate.notNull(myQueryBodyParamIndex, "GraphQL method does not have @" + GraphQLQueryBody.class.getSimpleName() + " parameter");
				return (String) methodParams[myQueryBodyParamIndex];
			case GET:
				Validate.notNull(myQueryUrlParamIndex, "GraphQL method does not have @" + GraphQLQueryUrl.class.getSimpleName() + " parameter");
				return (String) methodParams[myQueryUrlParamIndex];
		}
		return null;
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
		Object[] methodParams = createMethodParams(theRequest);
		if (myIdParamIndex != null) {
			methodParams[myIdParamIndex] = theRequest.getId();
		}

		String responseString = (String) invokeServerMethod(theRequest, methodParams);

		int statusCode = Constants.STATUS_HTTP_200_OK;
		String statusMessage = Constants.HTTP_STATUS_NAMES.get(statusCode);
		String contentType = Constants.CT_JSON;
		String charset = Constants.CHARSET_NAME_UTF8;
		boolean respondGzip = theRequest.isRespondGzip();

		HttpServletRequest servletRequest = null;
		HttpServletResponse servletResponse = null;
		if (theRequest instanceof ServletRequestDetails) {
			servletRequest = ((ServletRequestDetails) theRequest).getServletRequest();
			servletResponse = ((ServletRequestDetails) theRequest).getServletResponse();
		}

		String graphQLQuery = getQueryValue(methodParams);
		// Interceptor call: SERVER_OUTGOING_GRAPHQL_RESPONSE
		HookParams params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(String.class, graphQLQuery)
			.add(String.class, responseString)
			.add(HttpServletRequest.class, servletRequest)
			.add(HttpServletResponse.class, servletResponse);
		if (!theRequest.getInterceptorBroadcaster().callHooks(Pointcut.SERVER_OUTGOING_GRAPHQL_RESPONSE, params)) {
			return null;
		}

		// Interceptor call: SERVER_OUTGOING_RESPONSE
		params = new HookParams()
			.add(RequestDetails.class, theRequest)
			.addIfMatchesType(ServletRequestDetails.class, theRequest)
			.add(IBaseResource.class, null)
			.add(ResponseDetails.class, new ResponseDetails())
			.add(HttpServletRequest.class, servletRequest)
			.add(HttpServletResponse.class, servletResponse);
		if (!theRequest.getInterceptorBroadcaster().callHooks(Pointcut.SERVER_OUTGOING_RESPONSE, params)) {
			return null;
		}

		// Write the response
		Writer writer = theRequest.getResponse().getResponseWriter(statusCode, statusMessage, contentType, charset, respondGzip);
		writer.write(responseString);
		writer.close();

		return null;
	}
}
