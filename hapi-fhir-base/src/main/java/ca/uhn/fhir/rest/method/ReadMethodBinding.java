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
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.RestfulOperationTypeEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.method.SearchMethodBinding.RequestType;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ReadMethodBinding extends BaseResourceReturningMethodBinding implements IClientResponseHandlerHandlesBinary<Object> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadMethodBinding.class);

	private Integer myIdIndex;
	private boolean mySupportsVersion;
	private Integer myVersionIdIndex;

	public ReadMethodBinding(Class<? extends IResource> theAnnotatedResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theAnnotatedResourceType, theMethod, theContext, theProvider);

		Validate.notNull(theMethod, "Method must not be null");

		Integer idIndex = MethodUtil.findIdParameterIndex(theMethod);
		Integer versionIdIndex = MethodUtil.findVersionIdParameterIndex(theMethod);

		mySupportsVersion = theMethod.getAnnotation(Read.class).version();
		myIdIndex = idIndex;
		myVersionIdIndex = versionIdIndex;

		Class<?>[] parameterTypes = theMethod.getParameterTypes();
		if (!IdDt.class.equals(parameterTypes[myIdIndex])) {
			throw new ConfigurationException("ID parameter must be of type: " + IdDt.class.getCanonicalName() + " - Found: " + parameterTypes[myIdIndex]);
		}
		if (myVersionIdIndex != null && !IdDt.class.equals(parameterTypes[myVersionIdIndex])) {
			throw new ConfigurationException("Version ID parameter must be of type: " + IdDt.class.getCanonicalName() + " - Found: " + parameterTypes[myVersionIdIndex]);
		}

	}

	@Override
	public List<Class<?>> getAllowableParamAnnotations() {
		ArrayList<Class<?>> retVal = new ArrayList<Class<?>>();
		retVal.add(IdParam.class);
		return retVal;
	}

	@Override
	public RestfulOperationTypeEnum getResourceOperationType() {
		return isVread() ? RestfulOperationTypeEnum.VREAD : RestfulOperationTypeEnum.READ;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.RESOURCE;
	}

	@Override
	public RestfulOperationSystemEnum getSystemOperationType() {
		return null;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(Request theRequest) {
		if (!theRequest.getResourceName().equals(getResourceName())) {
			return false;
		}
		for (String next : theRequest.getParameters().keySet()) {
			if (!ALLOWED_PARAMS.contains(next)) {
				return false;
			}
		}
		if (theRequest.getId() == null) {
			return false;
		}
		if (mySupportsVersion == false) {
			if (theRequest.getId().hasVersionIdPart()) {
				return false;
			}
		}
		if (theRequest.getRequestType() != RequestType.GET) {
			ourLog.trace("Method {} doesn't match because request type is not GET: {}", theRequest.getId(), theRequest.getRequestType());
			return false;
		}
		if (Constants.PARAM_HISTORY.equals(theRequest.getOperation())) {
			if (mySupportsVersion == false && myVersionIdIndex == null) {
				return false;
			}
			if (theRequest.getId().hasVersionIdPart() == false) {
				return false;
			}
		} else if (!StringUtils.isBlank(theRequest.getOperation())) {
			return false;
		}
		return true;
	}

	@Override
	public HttpGetClientInvocation invokeClient(Object[] theArgs) {
		HttpGetClientInvocation retVal;
		IdDt id = ((IdDt) theArgs[myIdIndex]);
		if (myVersionIdIndex == null) {
			String resourceName = getResourceName();
			if (id.hasVersionIdPart()) {
				retVal = createVReadInvocation(new IdDt(resourceName, id.getIdPart(), id.getVersionIdPart()));
			} else {
				retVal = createReadInvocation(id, resourceName);
			}
		} else {
			IdDt vid = ((IdDt) theArgs[myVersionIdIndex]);
			String resourceName = getResourceName();

			retVal = createVReadInvocation(new IdDt(resourceName, id.getIdPart(), vid.getVersionIdPart()));
		}

		for (int idx = 0; idx < theArgs.length; idx++) {
			IParameter nextParam = getParameters().get(idx);
			nextParam.translateClientArgumentIntoQueryArgument(getContext(), theArgs[idx], null);
		}

		return retVal;
	}

	@Override
	public Object invokeClient(String theResponseMimeType, InputStream theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException,
			BaseServerResponseException {
		byte[] contents = IOUtils.toByteArray(theResponseReader);
		Binary resource = new Binary(theResponseMimeType, contents);

		switch (getMethodReturnType()) {
		case BUNDLE:
			return Bundle.withSingleResource(resource);
		case LIST_OF_RESOURCES:
			return Collections.singletonList(resource);
		case RESOURCE:
			return resource;
		case BUNDLE_PROVIDER:
			return new SimpleBundleProvider(resource);
		}

		throw new IllegalStateException("" + getMethodReturnType()); // should not happen
	}

	@Override
	public IBundleProvider invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		theMethodParams[myIdIndex] = theRequest.getId();
		if (myVersionIdIndex != null) {
			theMethodParams[myVersionIdIndex] = new IdDt(theRequest.getId().getVersionIdPart());
		}

		Object response = invokeServerMethod(theMethodParams);

		return toResourceList(response);
	}

	@Override
	public boolean isBinary() {
		return "Binary".equals(getResourceName());
	}

	public boolean isVread() {
		return mySupportsVersion || myVersionIdIndex != null;
	}

	public static HttpGetClientInvocation createAbsoluteReadInvocation(IdDt theId) {
		return new HttpGetClientInvocation(theId.getValue());
	}

	public static HttpGetClientInvocation createAbsoluteVReadInvocation(IdDt theId) {
		return new HttpGetClientInvocation(theId.getValue());
	}

	public static HttpGetClientInvocation createReadInvocation(IdDt theId, String theResourceName) {
		return new HttpGetClientInvocation(new IdDt(theResourceName, theId.getIdPart()).getValue());
	}

	public static HttpGetClientInvocation createVReadInvocation(IdDt theId) {
		return new HttpGetClientInvocation(theId.getValue());
	}

}
