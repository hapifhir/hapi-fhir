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
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.GetClientInvocation;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ReadMethodBinding extends BaseResourceReturningMethodBinding {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadMethodBinding.class);

	private Integer myIdIndex;
	private Integer myVersionIdIndex;
	private int myParameterCount;

	public ReadMethodBinding(Class<? extends IResource> theAnnotatedResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theAnnotatedResourceType, theMethod, theContext, theProvider);

		Validate.notNull(theMethod, "Method must not be null");

		Integer idIndex = Util.findIdParameterIndex(theMethod);
		Integer versionIdIndex = Util.findVersionIdParameterIndex(theMethod);

		myIdIndex = idIndex;
		myVersionIdIndex = versionIdIndex;
		myParameterCount = getMethod().getParameterTypes().length;

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		if (!IdDt.class.equals(parameterTypes[myIdIndex])) {
			throw new ConfigurationException("ID parameter must be of type: " + IdDt.class.getCanonicalName() + " - Found: " + parameterTypes[myIdIndex]);
		}
		if (myVersionIdIndex != null && !IdDt.class.equals(parameterTypes[myVersionIdIndex])) {
			throw new ConfigurationException("Version ID parameter must be of type: " + IdDt.class.getCanonicalName() + " - Found: " + parameterTypes[myVersionIdIndex]);
		}

	}

	public boolean isVread() {
		return myVersionIdIndex != null;
	}

	@Override
	public boolean matches(Request theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			return false;
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (!ALLOWED_PARAMS.contains(next)) {
				return false;
			}
		}
		if ((theRequest.getVersion() == null) != (myVersionIdIndex == null)) {
			return false;
		}
		if (theRequest.getId() == null) {
			return false;
		}
		if (theRequest.getRequestType() != RequestType.GET) {
			ourLog.trace("Method {} doesn't match because request type is not GET: {}", theRequest.getId(), theRequest.getRequestType());
			return false;
		}
		if (Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			if (myVersionIdIndex == null) {
				return false;
			}
		} else if (!StringUtils.isBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public List<IResource> invokeServer(Object theResourceProvider, Request theRequest) throws InvalidRequestException, InternalErrorException {
		Object[] params = new Object[myParameterCount];
		params[myIdIndex] = theRequest.getId();
		if (myVersionIdIndex != null) {
			params[myVersionIdIndex] = theRequest.getVersion();
		}

		Object response;
		try {
			response = getMethod().invoke(theResourceProvider, params);
		} catch (Exception e) {
			throw new InternalErrorException("Failed to call access method", e);
		}

		return toResourceList(response);
	}

	@Override
	public GetClientInvocation invokeClient(Object[] theArgs) {
		String id = ((IdDt) theArgs[myIdIndex]).getValue();
		if (myVersionIdIndex == null) {
			return new GetClientInvocation(getResourceName(), id);
		} else {
			String vid = ((IdDt) theArgs[myVersionIdIndex]).getValue();
			return new GetClientInvocation(getResourceName(), id, Constants.URL_TOKEN_HISTORY, vid);
		}
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return isVread() ? RestfulOperationTypeEnum.VREAD : RestfulOperationTypeEnum.READ;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

}
