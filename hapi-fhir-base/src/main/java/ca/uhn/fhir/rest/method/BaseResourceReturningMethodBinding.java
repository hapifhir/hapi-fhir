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
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.model.api.Include;
import org.hl7.fhir.instance.model.IBaseResource;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.RestfulServer.NarrativeModeEnum;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.util.ReflectionUtil;

abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding<Object> {
	protected static final Set<String> ALLOWED_PARAMS;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReturningMethodBinding.class);

	static {
		HashSet<String> set = new HashSet<String>();
		set.add(Constants.PARAM_FORMAT);
		set.add(Constants.PARAM_NARRATIVE);
		set.add(Constants.PARAM_PRETTY);
		set.add(Constants.PARAM_SORT);
		set.add(Constants.PARAM_SORT_ASC);
		set.add(Constants.PARAM_SORT_DESC);
		set.add(Constants.PARAM_COUNT);
		ALLOWED_PARAMS = Collections.unmodifiableSet(set);
	}

	private MethodReturnTypeEnum myMethodReturnType;
	private Class<?> myResourceListCollectionType;
	private String myResourceName;
	private Class<? extends IResource> myResourceType;

	@SuppressWarnings("unchecked")
	public BaseResourceReturningMethodBinding(Class<?> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();
		if (Collection.class.isAssignableFrom(methodReturnType)) {

			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
			Class<?> collectionType = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (collectionType != null) {
				if (!Object.class.equals(collectionType) && !IResource.class.isAssignableFrom(collectionType)) {
					throw new ConfigurationException("Method " + theMethod.getDeclaringClass().getSimpleName() + "#" + theMethod.getName() + " returns an invalid collection generic type: "
							+ collectionType);
				}
			}
			myResourceListCollectionType = collectionType;

		} else if (IBaseResource.class.isAssignableFrom(methodReturnType)) {
			if (Modifier.isAbstract(methodReturnType.getModifiers()) == false && theContext.getResourceDefinition((Class<? extends IBaseResource>) methodReturnType).isBundle()) {
				myMethodReturnType = MethodReturnTypeEnum.BUNDLE_RESOURCE;
			} else {
				myMethodReturnType = MethodReturnTypeEnum.RESOURCE;
			}
		} else if (Bundle.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE;
		} else if (IBundleProvider.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE_PROVIDER;
		} else {
			throw new ConfigurationException("Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: "
					+ theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			if (IResource.class.isAssignableFrom(theReturnResourceType)) {
				ResourceDef resourceDefAnnotation = theReturnResourceType.getAnnotation(ResourceDef.class);
				if (resourceDefAnnotation == null) {
					if (Modifier.isAbstract(theReturnResourceType.getModifiers())) {
						// If we're returning an abstract type, that's ok
					} else {
						throw new ConfigurationException(theReturnResourceType.getCanonicalName() + " has no @" + ResourceDef.class.getSimpleName() + " annotation");
					}
				} else {
					myResourceType = (Class<? extends IResource>) theReturnResourceType;
					myResourceName = resourceDefAnnotation.name();
				}
			}
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
				List<IResource> listOfResources;
				if (myResourceListCollectionType != null) {
					listOfResources = new ArrayList<IResource>();
					for (IResource next : bundle.toListOfResources()) {
						if (!myResourceListCollectionType.isAssignableFrom(next.getClass())) {
							ourLog.debug("Not returning resource of type {} because it is not a subclass or instance of {}", next.getClass(), myResourceListCollectionType);
							continue;
						}
						listOfResources.add(next);
					}
				} else {
					listOfResources = bundle.toListOfResources();
				}
				return listOfResources;
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

			MethodUtil.parseClientRequestResourceHeaders(null, theHeaders, resource);

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

	public abstract Object invokeServer(RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException;

	@Override
	public void invokeServer(RestfulServer theServer, Request theRequest) throws BaseServerResponseException, IOException {

		// Pretty print
		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theRequest);

		// Narrative mode
		NarrativeModeEnum narrativeMode = RestfulServerUtils.determineNarrativeMode(theRequest);

		// Determine response encoding
		EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest.getServletRequest());

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

		Integer count = RestfulServerUtils.extractCountParameter(theRequest.getServletRequest());

		boolean respondGzip = theRequest.isRespondGzip();

		HttpServletResponse response = theRequest.getServletResponse();
		Object resultObj = invokeServer(theRequest, params);
		switch (getReturnType()) {
		case BUNDLE: {

			if (getMethodReturnType() == MethodReturnTypeEnum.BUNDLE_RESOURCE) {
				IResource resource;
				if (resultObj instanceof IBundleProvider) {
					IBundleProvider result = (IBundleProvider) resultObj;
					resource = result.getResources(0, 1).get(0);
				} else {
					resource = (IResource) resultObj;
				}
				
				/*
				 * We assume that the bundle we got back from the handling method
				 * may not have everything populated (e.g. self links, bundle type,
				 * etc) so we do that here.
				 */
				IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();
				bundleFactory.initializeWithBundleResource(resource);
				bundleFactory.addRootPropertiesToBundle(null, theRequest.getFhirServerBase(), theRequest.getCompleteUrl(), count, getResponseBundleType());
				
				RestfulServerUtils.streamResponseAsResource(theServer, response, resource, responseEncoding, prettyPrint, requestIsBrowser, narrativeMode, respondGzip, theRequest.getFhirServerBase());
				break;
			} else {
                Set<Include> includes = getRequestIncludesFromParams(params);

				IBundleProvider result = (IBundleProvider) resultObj;
				IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();
				bundleFactory.initializeBundleFromBundleProvider(theServer, result, responseEncoding, theRequest.getFhirServerBase(), theRequest.getCompleteUrl(), prettyPrint, 0, count, null,
						getResponseBundleType(), includes);
				Bundle bundle = bundleFactory.getDstu1Bundle();
				if (bundle != null) {
					for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
						IServerInterceptor next = theServer.getInterceptors().get(i);
						boolean continueProcessing = next.outgoingResponse(theRequest, bundle, theRequest.getServletRequest(), theRequest.getServletResponse());
						if (!continueProcessing) {
							ourLog.debug("Interceptor {} returned false, not continuing processing");
							return;
						}
					}
					RestfulServerUtils.streamResponseAsBundle(theServer, response, bundle, responseEncoding, theRequest.getFhirServerBase(), prettyPrint, narrativeMode, respondGzip, requestIsBrowser);
				} else {
					IBaseResource resBundle = bundleFactory.getResourceBundle();
					for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
						IServerInterceptor next = theServer.getInterceptors().get(i);
						boolean continueProcessing = next.outgoingResponse(theRequest, resBundle, theRequest.getServletRequest(), theRequest.getServletResponse());
						if (!continueProcessing) {
							ourLog.debug("Interceptor {} returned false, not continuing processing");
							return;
						}
					}
					RestfulServerUtils.streamResponseAsResource(theServer, response, (IResource) resBundle, responseEncoding, prettyPrint, requestIsBrowser, narrativeMode,
							Constants.STATUS_HTTP_200_OK, theRequest.isRespondGzip(), theRequest.getFhirServerBase());
				}

				break;
			}
		}
		case RESOURCE: {
			IBundleProvider result = (IBundleProvider) resultObj;
			if (result.size() == 0) {
				throw new ResourceNotFoundException(theRequest.getId());
			} else if (result.size() > 1) {
				throw new InternalErrorException("Method returned multiple resources");
			}

			IResource resource = result.getResources(0, 1).get(0);

			for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = theServer.getInterceptors().get(i);
				boolean continueProcessing = next.outgoingResponse(theRequest, resource, theRequest.getServletRequest(), theRequest.getServletResponse());
				if (!continueProcessing) {
					return;
				}
			}

			RestfulServerUtils.streamResponseAsResource(theServer, response, resource, responseEncoding, prettyPrint, requestIsBrowser, narrativeMode, respondGzip, theRequest.getFhirServerBase());
			break;
		}
		}
	}

	/**
	 * If the response is a bundle, this type will be placed in the root of the bundle (can be null)
	 */
	protected abstract BundleTypeEnum getResponseBundleType();

	/**
	 * Subclasses may override
	 * 
	 * @param theRequest
	 *            The incoming request
	 * @throws IOException
	 *             Subclasses may throw this in the event of an IO exception
	 */
	protected Object parseRequestObject(Request theRequest) throws IOException {
		return null;
	}

	public enum MethodReturnTypeEnum {
		BUNDLE, BUNDLE_PROVIDER, LIST_OF_RESOURCES, RESOURCE, BUNDLE_RESOURCE
	}

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}

}
