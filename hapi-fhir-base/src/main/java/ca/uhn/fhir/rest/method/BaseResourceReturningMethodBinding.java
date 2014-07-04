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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.param.IParameter;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;

abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding<Object> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReturningMethodBinding.class);
	protected static final Set<String> ALLOWED_PARAMS;
	static {
		HashSet<String> set = new HashSet<String>();
		set.add(Constants.PARAM_FORMAT);
		set.add(Constants.PARAM_NARRATIVE);
		set.add(Constants.PARAM_PRETTY);
		ALLOWED_PARAMS = Collections.unmodifiableSet(set);
	}

	private MethodReturnTypeEnum myMethodReturnType;
	private String myResourceName;
	private Class<? extends IResource> myResourceType;

	public BaseResourceReturningMethodBinding(Class<? extends IResource> theReturnResourceType, Method theMethod, FhirContext theConetxt, Object theProvider) {
		super(theMethod, theConetxt, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();
		if (Collection.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
		} else if (IResource.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.RESOURCE;
		} else if (Bundle.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE;
		} else if (IBundleProvider.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE_PROVIDER;
		} else {
			throw new ConfigurationException("Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		myResourceType = theReturnResourceType;
		if (theReturnResourceType != null) {
			ResourceDef resourceDefAnnotation = theReturnResourceType.getAnnotation(ResourceDef.class);
			if (resourceDefAnnotation == null) {
				throw new ConfigurationException(theReturnResourceType.getCanonicalName() + " has no @" + ResourceDef.class.getSimpleName() + " annotation");
			}
			myResourceName = resourceDefAnnotation.name();
		}
	}

	public MethodReturnTypeEnum getMethodReturnType() {
		return myMethodReturnType;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	public abstract ReturnTypeEnum getReturnType();

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException {
		IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseReader, theResponseStatusCode);

		switch (getReturnType()) {
		case BUNDLE: {
			Bundle bundle;
			if (myResourceType != null) {
				bundle = parser.parseBundle(myResourceType, theResponseReader);
			} else {
				bundle = parser.parseBundle(theResponseReader);
			}
			switch (getMethodReturnType()) {
			case BUNDLE:
				return bundle;
			case LIST_OF_RESOURCES:
				return bundle.toListOfResources();
			case RESOURCE:
				List<IResource> list = bundle.toListOfResources();
				if (list.size() == 0) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new InvalidResponseException(theResponseStatusCode, "FHIR server call returned a bundle with multiple resources, but this method is only able to returns one.");
				}
			case BUNDLE_PROVIDER:
				throw new IllegalStateException("Return type of " + IBundleProvider.class.getSimpleName() + " is not supported in clients");
			}
			break;
		}
		case RESOURCE: {
			IResource resource;
			if (myResourceType != null) {
				resource = parser.parseResource(myResourceType, theResponseReader);
			} else {
				resource = parser.parseResource(theResponseReader);
			}

			List<String> lmHeaders = theHeaders.get(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
			if (lmHeaders != null && lmHeaders.size() > 0 && StringUtils.isNotBlank(lmHeaders.get(0))) {
				String headerValue = lmHeaders.get(0);
				Date headerDateValue;
				try {
					headerDateValue = DateUtils.parseDate(headerValue);
					InstantDt lmValue = new InstantDt(headerDateValue);
					resource.getResourceMetadata().put(ResourceMetadataKeyEnum.UPDATED, lmValue);
				} catch (Exception e) {
					ourLog.warn("Unable to parse date string '{}'. Error is: {}", headerValue, e.toString());
				}
			}

			switch (getMethodReturnType()) {
			case BUNDLE:
				return Bundle.withSingleResource(resource);
			case LIST_OF_RESOURCES:
				return Collections.singletonList(resource);
			case RESOURCE:
				return resource;
			case BUNDLE_PROVIDER:
				throw new IllegalStateException("Return type of " + IBundleProvider.class.getSimpleName() + " is not supported in clients");
			}
			break;
		}
		}

		throw new IllegalStateException("Should not get here!");
	}

	public abstract IBundleProvider invokeServer(Request theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException;

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest, HttpServletResponse theResponse) throws BaseServerResponseException, IOException {

		// Pretty print
		boolean prettyPrint = RestfulServer.prettyPrintResponse(theRequest);

		// Narrative mode
		NarrativeModeEnum narrativeMode = RestfulServer.determineNarrativeMode(theRequest);

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServer.determineResponseEncoding(theRequest.getServletRequest());

		// Is this request coming from a browser
		String uaHeader = theRequest.getServletRequest().getHeader("user-agent");
		boolean requestIsBrowser = false;
		if (uaHeader != null && uaHeader.contains("Mozilla")) {
			requestIsBrowser = true;
		}

		Object requestObject = parseRequestObject(theRequest);

		// Method params
		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param != null) {
				params[i] = param.translateQueryParametersIntoServerArgument(theRequest, requestObject);
			}
		}

		Integer count = RestfulServer.extractCountParameter(theRequest.getServletRequest());

		boolean respondGzip=theRequest.isRespondGzip();

		IBundleProvider result = invokeServer(theRequest, params);
		switch (getReturnType()) {
		case BUNDLE:
			RestfulServer.streamResponseAsBundle(theServer, theResponse, result, responseEncoding, theRequest.getFhirServerBase(), theRequest.getCompleteUrl(), prettyPrint, requestIsBrowser, narrativeMode, 0, count, null, respondGzip);
			break;
		case RESOURCE:
			if (result.size() == 0) {
				throw new ResourceNotFoundException(theRequest.getId());
			} else if (result.size() > 1) {
				throw new InternalErrorException("Method returned multiple resources");
			}
			RestfulServer.streamResponseAsResource(theServer, theResponse, result.getResources(0, 1).get(0), responseEncoding, prettyPrint, requestIsBrowser, narrativeMode, respondGzip);
			break;
		}
	}


	/**
	 * Subclasses may override
	 * 
	 * @throws IOException
	 *             Subclasses may throw this in the event of an IO exception
	 */
	protected Object parseRequestObject(Request theRequest) throws IOException {
		return null;
	}

	public enum MethodReturnTypeEnum {
		BUNDLE, LIST_OF_RESOURCES, RESOURCE, BUNDLE_PROVIDER
	}

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}

}
