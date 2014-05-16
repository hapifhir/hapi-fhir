package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR Library
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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.param.BaseQueryParameter;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class SearchMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchMethodBinding.class);

	private Class<?> myDeclaredResourceType;
	private String myQueryName;

	public SearchMethodBinding(Class<? extends IResource> theReturnResourceType, Method theMethod, String theQueryName, FhirContext theContext, Object theProvider) {
		super(theReturnResourceType, theMethod, theContext, theProvider);
		this.myQueryName = StringUtils.defaultIfBlank(theQueryName, null);
		this.myDeclaredResourceType = theMethod.getReturnType();
	}

	public Class<?> getDeclaredResourceType() {
		return myDeclaredResourceType.getClass();
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return RestfulOperationTypeEnum.SEARCH_TYPE;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		assert (myQueryName == null || ((theArgs != null ? theArgs.length : 0) == getParameters().size())) : "Wrong number of arguments: " + (theArgs != null ? theArgs.length : "null");

		Map<String, List<String>> queryStringArgs = new LinkedHashMap<String, List<String>>();

		if (myQueryName != null) {
			queryStringArgs.put(Constants.PARAM_QUERY, Collections.singletonList(myQueryName));
		}

		String resourceName = getResourceName();
		GetClientInvocation retVal = createSearchInvocation(resourceName, queryStringArgs);

		if (theArgs != null) {
			for (int idx = 0; idx < theArgs.length; idx++) {
				IParameter nextParam = getParameters().get(idx);
				nextParam.translateClientArgumentIntoQueryArgument(theArgs[idx], queryStringArgs, retVal);
			}
		}

		return retVal;
	}

	public static GetClientInvocation createSearchInvocation(String theResourceName, Map<String, List<String>> theParameters) {
		return new GetClientInvocation(theParameters, theResourceName);
	}

	@Override
	public List<IResource> invokeServer(Request theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		assert theRequest.getId() == null;
		assert theRequest.getVersionId() == null;

		Object response = invokeServerMethod(theMethodParams);

		return toResourceList(response);

	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			ourLog.trace("Method {} doesn't match because resource name {} != {}", getMethod().getName(), theRequest.getResourceName(), getResourceName());
			return false;
		}
		if (theRequest.getId() != null || theRequest.getVersionId() != null) {
			ourLog.trace("Method {} doesn't match because ID or Version are not null: {} - {}", theRequest.getId(), theRequest.getVersionId());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.GET && theRequest.getOperation() != null && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is GET but operation is not null: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() == RequestType.POST && !Constants.PARAM_SEARCH.equals(theRequest.getOperation())) {
			ourLog.trace("Method {} doesn't match because request type is POST but operation is not _search: {}", theRequest.getId(), theRequest.getOperation());
			return false;
		}
		if (theRequest.getRequestType() != RequestType.GET && theRequest.getRequestType() != RequestType.POST) {
			ourLog.trace("Method {} doesn't match because request type is {}", theRequest.getOperation());
			return false;
		}

		// This is used to track all the parameters so we can reject queries that 
		// have additional params we don't understand
		Set<String> methodParamsTemp = new HashSet<String>();
		
		Set<String> unqualifiedNames = theRequest.getUnqualifiedToQualifiedNames().keySet();
		Set<String> qualifiedParamNames = theRequest.getParameters().keySet();
		for (int i = 0; i < this.getParameters().size(); i++) {
			if (!(getParameters().get(i) instanceof BaseQueryParameter)) {
				continue;
			}
			BaseQueryParameter temp = (BaseQueryParameter) getParameters().get(i);
			String name = temp.getName();
			if (temp.isRequired()) {

				if (qualifiedParamNames.contains(name)) {
					methodParamsTemp.add(name);
				} else if (unqualifiedNames.contains(name)) {
					methodParamsTemp.addAll(theRequest.getUnqualifiedToQualifiedNames().get(name));
				} else {
					ourLog.trace("Method {} doesn't match param '{}' is not present", getMethod().getName(), name);
					return false;

				}
			}
		}
		if (myQueryName != null) {
			String[] queryNameValues = theRequest.getParameters().get(Constants.PARAM_QUERY);
			if (queryNameValues != null && StringUtils.isNotBlank(queryNameValues[0])) {
				String queryName = queryNameValues[0];
				if (!myQueryName.equals(queryName)) {
					ourLog.trace("Query name does not match {}", myQueryName);
					return false;
				} else {
					methodParamsTemp.add(Constants.PARAM_QUERY);
				}
			} else {
				ourLog.trace("Query name does not match {}", myQueryName);
				return false;
			}
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (ALLOWED_PARAMS.contains(next)) {
				methodParamsTemp.add(next);
			}
		}
		boolean retVal = methodParamsTemp.containsAll(theRequest.getParameters().keySet());

		ourLog.trace("Method {} matches: {}", getMethod().getName(), retVal);

		return retVal;
	}

	public void setResourceType(Class<?> resourceType) {
		this.myDeclaredResourceType = resourceType;
	}

	public static enum RequestType {
		DELETE, GET, OPTIONS, POST, PUT
	}

}
