package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.BundleLinks;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ReflectionUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

public abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding<Object> {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseResourceReturningMethodBinding.class);

	private MethodReturnTypeEnum myMethodReturnType;
	private String myResourceName;

	@SuppressWarnings("unchecked")
	public BaseResourceReturningMethodBinding(Class<?> theReturnResourceType, Method theMethod, FhirContext theContext, Object theProvider) {
		super(theMethod, theContext, theProvider);

		Class<?> methodReturnType = theMethod.getReturnType();

		Set<Class<?>> expectedReturnTypes = provideExpectedReturnTypes();
		if (expectedReturnTypes != null) {

			Validate.isTrue(expectedReturnTypes.contains(methodReturnType), "Unexpected method return type on %s - Allowed: %s", theMethod, expectedReturnTypes);

		} else if (Collection.class.isAssignableFrom(methodReturnType)) {

			myMethodReturnType = MethodReturnTypeEnum.LIST_OF_RESOURCES;
			Class<?> collectionType = ReflectionUtil.getGenericCollectionTypeOfMethodReturnType(theMethod);
			if (collectionType != null) {
				if (!Object.class.equals(collectionType) && !IBaseResource.class.isAssignableFrom(collectionType)) {
					throw new ConfigurationException(Msg.code(433) + "Method " + theMethod.getDeclaringClass().getSimpleName() + "#" + theMethod.getName() + " returns an invalid collection generic type: " + collectionType);
				}
			}

		} else if (IBaseResource.class.isAssignableFrom(methodReturnType)) {

			if ( IBaseBundle.class.isAssignableFrom(methodReturnType)) {
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
			throw new ConfigurationException(Msg.code(434) + "Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			if (IBaseResource.class.isAssignableFrom(theReturnResourceType)) {

				// If we're returning an abstract type, that's ok, but if we know the resource
				// type let's grab it
				if (!Modifier.isAbstract(theReturnResourceType.getModifiers()) && !Modifier.isInterface(theReturnResourceType.getModifiers())) {
					Class<? extends IBaseResource> resourceType = (Class<? extends IResource>) theReturnResourceType;
					RuntimeResourceDefinition resourceDefinition = theContext.getResourceDefinition(resourceType);
					myResourceName = resourceDefinition.getName();
				}
			}
		}

	}

	/**
	 * Subclasses may override
	 */
	protected Set<Class<?>> provideExpectedReturnTypes() {
		return null;
	}

	IBaseResource createBundleFromBundleProvider(IRestfulServer<?> theServer, RequestDetails theRequest, Integer theLimit, String theLinkSelf, Set<Include> theIncludes,
																IBundleProvider theResult, int theOffset, BundleTypeEnum theBundleType, EncodingEnum theLinkEncoding, String theSearchId) {
		IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();
		final Integer offset;
		Integer limit = theLimit;

		if (theResult.getCurrentPageOffset() != null) {
			offset = theResult.getCurrentPageOffset();
			limit = theResult.getCurrentPageSize();
			Validate.notNull(limit, "IBundleProvider returned a non-null offset, but did not return a non-null page size");
		} else {
			offset = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_OFFSET);
		}

		int numToReturn;
		String searchId = null;
		List<IBaseResource> resourceList;
		Integer numTotalResults = theResult.size();

		int pageSize;
		if (offset != null || !theServer.canStoreSearchResults()) {
			if (limit != null) {
				pageSize = limit;
			} else {
				if (theServer.getDefaultPageSize() != null) {
					pageSize = theServer.getDefaultPageSize();
				} else {
					pageSize = numTotalResults != null ? numTotalResults : Integer.MAX_VALUE;
				}
			}
			numToReturn = pageSize;

			if ((offset != null && !isOffsetModeHistory()) || theResult.getCurrentPageOffset() != null) {
				// When offset query is done theResult already contains correct amount (+ their includes etc.) so return everything
				resourceList = theResult.getResources(0, Integer.MAX_VALUE);
			} else if (numToReturn > 0) {
				resourceList = theResult.getResources(0, numToReturn);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

		} else {
			IPagingProvider pagingProvider = theServer.getPagingProvider();
			if (limit == null || ((Integer) limit).equals(0)) {
				pageSize = pagingProvider.getDefaultPageSize();
			} else {
				pageSize = Math.min(pagingProvider.getMaximumPageSize(), limit);
			}
			numToReturn = pageSize;

			if (numTotalResults != null) {
				numToReturn = Math.min(numToReturn, numTotalResults - theOffset);
			}

			if (numToReturn > 0 || theResult.getCurrentPageId() != null) {
				resourceList = theResult.getResources(theOffset, numToReturn + theOffset);
			} else {
				resourceList = Collections.emptyList();
			}
			RestfulServerUtils.validateResourceListNotNull(resourceList);

			if (numTotalResults == null) {
				numTotalResults = theResult.size();
			}

			if (theSearchId != null) {
				searchId = theSearchId;
			} else {
				if (numTotalResults == null || numTotalResults > numToReturn) {
					searchId = pagingProvider.storeResultList(theRequest, theResult);
					if (isBlank(searchId)) {
						ourLog.info("Found {} results but paging provider did not provide an ID to use for paging", numTotalResults);
						searchId = null;
					}
				}
			}
		}

		/*
		 * Remove any null entries in the list - This generally shouldn't happen but can if
		 * data has been manually purged from the JPA database
		 */
		boolean hasNull = false;
		for (IBaseResource next : resourceList) {
			if (next == null) {
				hasNull = true;
				break;
			}
		}
		if (hasNull) {
			resourceList.removeIf(Objects::isNull);
		}

		/*
		 * Make sure all returned resources have an ID (if not, this is a bug
		 * in the user server code)
		 */
		for (IBaseResource next : resourceList) {
			if (next.getIdElement() == null || next.getIdElement().isEmpty()) {
				if (!(next instanceof IBaseOperationOutcome)) {
					throw new InternalErrorException(Msg.code(435) + "Server method returned resource of type[" + next.getClass().getSimpleName() + "] with no ID specified (IResource#setId(IdDt) must be called)");
				}
			}
		}

		BundleLinks links = new BundleLinks(theRequest.getFhirServerBase(), theIncludes, RestfulServerUtils.prettyPrintResponse(theServer, theRequest), theBundleType);
		links.setSelf(theLinkSelf);

		if (theResult.getCurrentPageOffset() != null) {

			if (isNotBlank(theResult.getNextPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theRequest.getRequestPath(), theRequest.getTenantId(), offset + limit, limit, theRequest.getParameters()));
			}
			if (isNotBlank(theResult.getPreviousPageId())) {
				links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theRequest.getRequestPath(), theRequest.getTenantId(), Math.max(offset - limit, 0), limit, theRequest.getParameters()));
			}

		}

		if (offset != null || (!theServer.canStoreSearchResults() && !isEverythingOperation(theRequest)) || isOffsetModeHistory()) {
			// Paging without caching
			// We're doing offset pages
			int requestedToReturn = numToReturn;
			if (theServer.getPagingProvider() == null && offset != null) {
				// There is no paging provider at all, so assume we're querying up to all the results we need every time
				requestedToReturn += offset;
			}
			if (numTotalResults == null || requestedToReturn < numTotalResults) {
				if (!resourceList.isEmpty()) {
					links.setNext(RestfulServerUtils.createOffsetPagingLink(links, theRequest.getRequestPath(), theRequest.getTenantId(), defaultIfNull(offset, 0) + numToReturn, numToReturn, theRequest.getParameters()));
				}
			}
			if (offset != null && offset > 0) {
				int start = Math.max(0, theOffset - pageSize);
				links.setPrev(RestfulServerUtils.createOffsetPagingLink(links, theRequest.getRequestPath(), theRequest.getTenantId(), start, pageSize, theRequest.getParameters()));
			}
		} else if (isNotBlank(theResult.getCurrentPageId())) {
			// We're doing named pages
			searchId = theResult.getUuid();
			if (isNotBlank(theResult.getNextPageId())) {
				links.setNext(RestfulServerUtils.createPagingLink(links, theRequest, searchId, theResult.getNextPageId(), theRequest.getParameters()));
			}
			if (isNotBlank(theResult.getPreviousPageId())) {
				links.setPrev(RestfulServerUtils.createPagingLink(links, theRequest, searchId, theResult.getPreviousPageId(), theRequest.getParameters()));
			}
		} else if (searchId != null) {
			/*
			 * We're doing offset pages - Note that we only return paging links if we actually
			 * included some results in the response. We do this to avoid situations where
			 * people have faked the offset number to some huge number to avoid them getting
			 * back paging links that don't make sense.
			 */
			if (resourceList.size() > 0) {
				if (numTotalResults == null || theOffset + numToReturn < numTotalResults) {
					links.setNext((RestfulServerUtils.createPagingLink(links, theRequest, searchId, theOffset + numToReturn, numToReturn, theRequest.getParameters())));
				}
				if (theOffset > 0) {
					int start = Math.max(0, theOffset - pageSize);
					links.setPrev(RestfulServerUtils.createPagingLink(links, theRequest, searchId, start, pageSize, theRequest.getParameters()));
				}
			}
		}

		bundleFactory.addRootPropertiesToBundle(theResult.getUuid(), links, theResult.size(), theResult.getPublished());
		bundleFactory.addResourcesToBundle(new ArrayList<>(resourceList), theBundleType, links.serverBase, theServer.getBundleInclusionRule(), theIncludes);

		return bundleFactory.getResourceBundle();

	}

	protected boolean isOffsetModeHistory() {
		return false;
	}

	private boolean isEverythingOperation(RequestDetails theRequest) {
		return (theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_TYPE
			|| theRequest.getRestOperationType() == RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE)
			&& theRequest.getOperation() != null && theRequest.getOperation().equals("$everything");
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

				BundleLinks bundleLinks = new BundleLinks(theRequest.getServerBaseForRequest(), null, RestfulServerUtils.prettyPrintResponse(theServer, theRequest), getResponseBundleType());
				bundleLinks.setSelf(RestfulServerUtils.createLinkSelf(theRequest.getFhirServerBase(), theRequest));

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
					IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();
					bundleFactory.initializeWithBundleResource(resource);
					bundleFactory.addRootPropertiesToBundle(null, bundleLinks, count, lastUpdated);

					responseObject = resource;
				} else {
					Set<Include> includes = getRequestIncludesFromParams(params);

					IBundleProvider result = (IBundleProvider) resultObj;
					if (count == null) {
						count = result.preferredPageSize();
					}

					Integer offsetI = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_PAGINGOFFSET);
					if (offsetI == null || offsetI < 0) {
						offsetI = 0;
					}

					Integer resultSize = result.size();
					int start;
					if (resultSize != null) {
						start = Math.max(0, Math.min(offsetI, resultSize - 1));
					} else {
						start = offsetI;
					}

					ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest, theServer.getDefaultResponseEncoding());
					EncodingEnum linkEncoding = theRequest.getParameters().containsKey(Constants.PARAM_FORMAT) && responseEncoding != null ? responseEncoding.getEncoding() : null;

					responseObject = createBundleFromBundleProvider(theServer, theRequest, count, RestfulServerUtils.createLinkSelf(theRequest.getFhirServerBase(), theRequest), includes, result, start, getResponseBundleType(), linkEncoding, null);
				}
				break;
			}
			case RESOURCE: {
				IBundleProvider result = (IBundleProvider) resultObj;
				if (result.size() == 0) {
					throw new ResourceNotFoundException(Msg.code(436) + "Resource " + theRequest.getId() + " is not known");
				} else if (result.size() > 1) {
					throw new InternalErrorException(Msg.code(437) + "Method returned multiple resources");
				}

				IBaseResource resource = result.getResources(0, 1).get(0);
				responseObject = resource;
				break;
			}
			default:
				throw new IllegalStateException(Msg.code(438)); // should not happen
		}
		return responseObject;
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
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {
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
			boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);

			return theRequest.getResponse().streamResponseAsResource(responseDetails.getResponseResource(), prettyPrint, summaryMode, responseDetails.getResponseCode(), null, theRequest.isRespondGzip(), isAddContentLocationHeader());
		}
	}

	public abstract Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException;

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
			if (!theRequest.getInterceptorBroadcaster().callHooks(Pointcut.SERVER_OUTGOING_RESPONSE, responseParams)) {
				return false;
			}
		}
		return true;
	}

	public static void callOutgoingFailureOperationOutcomeHook(RequestDetails theRequestDetails, IBaseOperationOutcome theOperationOutcome) {
		HookParams responseParams = new HookParams();
		responseParams.add(RequestDetails.class, theRequestDetails);
		responseParams.addIfMatchesType(ServletRequestDetails.class, theRequestDetails);
		responseParams.add(IBaseOperationOutcome.class, theOperationOutcome);

		if (theRequestDetails.getInterceptorBroadcaster() != null) {
			theRequestDetails.getInterceptorBroadcaster().callHooks(Pointcut.SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME, responseParams);
		}
	}
}
