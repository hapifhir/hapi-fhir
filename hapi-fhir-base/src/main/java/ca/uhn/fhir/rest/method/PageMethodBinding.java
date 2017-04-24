package ca.uhn.fhir.rest.method;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
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

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.client.BaseHttpClientInvocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.EncodingEnum;
import ca.uhn.fhir.rest.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.IRestfulServer;
import ca.uhn.fhir.rest.server.IVersionSpecificBundleFactory;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.CoverageIgnore;

public class PageMethodBinding extends BaseResourceReturningMethodBinding {

	public PageMethodBinding(FhirContext theContext, Method theMethod) {
		super(null, theMethod, theContext, null);
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PageMethodBinding.class);

	public IBaseResource provider() {
		return null;
	}

	@Override
	protected BundleTypeEnum getResponseBundleType() {
		return null;
	}

	@Override
	public ReturnTypeEnum getReturnType() {
		return ReturnTypeEnum.BUNDLE;
	}

	@Override
	public Object invokeServer(IRestfulServer<?> theServer, RequestDetails theRequest, Object[] theMethodParams) throws InvalidRequestException, InternalErrorException {
		return handlePagingRequest(theServer, theRequest, theRequest.getParameters().get(Constants.PARAM_PAGINGACTION)[0]);
	}

	@Override
	public ResourceOrDstu1Bundle doInvokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) {
		IBase bundle = handlePagingRequest(theServer, theRequest, theRequest.getParameters().get(Constants.PARAM_PAGINGACTION)[0]);
		if (bundle instanceof Bundle) {
			return new ResourceOrDstu1Bundle((Bundle) bundle);
		}
		return new ResourceOrDstu1Bundle((IBaseResource) bundle);
	}
	
	private IBase handlePagingRequest(IRestfulServer<?> theServer, RequestDetails theRequest, String thePagingAction) {
		IPagingProvider pagingProvider = theServer.getPagingProvider();
		if (pagingProvider == null) {
			throw new InvalidRequestException("This server does not support paging");
		}
		IBundleProvider resultList = pagingProvider.retrieveResultList(thePagingAction);
		if (resultList == null) {
			ourLog.info("Client requested unknown paging ID[{}]", thePagingAction);
			String msg = getContext().getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", thePagingAction);
			throw new ResourceGoneException(msg);
		}

		Integer count = RestfulServerUtils.extractCountParameter(theRequest);
		if (count == null) {
			count = pagingProvider.getDefaultPageSize();
		} else if (count > pagingProvider.getMaximumPageSize()) {
			count = pagingProvider.getMaximumPageSize();
		}

		Integer offsetI = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_PAGINGOFFSET);
		if (offsetI == null || offsetI < 0) {
			offsetI = 0;
		}

		Integer totalNum = resultList.size();
		int start = offsetI;
		if (totalNum != null) {
			start = Math.min(start, totalNum - 1);
		}
		
		ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest, theServer.getDefaultResponseEncoding());
		boolean prettyPrint = RestfulServerUtils.prettyPrintResponse(theServer, theRequest);

		IVersionSpecificBundleFactory bundleFactory = theServer.getFhirContext().newBundleFactory();

		Set<Include> includes = new HashSet<Include>();
		String[] reqIncludes = theRequest.getParameters().get(Constants.PARAM_INCLUDE);
		if (reqIncludes != null) {
			for (String nextInclude : reqIncludes) {
				includes.add(new Include(nextInclude));
			}
		}

		String linkSelfBase = theRequest.getFhirServerBase(); // myServerAddressStrategy.determineServerBase(getServletContext(),
																				// theRequest.getServletRequest());
		String completeUrl = theRequest.getCompleteUrl();
		String linkSelf = linkSelfBase + completeUrl.substring(theRequest.getCompleteUrl().indexOf('?'));

		BundleTypeEnum bundleType = null;
		String[] bundleTypeValues = theRequest.getParameters().get(Constants.PARAM_BUNDLETYPE);
		if (bundleTypeValues != null) {
			bundleType = BundleTypeEnum.VALUESET_BINDER.fromCodeString(bundleTypeValues[0]);
		}

		EncodingEnum encodingEnum = null;
		if (responseEncoding != null) {
			encodingEnum = responseEncoding.getEncoding();
		}
		bundleFactory.initializeBundleFromBundleProvider(theServer, resultList, encodingEnum, theRequest.getFhirServerBase(), linkSelf, prettyPrint, start, count, thePagingAction, bundleType, includes);

		Bundle bundle = bundleFactory.getDstu1Bundle();
		if (bundle != null) {
			return bundle;
		}
		return bundleFactory.getResourceBundle();
		// if (bundle != null) {
		// for (int i = getInterceptors().size() - 1; i >= 0; i--) {
		// IServerInterceptor next = getInterceptors().get(i);
		// boolean continueProcessing = next.outgoingResponse(theRequest, bundle, theRequest.getServletRequest(),
		// theRequest.getServletResponse());
		// if (!continueProcessing) {
		// ourLog.debug("Interceptor {} returned false, not continuing processing");
		// return;
		// }
		// }
		// theRequest.getResponse().streamResponseAsBundle(bundle, summaryMode, respondGzip, requestIsBrowser);
		// } else {
		// IBaseResource resBundle = bundleFactory.getResourceBundle();
		// for (int i = getInterceptors().size() - 1; i >= 0; i--) {
		// IServerInterceptor next = getInterceptors().get(i);
		// boolean continueProcessing = next.outgoingResponse(theRequest, resBundle, theRequest.getServletRequest(),
		// theRequest.getServletResponse());
		// if (!continueProcessing) {
		// ourLog.debug("Interceptor {} returned false, not continuing processing");
		// return;
		// }
		// }
		// theRequest.getResponse().streamResponseAsResource(resBundle, prettyPrint, summaryMode,
		// Constants.STATUS_HTTP_200_OK, theRequest.isRespondGzip(), false);
		// }
	}

	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.GET_PAGE;
	}

	@Override
	public boolean incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		String[] pageId = theRequest.getParameters().get(Constants.PARAM_PAGINGACTION);
		if (pageId == null || pageId.length == 0 || isBlank(pageId[0])) {
			return false;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET) {
			return false;
		}
		return true;
	}

	@CoverageIgnore
	@Override
	public BaseHttpClientInvocation invokeClient(Object[] theArgs) throws InternalErrorException {
		throw new UnsupportedOperationException();
	}

}
