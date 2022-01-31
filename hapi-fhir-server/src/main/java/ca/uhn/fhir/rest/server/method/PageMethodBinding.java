package ca.uhn.fhir.rest.server.method;

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
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.RestfulServerUtils.ResponseEncoding;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.ReflectionUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
	public IBaseResource doInvokeServer(IRestfulServer<?> theServer, RequestDetails theRequest) {
		return handlePagingRequest(theServer, theRequest, theRequest.getParameters().get(Constants.PARAM_PAGINGACTION)[0]);
	}
	
	private IBaseResource handlePagingRequest(IRestfulServer<?> theServer, RequestDetails theRequest, String thePagingAction) {
		IPagingProvider pagingProvider = theServer.getPagingProvider();
		if (pagingProvider == null) {
			throw new InvalidRequestException(Msg.code(416) + "This server does not support paging");
		}

		// Interceptor invoke: SERVER_INCOMING_REQUEST_PRE_HANDLED
		IServerInterceptor.ActionRequestDetails details = new IServerInterceptor.ActionRequestDetails(theRequest);
		populateActionRequestDetailsForInterceptor(theRequest, details, ReflectionUtil.EMPTY_OBJECT_ARRAY);
		HookParams preHandledParams = new HookParams();
		preHandledParams.add(RestOperationTypeEnum.class, theRequest.getRestOperationType());
		preHandledParams.add(RequestDetails.class, theRequest);
		preHandledParams.addIfMatchesType(ServletRequestDetails.class, theRequest);
		preHandledParams.add(IServerInterceptor.ActionRequestDetails.class, details);
		if (theRequest.getInterceptorBroadcaster() != null) {
			theRequest
				.getInterceptorBroadcaster()
				.callHooks(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED, preHandledParams);
		}

		Integer offsetI;
		int start = 0;
		IBundleProvider bundleProvider;

		String pageId = null;
		String[] pageIdParams = theRequest.getParameters().get(Constants.PARAM_PAGEID);
		if (pageIdParams != null) {
			if (pageIdParams.length > 0) {
				if (isNotBlank(pageIdParams[0])) {
					pageId = pageIdParams[0];
				}
			}
		}

		if (pageId != null) {
			// This is a page request by Search ID and Page ID

			bundleProvider = pagingProvider.retrieveResultList(theRequest, thePagingAction, pageId);
			validateHaveBundleProvider(thePagingAction, bundleProvider);

		} else {
			// This is a page request by Search ID and Offset

			bundleProvider = pagingProvider.retrieveResultList(theRequest, thePagingAction);
			validateHaveBundleProvider(thePagingAction, bundleProvider);

			offsetI = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_PAGINGOFFSET);
			if (offsetI == null || offsetI < 0) {
				offsetI = 0;
			}

			Integer totalNum = bundleProvider.size();
			start = offsetI;
			if (totalNum != null) {
				start = Math.min(start, totalNum);
			}
		}

		ResponseEncoding responseEncoding = RestfulServerUtils.determineResponseEncodingNoDefault(theRequest, theServer.getDefaultResponseEncoding());

		Set<Include> includes = new HashSet<>();
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

		Integer count = RestfulServerUtils.extractCountParameter(theRequest);
		if (count == null) {
			count = pagingProvider.getDefaultPageSize();
		} else if (count > pagingProvider.getMaximumPageSize()) {
			count = pagingProvider.getMaximumPageSize();
		}

		return createBundleFromBundleProvider(theServer, theRequest, count, linkSelf, includes, bundleProvider, start, bundleType, encodingEnum, thePagingAction);
	}

	private void validateHaveBundleProvider(String thePagingAction, IBundleProvider theBundleProvider) {
		// Return an HTTP 410 if the search is not known
		if (theBundleProvider == null) {
			ourLog.info("Client requested unknown paging ID[{}]", thePagingAction);
			String msg = getContext().getLocalizer().getMessage(PageMethodBinding.class, "unknownSearchId", thePagingAction);
			throw new ResourceGoneException(Msg.code(417) + msg);
		}
	}

	@Nonnull
	@Override
	public RestOperationTypeEnum getRestOperationType() {
		return RestOperationTypeEnum.GET_PAGE;
	}

	@Override
	public MethodMatchEnum incomingServerRequestMatchesMethod(RequestDetails theRequest) {
		String[] pageId = theRequest.getParameters().get(Constants.PARAM_PAGINGACTION);
		if (pageId == null || pageId.length == 0 || isBlank(pageId[0])) {
			return MethodMatchEnum.NONE;
		}
		if (theRequest.getRequestType() != RequestTypeEnum.GET) {
			return MethodMatchEnum.NONE;
		}

		return MethodMatchEnum.EXACT;
	}


}
