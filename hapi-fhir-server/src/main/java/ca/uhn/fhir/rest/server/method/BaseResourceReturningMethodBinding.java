/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ReflectionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding {
	protected final ResponseBundleBuilder myResponseBundleBuilder;

	private MethodReturnTypeEnum myMethodReturnType;
	private String myResourceName;

	@SuppressWarnings("unchecked")
	public BaseResourceReturningMethodBinding(
			Class<?> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();

		Set<Class<?>> expectedReturnTypes = provideExpectedReturnTypes();
		if (expectedReturnTypes != null) {

			Validate.isTrue(
					expectedReturnTypes.contains(methodReturnType),
					"Unexpected method return type on %s - Allowed: %s",
					theMethod,
					expectedReturnTypes);

		} else if (Collection.class.isAssignableFrom(methodReturnType)) {

			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
			Class<?> collectionType = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (collectionType != null) {
				if (!Object.class.equals(collectionType) && !IBaseResource.class.isAssignableFrom(collectionType)) {
					throw new ConfigurationException(Msg.code(433) + "Method "
							+ theMethod.getDeclaringClass().getSimpleName() + "#" + theMethod.getName()
							+ " returns an invalid collection generic type: " + collectionType);
				}
			}

		} else if (IBaseResource.class.isAssignableFrom(methodReturnType)) {

			if (IBaseBundle.class.isAssignableFrom(methodReturnType)) {
				myMethodReturnType = MethodReturnTypeEnum.BUNDLE_RESOURCE;
			} else {
				myMethodReturnType = MethodReturnTypeEnum.RESOURCE;
			}
		} else if (IBundleProvider.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.BUNDLE_PROVIDER;
		} else if (MethodOutcome.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.METHOD_OUTCOME;
		} else if (void.class.equals(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.VOID;
		} else {
			throw new ConfigurationException(Msg.code(434) + "Invalid return type '"
					+ methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: "
					+ theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			if (IBaseResource.class.isAssignableFrom(theReturnResourceType)) {

				// If we're returning an abstract type, that's ok, but if we know the resource
				// type let's grab it
				if (!Modifier.isAbstract(theReturnResourceType.getModifiers())
						&& !Modifier.isInterface(theReturnResourceType.getModifiers())) {
					Class<? extends IBaseResource> resourceType = (Class<? extends IResource>) theReturnResourceType;
					RuntimeResourceDefinition resourceDefinition = theContext.getResourceDefinition(resourceType);
					myResourceName = resourceDefinition.getName();
				}
			}
		}

		myResponseBundleBuilder = new ResponseBundleBuilder(isOffsetModeHistory());
	}

	/**
	 * Subclasses may override
	 */
	protected Set<Class<?>> provideExpectedReturnTypes() {
		return null;
	}

	protected boolean isOffsetModeHistory() {
		return false;
	}

	public IBaseResource doInvokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) {
		Object[] params = createMethodParams(theRequest);

		Object resultObj = invokeServer(theServer, theRequest, params);
		if (resultObj == null) {
			return null;
		}

		Integer count = RestfulServerUtils.extractCountParameter(theRequest);

		final IBaseResource responseObject;

		switch (getReturnType()) {
			case BUNDLE: {

				/*
				 * Figure out the self-link for this request
				 */

				BundleTypeEnum responseBundleType = getResponseBundleType();
				BundleLinks bundleLinks = new BundleLinks(
						theRequest.getServerBaseForRequest(),
						null,
						RestfulServerUtils.prettyPrintResponse(theServer, theRequest),
						responseBundleType);
				String linkSelf = RestfulServerUtils.createLinkSelf(theRequest.getFhirServerBase(), theRequest);
				bundleLinks.setSelf(linkSelf);

				if (getMethodReturnType() == MethodReturnTypeEnum.BUNDLE_RESOURCE) {
					IBaseResource resource;
					IPrimitiveType<Date> lastUpdated;
					if (resultObj instanceof IBundleProvider) {
						IBundleProvider result = (IBundleProvider) resultObj;
						resource = result.getResources(0, 1).get(0);
						lastUpdated = result.getPublished();
					} else {
						resource = (IBaseResource) resultObj;
						lastUpdated = theServer.getFhirContext().getVersion().getLastUpdated(resource);
					}

					/*
					 * We assume that the bundle we got back from the handling method may not have everything populated (e.g. self links, bundle type, etc) so we do that here.
					 */
					IVersionSpecificBundleFactory bundleFactory =
							theServer.getFhirContext().newBundleFactory();
					bundleFactory.initializeWithBundleResource(resource);
					bundleFactory.addRootPropertiesToBundle(null, bundleLinks, count, lastUpdated);

					responseObject = resource;
				} else {
					ResponseBundleRequest responseBundleRequest = buildResponseBundleRequest(
							theServer,
							theRequest,
							params,
							(IBundleProvider) resultObj,
							count,
							responseBundleType,
							linkSelf);
					responseObject = myResponseBundleBuilder.buildResponseBundle(responseBundleRequest);
				}
				break;
			}
			case RESOURCE: {
				IBundleProvider result = (IBundleProvider) resultObj;
				Integer size = result.size();
				if (size == null || size == 0) {
					throw new ResourceNotFoundException(
							Msg.code(436) + "Resource " + theRequest.getId() + " is not known");
				} else if (size > 1) {
					throw new InternalErrorException(Msg.code(437) + "Method returned multiple resources");
				}

				responseObject = result.getResources(0, 1).get(0);
				break;
			}
			default:
				throw new IllegalStateException(Msg.code(438)); // should not happen
		}
		return responseObject;
	}

	private ResponseBundleRequest buildResponseBundleRequest(
			IRestfulServer<?> theServer,
			RequestDetails theRequest,
			Object[] theParams,
			IBundleProvider theBundleProvider,
			Integer theCount,
			BundleTypeEnum theBundleTypeEnum,
			String theLinkSelf) {
		Set<Include> includes = getRequestIncludesFromParams(theParams);

		if (theCount == null) {
			theCount = theBundleProvider.preferredPageSize();
		}

		int offset = OffsetCalculator.calculateOffset(theRequest, theBundleProvider);

		return new ResponseBundleRequest(
				theServer,
				theBundleProvider,
				theRequest,
				offset,
				theCount,
				theLinkSelf,
				includes,
				theBundleTypeEnum,
				null);
	}

	public MethodReturnTypeEnum getMethodReturnType() {
		return myMethodReturnType;
	}

	@Override
	public String getResourceName() {
		return myResourceName;
	}

	protected void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	/**
	 * If the response is a bundle, this type will be placed in the root of the bundle (can be null)
	 */
	protected abstract BundleTypeEnum getResponseBundleType();

	public abstract ReturnTypeEnum getReturnType();

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest)
			throws BaseServerResponseException, IOException {
		IBaseResource response = doInvokeServer(theServer, theRequest);
		/*
		When we write directly to an HttpServletResponse, the invocation returns null. However, we still want to invoke
		the SERVER_OUTGOING_RESPONSE pointcut.
		*/
		if (response == null) {
			ResponseDetails responseDetails = new ResponseDetails();
			responseDetails.setResponseCode(Constants.STATUS_HTTP_200_OK);
			callOutgoingResponseHook(theRequest, responseDetails);
			return null;
		} else {
			Set<SummaryEnum> summaryMode = RestfulServerUtils.determineSummaryMode(theRequest);
			ResponseDetails responseDetails = new ResponseDetails();
			responseDetails.setResponseResource(response);
			responseDetails.setResponseCode(Constants.STATUS_HTTP_200_OK);
			if (!callOutgoingResponseHook(theRequest, responseDetails)) {
				return null;
			}

			return RestfulServerUtils.streamResponseAsResource(
					theServer,
					responseDetails.getResponseResource(),
					summaryMode,
					responseDetails.getResponseCode(),
					isAddContentLocationHeader(),
					theRequest.isRespondGzip(),
					theRequest,
					null,
					null);
		}
	}

	public abstract Object invokeServer(
			IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams)
			throws InvalidRequestException, InternalErrorException;

	/**
	 * Should the response include a Content-Location header. Search method bunding (and any others?) may override this to disable the content-location, since it doesn't make sense
	 */
	protected boolean isAddContentLocationHeader() {
		return true;
	}

	public enum MethodReturnTypeEnum {
		BUNDLE,
		BUNDLE_PROVIDER,
		BUNDLE_RESOURCE,
		LIST_OF_RESOURCES,
		METHOD_OUTCOME,
		VOID,
		RESOURCE
	}

	public enum ReturnTypeEnum {
		BUNDLE,
		RESOURCE
	}

	public static boolean callOutgoingResponseHook(RequestDetails theRequest, ResponseDetails theResponseDetails) {
		HttpServletRequest servletRequest = null;
		HttpServletResponse servletResponse = null;
		if (theRequest instanceof ServletRequestDetails) {
			servletRequest = ((ServletRequestDetails) theRequest).getServletRequest();
			servletResponse = ((ServletRequestDetails) theRequest).getServletResponse();
		}

		HookParams responseParams = new HookParams();
		responseParams.add(RequestDetails.class, theRequest);
		responseParams.addIfMatchesType(ServletRequestDetails.class, theRequest);
		responseParams.add(IBaseResource.class, theResponseDetails.getResponseResource());
		responseParams.add(ResponseDetails.class, theResponseDetails);
		responseParams.add(HttpServletRequest.class, servletRequest);
		responseParams.add(HttpServletResponse.class, servletResponse);
		if (theRequest.getInterceptorBroadcaster() != null) {
			return theRequest.getInterceptorBroadcaster().callHooks(Pointcut.SERVER_OUTGOING_RESPONSE, responseParams);
		}
		return true;
	}

	public static void callOutgoingFailureOperationOutcomeHook(
			RequestDetails theRequestDetails, IBaseOperationOutcome theOperationOutcome) {
		HookParams responseParams = new HookParams();
		responseParams.add(RequestDetails.class, theRequestDetails);
		responseParams.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		responseParams.add(IBaseOperationOutcome.class, theOperationOutcome);

		if (theRequestDetails.getInterceptorBroadcaster() != null) {
			theRequestDetails
					.getInterceptorBroadcaster()
					.callHooks(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME, responseParams);
		}
	}
}
