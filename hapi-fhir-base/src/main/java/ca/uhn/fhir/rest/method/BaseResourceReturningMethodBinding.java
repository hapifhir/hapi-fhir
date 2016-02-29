package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.base.resource.BaseOperationOutcome;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.client.exceptions.InvalidResponseException;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.util.BundleUtil;
import ca.uhn.fhir.util.ReflectionUtil;
import ca.uhn.fhir.util.UrlUtil;

public abstract class BaseResourceReturningMethodBinding extends BaseMethodBinding<Object> {
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
		set.add(Constants.PARAM_SUMMARY);
		set.add(Constants.PARAM_ELEMENTS);
		set.add(ResponseHighlighterInterceptor.PARAM_RAW);
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
				if (!Object.class.equals(collectionType) && !IBaseResource.class.isAssignableFrom(collectionType)) {
					throw new ConfigurationException(
							"Method " + theMethod.getDeclaringClass().getSimpleName() + "#" + theMethod.getName() + " returns an invalid collection generic type: " + collectionType);
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
		} else if (MethodOutcome.class.isAssignableFrom(methodReturnType)) {
			myMethodReturnType = MethodReturnTypeEnum.METHOD_OUTCOME;
		} else {
			throw new ConfigurationException(
					"Invalid return type '" + methodReturnType.getCanonicalName() + "' on method '" + theMethod.getName() + "' on type: " + theMethod.getDeclaringClass().getCanonicalName());
		}

		if (theReturnResourceType != null) {
			if (IBaseResource.class.isAssignableFrom(theReturnResourceType)) {
				if (Modifier.isAbstract(theReturnResourceType.getModifiers()) || Modifier.isInterface(theReturnResourceType.getModifiers())) {
					// If we're returning an abstract type, that's ok
				} else {
					myResourceType = (Class<? extends IResource>) theReturnResourceType;
					myResourceName = theContext.getResourceDefinition(myResourceType).getName();
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

	/**
	 * If the response is a bundle, this type will be placed in the root of the bundle (can be null)
	 */
	protected abstract BundleTypeEnum getResponseBundleType();

	public abstract ReturnTypeEnum getReturnType();

	@Override
	public Object invokeClient(String theResponseMimeType, Reader theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) {
		IParser parser = createAppropriateParserForParsingResponse(theResponseMimeType, theResponseReader, theResponseStatusCode);

		switch (getReturnType()) {
		case BUNDLE: {
			
			Bundle dstu1bundle = null;
			IBaseBundle dstu2bundle = null;
			List<IBaseResource> listOfResources = null;
			if (getMethodReturnType() == MethodReturnTypeEnum.BUNDLE || getContext().getVersion().getVersion() == FhirVersionEnum.DSTU1) {
				if (myResourceType != null) {
					dstu1bundle = parser.parseBundle(myResourceType, theResponseReader);
				} else {
					dstu1bundle = parser.parseBundle(theResponseReader);
				}
			} else {
				Class<? extends IBaseResource> type = getContext().getResourceDefinition("Bundle").getImplementingClass();
				dstu2bundle = (IBaseBundle) parser.parseResource(type, theResponseReader);
				listOfResources = BundleUtil.toListOfResources(getContext(), dstu2bundle);
			}
			
			switch (getMethodReturnType()) {
			case BUNDLE:
				return dstu1bundle;
			case BUNDLE_RESOURCE:
				return dstu2bundle;
			case LIST_OF_RESOURCES:
				if (myResourceListCollectionType != null) {
					for (Iterator<IBaseResource> iter = listOfResources.iterator(); iter.hasNext(); ) {
						IBaseResource next = iter.next();
						if (!myResourceListCollectionType.isAssignableFrom(next.getClass())) {
							ourLog.debug("Not returning resource of type {} because it is not a subclass or instance of {}", next.getClass(), myResourceListCollectionType);
							iter.remove();
						}
					}
				}
				return listOfResources;
			case RESOURCE:
				List<IResource> list = dstu1bundle.toListOfResources();
				if (list.size() == 0) {
					return null;
				} else if (list.size() == 1) {
					return list.get(0);
				} else {
					throw new InvalidResponseException(theResponseStatusCode, "FHIR server call returned a bundle with multiple resources, but this method is only able to returns one.");
				}
			case BUNDLE_PROVIDER:
				throw new IllegalStateException("Return type of " + IBundleProvider.class.getSimpleName() + " is not supported in clients");
			default:
				break;
			}
			break;
		}
		case RESOURCE: {
			IBaseResource resource;
			if (myResourceType != null) {
				resource = parser.parseResource(myResourceType, theResponseReader);
			} else {
				resource = parser.parseResource(theResponseReader);
			}

			MethodUtil.parseClientRequestResourceHeaders(null, theHeaders, resource);

			switch (getMethodReturnType()) {
			case BUNDLE:
				return Bundle.withSingleResource((IResource) resource);
			case LIST_OF_RESOURCES:
				return Collections.singletonList(resource);
			case RESOURCE:
				return resource;
			case BUNDLE_PROVIDER:
				throw new IllegalStateException("Return type of " + IBundleProvider.class.getSimpleName() + " is not supported in clients");
			case BUNDLE_RESOURCE:
				// TODO: we should support this
				throw new IllegalStateException("Return type of " + IBundleProvider.class.getSimpleName() + " is not yet supported in clients");
			case METHOD_OUTCOME:
				MethodOutcome retVal = new MethodOutcome();
				retVal.setOperationOutcome((BaseOperationOutcome) resource);
				return retVal;
			}
			break;
		}
		}

		throw new IllegalStateException("Should not get here!");
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) throws BaseServerResponseException, IOException {

//		byte[] requestContents = loadRequestContents(theRequest);
		byte[] requestContents = null;

		final ResourceOrDstu1Bundle responseObject = invokeServer(theServer, theRequest, requestContents);

		Set<SummaryEnum> summaryMode = RestfulServerUtils.determineSummaryMode(theRequest);
		if (responseObject.getResource() != null) {
			
			for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = theServer.getInterceptors().get(i);
				boolean continueProcessing = next.outgoingResponse(theRequest, responseObject.getResource());
				if (!continueProcessing) {
					return null;
				}
			}
			
			boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);
			
			return theRequest.getResponse().streamResponseAsResource(responseObject.getResource(), prettyPrint, summaryMode, Constants.STATUS_HTTP_200_OK, theRequest.isRespondGzip(),
					isAddContentLocationHeader());
			
		} else {
			// Is this request coming from a browser
			String uaHeader = theRequest.getHeader("user-agent");
			boolean requestIsBrowser = false;
			if (uaHeader != null && uaHeader.contains("Mozilla")) {
				requestIsBrowser = true;
			}
			
			for (int i = theServer.getInterceptors().size() - 1; i >= 0; i--) {
				IServerInterceptor next = theServer.getInterceptors().get(i);
				boolean continueProcessing = next.outgoingResponse(theRequest, responseObject.getDstu1Bundle());
				if (!continueProcessing) {
					ourLog.debug("Interceptor {} returned false, not continuing processing");
					return null;
				}
			}

			return theRequest.getResponse().streamResponseAsBundle(responseObject.getDstu1Bundle(), summaryMode, theRequest.isRespondGzip(), requestIsBrowser);
		}
	}

	public ResourceOrDstu1Bundle invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, byte[] requestContents) {
		// Method params
		Object[] params = new Object[getParameters().size()];
		for (int i = 0; i < getParameters().size(); i++) {
			IParameter param = getParameters().get(i);
			if (param != null) {
				params[i] = param.translateQueryParametersIntoServerArgument(theRequest, this);
			}
		}

		Object resultObj = invokeServer(theServer, theRequest, params);

		Integer count = RestfulServerUtils.extractCountParameter(theRequest);

		final ResourceOrDstu1Bundle responseObject;

		switch (getReturnType()) {
		case BUNDLE: {

			/*
			 * Figure out the self-link for this request
			 */
			String serverBase = theRequest.getServerBaseForRequest();
			String linkSelf;
			StringBuilder b = new StringBuilder();
			b.append(serverBase);
			if (isNotBlank(theRequest.getRequestPath())) {
				b.append('/');
				b.append(theRequest.getRequestPath());
			}
			// For POST the URL parameters get jumbled with the post body parameters so don't include them, they might be huge
			if (theRequest.getRequestType() == RequestTypeEnum.GET) {
				boolean first = true;
				Map<String, String[]> parameters = theRequest.getParameters();
				for (Entry<String, String[]> nextParams : parameters.entrySet()) {
					for (String nextParamValue : nextParams.getValue()) {
						if (first) {
							b.append('?');
							first = false;
						} else {
							b.append('&');
						}
						b.append(UrlUtil.escape(nextParams.getKey()));
						b.append('=');
						b.append(UrlUtil.escape(nextParamValue));
					}
				}
			}
			linkSelf = b.toString();

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
				bundleFactory.addRootPropertiesToBundle(null, theRequest.getFhirServerBase(), linkSelf, count, getResponseBundleType(), lastUpdated);

				responseObject = new ResourceOrDstu1Bundle(resource);
				break;
				
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
				int start = Math.max(0, Math.min(offsetI, result.size() - 1));
				
				IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();

				EncodingEnum responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest, theServer.getDefaultResponseEncoding());
				EncodingEnum linkEncoding = theRequest.getParameters().containsKey(Constants.PARAM_FORMAT) ? responseEncoding : null;

				boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);
				bundleFactory.initializeBundleFromBundleProvider(theServer, result, linkEncoding, theRequest.getFhirServerBase(), linkSelf, prettyPrint, start, count, null, getResponseBundleType(), includes);
				Bundle bundle = bundleFactory.getDstu1Bundle();
				if (bundle != null) {
					responseObject = new ResourceOrDstu1Bundle(bundle);
				} else {
					IBaseResource resBundle = bundleFactory.getResourceBundle();
					responseObject = new ResourceOrDstu1Bundle(resBundle);
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

			IBaseResource resource = result.getResources(0, 1).get(0);
			responseObject = new ResourceOrDstu1Bundle(resource);
			break;
		}
		default:
			throw new IllegalStateException(); // should not happen
		}
		return responseObject;
	}

	public abstract Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException;

	/**
	 * Should the response include a Content-Location header. Search method bunding (and any others?) may override this to disable the content-location, since it doesn't make sense
	 */
	protected boolean isAddContentLocationHeader() {
		return true;
	}

	protected void setResourceName(String theResourceName) {
		myResourceName = theResourceName;
	}

	public enum MethodReturnTypeEnum {
		BUNDLE, BUNDLE_PROVIDER, BUNDLE_RESOURCE, LIST_OF_RESOURCES, METHOD_OUTCOME, RESOURCE
	}

	public static class ResourceOrDstu1Bundle {

		private final Bundle myDstu1Bundle;
		private final IBaseResource myResource;

		public ResourceOrDstu1Bundle(Bundle theBundle) {
			myDstu1Bundle = theBundle;
			myResource = null;
		}

		public ResourceOrDstu1Bundle(IBaseResource theResource) {
			myResource = theResource;
			myDstu1Bundle = null;
		}

		public Bundle getDstu1Bundle() {
			return myDstu1Bundle;
		}

		public IBaseResource getResource() {
			return myResource;
		}

	}

	public enum ReturnTypeEnum {
		BUNDLE, RESOURCE
	}

}
